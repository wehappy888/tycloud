package org.tycloud.component.snapshot;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tycloud.component.snapshot.face.service.SnapshotGroupService;
import org.tycloud.component.snapshot.group.EnableBusinessCode;
import org.tycloud.component.snapshot.group.EnableSnapshotGroup;
import org.tycloud.component.snapshot.group.EnableSnapshotKey;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.RdbmsConstants;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


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
 *  Description:快照拦截器,如果实体开启了快照，则对实体进行快照
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
@Order(value = 100)
@Component
public class SnapshotInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(SnapshotInterceptor.class);



	@Autowired
	private SnapshotGroupService snapshotGroupService;

	
	@Pointcut(" execution(* com.baomidou.mybatisplus.mapper.BaseMapper.insert(..)) " +
			" || execution(* com.baomidou.mybatisplus.mapper.BaseMapper.updateById(..)) ")
	public void snapshotCheck(){}
	
	@Around("snapshotCheck()")
	public Object checkSnapshot(ProceedingJoinPoint pjp) throws Throwable {
 		Object retVal = null;
		try {
			//正常的insert，update方法
			retVal = pjp.proceed();

			
			//检查entity是否需要快照
		    Object[] params = pjp.getArgs();
			pjp.getSignature().getDeclaringType();
		    Object currentEntity = !ValidationUtil.isEmpty(params) ? params[0] : null;
		    
		    //如果entity上有开启快照的注解，则对entity进行快照
		    if(!ValidationUtil.isEmpty(currentEntity) && currentEntity instanceof BaseEntity){
		    	EnableSnapshot enableSnapshot = currentEntity.getClass().getAnnotation(EnableSnapshot.class);
		    	if(!ValidationUtil.isEmpty(enableSnapshot)){
		    		//保存快照
					snapshotGroupService.saveSnapshot( (BaseEntity) currentEntity, enableSnapshot.snapshotClass(),enableSnapshot.mapper());
		    	}
		    	this.saveSnapshotGroup(currentEntity,pjp.getSignature().toShortString());//保存快照组信息
		    }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return retVal;
	}


	/**
	 * 保存快照组信息
	 * @param currentEntity
	 * @throws Exception
	 */
	private void saveSnapshotGroup(Object currentEntity,String  execution) throws Exception
	{
		EnableSnapshotGroup enableSnapshotGroup  = currentEntity.getClass().getAnnotation(EnableSnapshotGroup.class);
		if(!ValidationUtil.isEmpty(enableSnapshotGroup) && enableSnapshotGroup.enableSave() && execution.contains(RdbmsConstants.baseMapperInsert))
		{
			//保存快照分组列表
			String businessCode 										= null;
			Map<String ,EnableSnapshotKey> mainObjKeyAnotations 	= new HashMap<>();
			Map<String ,Object> mainObjKeyValues 	= new HashMap<>();
			for(Field field : currentEntity.getClass().getDeclaredFields())
			{
				EnableBusinessCode enableBusinessCode = field.getAnnotation(EnableBusinessCode.class);
				EnableSnapshotKey enableSnapshotKey   = field.getAnnotation(EnableSnapshotKey.class);
				if(!ValidationUtil.isEmpty(enableBusinessCode))
				{
					businessCode 		= (String)Bean.getPropertyValue(field.getName(),currentEntity);
				}

				if(!ValidationUtil.isEmpty(enableSnapshotKey))
				{
					Object keyValue  	= Bean.getPropertyValue(field.getName(),currentEntity);

					mainObjKeyAnotations.put(field.getName(),enableSnapshotKey);
					mainObjKeyValues.put(field.getName(),keyValue);
				}


			}
			this.snapshotGroupService.saveSnapshotGroup(businessCode,enableSnapshotGroup.groupCode(),enableSnapshotGroup.groupName(),
					mainObjKeyAnotations,mainObjKeyValues);
		}

	}





}

/*
*$Log: av-env.bat,v $
*/