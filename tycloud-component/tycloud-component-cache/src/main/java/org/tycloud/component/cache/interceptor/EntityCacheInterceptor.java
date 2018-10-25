package org.tycloud.component.cache.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tycloud.component.cache.Redis;
import org.tycloud.component.cache.annotation.CacheKey;
import org.tycloud.component.cache.annotation.EntityCache;
import org.tycloud.component.cache.enumeration.CacheOperation;
import org.tycloud.component.cache.enumeration.CacheType;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.context.RequestContextEntityType;
import org.tycloud.core.foundation.context.SpringContextHelper;
import org.tycloud.core.foundation.enumeration.StorageMode;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;
import org.tycloud.core.rdbms.service.BaseService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *
 * <pre>
 *  Tyrest
 *  File: SnapshotInterceptor.java
 *
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 *
 *  Description:
 *  Description:缓存拦截器
 *
 *  Notes:
 *  $Id: SnapshotInterceptor.java  Tyrest\magintrursh $
 *
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月17日		magintrursh		Initial.
 *
 * </pre>
 */
@Aspect
@Order(value = 0)
@Component
public class EntityCacheInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(EntityCacheInterceptor.class);





	private Map<String,Map<String,SortedMap<Integer,String>>> cacheOfkeyPropertiesMap = new HashMap<>();

	private static Long idModBy = 100L;

	private static String MOD = "MOD_";


	@Autowired
	private RedisTemplate redisTemplate;


	//TODO 处理缓存移除操作
	@Around( " execution(* com.baomidou.mybatisplus.mapper.BaseMapper.deleteById(..))")
	public Object deleteByMap(ProceedingJoinPoint pjp) throws Throwable {

		Object retVal  			= null;
		EntityCache entityCache = null;
		Class poClass 			= this.getPoClass(pjp);
		if(!ValidationUtil.isEmpty(poClass))
			 entityCache = (EntityCache)poClass.getAnnotation(EntityCache.class);

		if(this.needCache(entityCache))
		{
			//先从数据库根据id查询出对象
			BaseService baseService = this.getBaseService(pjp);
			if(ValidationUtil.isEmpty(RequestContext.getAttribute(RequestContextEntityType.currentBaseService)))
				RequestContext.setAttribute(RequestContextEntityType.currentBaseService,baseService);
			Object OldValue = baseService.selectById((Long)pjp.getArgs()[0]);
			if(!ValidationUtil.isEmpty(OldValue))
			{
				//删除该对象实例的所有Entity缓存
				this.processForCache(pjp, CacheOperation.REMOVE,entityCache, poClass,new Object[]{OldValue},null);
			}

		}

		if(ValidationUtil.isEmpty(entityCache) || !entityCache.cacheOnly())
			retVal  		= pjp.proceed();
		return retVal;
	}


	@Around( " execution(* com.baomidou.mybatisplus.mapper.BaseMapper.insert(..))"+
			"|| execution(* com.baomidou.mybatisplus.mapper.BaseMapper.updateById(..))")
	public Object insertOrUpdate(ProceedingJoinPoint pjp) throws Throwable {

		EntityCache entityCache = null;
		Class poClass 			= this.getPoClass(pjp);
		if(!ValidationUtil.isEmpty(poClass))
			entityCache = (EntityCache)poClass.getAnnotation(EntityCache.class);



		Object retVal  			= null;
		if(ValidationUtil.isEmpty(entityCache) || !entityCache.cacheOnly())
			retVal  			= pjp.proceed();

		if(this.needCache(entityCache))
			this.processForCache(pjp,CacheOperation.ADD,entityCache, poClass, pjp.getArgs()[0], pjp.getArgs()[0]);
		return retVal;
	}


	@Around( " execution(* com.baomidou.mybatisplus.mapper.BaseMapper.selectOne(..))"+
			"|| execution(* com.baomidou.mybatisplus.mapper.BaseMapper.selectById(..))")
	public Object selectOne(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal 			= null;
		EntityCache entityCache = null;
		Class poClass 			= this.getPoClass(pjp);
		if(!ValidationUtil.isEmpty(poClass))
			entityCache = (EntityCache)poClass.getAnnotation(EntityCache.class);
		if(this.needCache(entityCache))
		{
			retVal 		  = this.processForCache(pjp,CacheOperation.GET,entityCache, poClass,pjp.getArgs()[0],null);
			if(ValidationUtil.isEmpty(retVal))
			{
				retVal = pjp.proceed();
				if(!ValidationUtil.isEmpty(retVal))
					this.processForCache(pjp,CacheOperation.ADD, entityCache, poClass,pjp.getArgs()[0],retVal);
			}
		}else{
			if(ValidationUtil.isEmpty(entityCache) || !entityCache.cacheOnly())
				retVal = pjp.proceed();
		}
		return retVal;
	}

	private boolean needCache(EntityCache entityCache) throws Exception
	{
		return !ValidationUtil.isEmpty(entityCache);
	}



	private Class getPoClass(ProceedingJoinPoint pjp) throws Exception
	{
		//BaseService baseService = this.getBaseService(pjp);
		return (Class)RequestContext.getAttribute(RequestContextEntityType.poClassForCache);
		//return baseService.getPoClass();
	}

	private BaseService getBaseService(ProceedingJoinPoint pjp) throws Exception
	{
		BaseService baseService = (BaseService)RequestContext.getAttribute(RequestContextEntityType.currentBaseService);
		if(!ValidationUtil.isEmpty(baseService))
			return baseService;
		for(int index = 10;index<30 ;index++)
		{
			String className 		= new Throwable().getStackTrace()[index].getClassName();
			if("org.tycloud.core.rdbms.service.BaseService".equals(className))
			{
				className = new Throwable().getStackTrace()[index+1].getClassName();
				String beanName = Bean.getClassByName(className).getSimpleName();
				beanName = beanName.substring(0,1).toLowerCase()+beanName.substring(1);
				baseService = (BaseService) SpringContextHelper.getBean(beanName);
			}

			if(!ValidationUtil.isEmpty(baseService))
				break;
			if(ValidationUtil.isEmpty(baseService))
				continue;
		}

		if(ValidationUtil.isEmpty(baseService))
			throw new Exception("调用栈中找不到BaseService类.");

		return baseService;
	}





	private Object  processForCache(ProceedingJoinPoint pjp,CacheOperation operation,EntityCache entityCache,Class poClass,Object idOrPo,Object valueToCache)throws Exception
	{
		Object  valueFromCache 	= null;
		if(!ValidationUtil.isEmpty(entityCache))
		{
			Map<String,List<String>> keyPropertiesValue 	= this.getkeproPertyValues(idOrPo,poClass,entityCache);
			if(keyPropertiesValue.size() > 1 && CacheOperation.GET.equals(operation))
				throw new Exception("缓存key不能同时多个起作用.");
			if(!ValidationUtil.isEmpty(keyPropertiesValue))
			{
				for(String keyPrefix:keyPropertiesValue.keySet())
				{
					String hkey 		= CacheType.ENTITY_CACHE.name()+ Redis.VAR_SPLITOR+keyPrefix;
					String mkey 		= StringUtils.join(keyPropertiesValue.get(keyPrefix).toArray(),Redis.VAR_SPLITOR);
					switch (operation)
					{
						case ADD:
							valueFromCache = this.addIntoCache(entityCache,hkey,mkey,valueToCache);
							break;
						case GET:
							valueFromCache = this.loadFromCache(entityCache,hkey,mkey);
							break;
						case REMOVE:
							valueFromCache = this.removeFromCache(entityCache,hkey,mkey);
							break;
						case TTL:
							valueFromCache = this.refreshCacheExpire(entityCache,hkey,mkey);
							break;
					}
				}
			}
		}
		return valueFromCache;
	}


	private Object loadFromCache(EntityCache entityCache,String hkey,String mkey)throws Exception
	{
		Object valueFromCache = null;
		StorageMode storageMode  = entityCache.storageMode();
		switch (storageMode)
		{
			case HASH:
				valueFromCache = redisTemplate.opsForHash().get(hkey,mkey);
				break;
			case VALUE:
				valueFromCache = redisTemplate.opsForValue().get(hkey+Redis.VAR_SPLITOR+mkey);
				break;
			default:
				throw new Exception("自动缓存仅支持HASH和VALUE类型");
		}
		return valueFromCache;
	}


	private Object refreshCacheExpire(EntityCache entityCache,String hkey,String mkey) throws Exception
	{
		Object valueFromCache = false;
		StorageMode storageMode  = entityCache.storageMode();
		switch (storageMode)
		{
			case HASH:
				throw new Exception("Value类型的缓存才能刷新缓存时间.");
			case VALUE:
				String key  = hkey+Redis.VAR_SPLITOR+mkey;
				valueFromCache 			= redisTemplate.opsForValue().get(hkey+Redis.VAR_SPLITOR+mkey);
				if(!ValidationUtil.isEmpty(valueFromCache))
					 redisTemplate.expire(key,entityCache.expire(), TimeUnit.SECONDS);
				break;
			default:
				throw new Exception("自动缓存仅支持HASH和VALUE类型");
		}
		return valueFromCache;
	}


	private boolean addIntoCache(EntityCache entityCache,String hkey,String mkey,Object retValue)throws Exception
	{
		boolean flag = false;
		StorageMode storageMode  = entityCache.storageMode();
		switch (storageMode)
		{
			case HASH:
				redisTemplate.opsForHash().put(hkey,mkey,retValue);
				flag = true;
				break;
			case VALUE:
				String key  = hkey+ Redis.VAR_SPLITOR+mkey;
				redisTemplate.opsForValue().set(key,retValue);
				redisTemplate.expire(key,entityCache.expire(), TimeUnit.SECONDS);
				flag = true;
				break;
			default:
				throw new Exception("自动缓存仅支持HASH和VALUE类型");
		}
		return flag;
	}


	private boolean removeFromCache(EntityCache entityCache,String hkey,String mkey) throws Exception
	{
		boolean flag = false;
		StorageMode storageMode  = entityCache.storageMode();
		switch (storageMode)
		{
			case HASH:
				redisTemplate.opsForHash().delete(hkey,mkey);
				flag = true;
				break;
			case VALUE:
				redisTemplate.delete(hkey+Redis.VAR_SPLITOR+mkey);
				flag = true;
				break;
			default:
				throw new Exception("自动缓存仅支持HASH和VALUE类型");
		}
		return flag;
	}


	private Map<String,List<String>> getkeproPertyValues(Object idOrPo,Class poClass,EntityCache entityCache) throws Exception
	{
		Map<String,List<String>> keyPropertiesValue 	= new HashMap<>();
			Map<String,SortedMap<Integer,String>> keyProperties 			= this.getKeyProperties(poClass,entityCache);
			if(!ValidationUtil.isEmpty(keyProperties))
			{
				for(String keyPrefix:keyProperties.keySet())
				{
					List<String> keyOfValues 	= new ArrayList<>();
					SortedMap<Integer,String> properties 	= keyProperties.get(keyPrefix);
					for(String property:properties.values())
					{
						String propertyValue = null;
						if(idOrPo instanceof  BaseEntity)
							 propertyValue = String.valueOf(Bean.getPropertyValue(property,idOrPo));
						if(CacheKey.DEFAULT_PROPERTY.equals(property) && idOrPo instanceof Long)
							propertyValue = String.valueOf(idOrPo);
						if(!ValidationUtil.isEmpty(propertyValue))
						{
							keyOfValues.add(propertyValue);
						}
					}

					if(properties.size() != keyOfValues.size())
						continue;

					if(!ValidationUtil.isEmpty(keyOfValues))
						keyPropertiesValue.put(keyPrefix,keyOfValues);
				}
			}
		return keyPropertiesValue;
	}

	/**
	 * 解析出缓存key前缀和字段名称列表
	 * @param poClass
	 * @param entityCache
	 * @return
	 * @throws Exception
	 */
	private Map<String,SortedMap<Integer,String>> getKeyProperties(Class poClass,EntityCache entityCache) throws Exception
	{
		Map<String,SortedMap<Integer,String>> keyPropertiesMap = cacheOfkeyPropertiesMap.get(poClass.getName());
		if(ValidationUtil.isEmpty(keyPropertiesMap))
		{
				keyPropertiesMap = new HashMap<>();
				String [] keyPrefixs  		= entityCache.keyPrefix();
				if(ValidationUtil.isEmpty(keyPrefixs))
					keyPrefixs = new String []{poClass.getSimpleName()};
				for(String keyPrefix:keyPrefixs)
				{
					List<Field> poFields 	= Bean.getAllFields(poClass);
					for(Field field:poFields)
					{
						CacheKey cacheKey = field.getAnnotation(CacheKey.class);
						if(!ValidationUtil.isEmpty(cacheKey))
						{
							String [] cachekeyPrefixs = cacheKey.keyPrefix();
							int []   orders			  = cacheKey.order();
							for(int index = 0;index<cachekeyPrefixs.length;index++)
							{
								int order  = orders[index];
								String cachekeyPrefix = cachekeyPrefixs[index];
								if(keyPrefix.equals(cachekeyPrefix))
								{
									SortedMap<Integer,String> properties = keyPropertiesMap.get(keyPrefix);
									if(ValidationUtil.isEmpty(properties))
										properties = new TreeMap<>();
									properties.put(order,field.getName());
									keyPropertiesMap.put(keyPrefix,properties);
								}

							}
						}
					}
				}
			if(!ValidationUtil.isEmpty(keyPropertiesMap)
					&& keyPropertiesMap.keySet().size()==1
					&& poClass.getSimpleName().equals(keyPropertiesMap.keySet().iterator().next())
					&& ValidationUtil.isEmpty(keyPropertiesMap.get(poClass.getSimpleName())))
				//当实体有缓存注解而字段无注解时候，默认时候物理主键sequenceNbr作为key
				keyPropertiesMap.put(poClass.getSimpleName(),new TreeMap(){{put(1,CacheKey.DEFAULT_PROPERTY);}});

			if(!ValidationUtil.isEmpty(keyPropertiesMap))
				cacheOfkeyPropertiesMap.put(poClass.getName(),keyPropertiesMap);
		}
		return keyPropertiesMap;

	}






}

/*
*$Log: av-env.bat,v $
*/