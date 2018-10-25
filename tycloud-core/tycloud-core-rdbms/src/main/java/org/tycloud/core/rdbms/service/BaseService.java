package org.tycloud.core.rdbms.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.context.RequestContextEntityType;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.RdbmsConstants;
import org.tycloud.core.rdbms.annotation.Operate;
import org.tycloud.core.rdbms.annotation.Operator;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by 子杨 on 2017/4/28.
 */
public  class BaseService<V,P, M extends BaseMapper<P>> extends ServiceImpl<M,P> implements IService<P> {


    private Class<P> poClass = null;

    private Class<V> modelCalss = null;


    @SuppressWarnings("unchecked")
    public final   Class<P> getPoClass() {
        if (poClass == null) {
            poClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return poClass;
    }


    @SuppressWarnings("unchecked")
    public final   Class<V> getModelClass() {
        if (modelCalss == null) {
            modelCalss = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return modelCalss;
    }


    /**
     * 简化service层的创建操作,填充公有字段
     * @param model
     * @return
     * @throws Exception
     */
    public final   P prepareEntity(V model) throws Exception {
        P entity = Bean.toPo(model, this.getPoClass().newInstance());
        if (entity instanceof BaseEntity) {
            BaseEntity temp = (BaseEntity) entity;
            temp.setRecDate(new Date());
            temp.setRecStatus(RdbmsConstants.COMMON_ACTIVE);
            temp.setRecUserId(RequestContext.getExeUserId());// 有實際的用戶模塊以後 這裡填寫 當前用戶的ID
        }
        return entity;
    }



    private void setPoClassToContext()
    {
        RequestContext.setAttribute(RequestContextEntityType.poClassForCache,this.getPoClass());
        RequestContext.setAttribute(RequestContextEntityType.currentBaseService,this);
    }



    public final   V createWithModel(V model) throws Exception {
        setPoClassToContext();
        P entity = this.prepareEntity(model);
        this.insert(entity);
        return Bean.toModel(entity, model);
    }

    public final   V updateWithModel(V model) throws Exception {
        setPoClassToContext();
        P entity = this.prepareEntity(model);
        this.updateById(entity);
        return Bean.toModel(entity, model);
    }





    public final   boolean deleteBySeq(Long seq)throws Exception
    {
        setPoClassToContext();
        return this.deleteById(seq);
    }




    public final    V queryBySeq(Long  seq) throws Exception {
        setPoClassToContext();
        return Bean.toModel(this.selectById(seq), getModelClass().newInstance());
    }

    public final    List<V> queryBatchSeq(List<Long>  seqs) throws Exception {
        setPoClassToContext();
        return Bean.toModels(this.selectBatchIds(seqs), getModelClass());
    }

    public final   V queryByModel(V model) throws Exception
    {
        setPoClassToContext();
        P  entity = Bean.toPo(model,this.getPoClass().newInstance());
        return Bean.toModel(this.baseMapper.selectOne(entity),model);
    }

    /**
     * 简化service层操作，查询单个对象，参数顺序必须和前置方法一直，参数名称需要和对象的属性名称保持一样
     *
     * @param params
     * @return
     * @throws Exception
     */
    protected final    V queryModelByParams(Object... params) throws Exception
    {
        setPoClassToContext();
        P entity    = this.getPoClass().newInstance();
        Class clzz  = Bean.getClassByName(Thread.currentThread().getStackTrace()[2].getClassName());
        Method crurrntMethod                = Bean.getMethodByName(Thread.currentThread().getStackTrace()[2].getMethodName(),clzz);
        String[] parameterNames             = Bean.getMethodParameterNamesByAsm4(clzz, crurrntMethod);

        for(int i=0;i<params.length;i++)
        {
            if(!ValidationUtil.isEmpty(params[i]))
                Bean.setPropertyValue(parameterNames[i],params[i],entity);
        }
        return Bean.toModel(this.baseMapper.selectOne(entity),this.getModelClass().newInstance());
    }






    protected int queryCount(Object... params) throws Exception
    {
        Wrapper wrapper = this.assemblyWrapperParams(
                Thread.currentThread().getStackTrace()[2].getMethodName(),
                Bean.getClassByName(Thread.currentThread().getStackTrace()[2].getClassName()),
                params
        );
        return this.selectCount(wrapper);
    }

    /**
     * 简化service单表查询
     * 列表查询，
     * @param orderBy 排序字段
     * @param isAsc   排序规则
     * @param params 前置方法参数列表，參數順序保持一致，參數名稱需要和數據庫字段名稱相互對應符合轉換規則
     * @return
     * @throws Exception
     */
    protected final   List<V> queryForList(String orderBy,boolean isAsc,Object... params)throws Exception
    {
        Wrapper wrapper = this.assemblyWrapperParams(
                Thread.currentThread().getStackTrace()[2].getMethodName(),
                Bean.getClassByName(Thread.currentThread().getStackTrace()[2].getClassName()),
                params
        );
        if(!ValidationUtil.isEmpty(orderBy))
            wrapper.orderBy(orderBy,isAsc);
        return Bean.toModels(this.selectList(wrapper),this.getModelClass());
    }


    /**
     * 简化service单表查询
     * 分页查询，只能被子类调用
     * @param page  分页实体
     * @param orderBy 排序字段
     * @param isAsc 排序规则
     * @param params 前置方法参数列表，參數順序保持一致，參數名稱需要和數據庫字段名稱相互對應符合轉換規則
     * @return  分页实体
     * @throws Exception
     */
    protected final    Page queryForPage(Page page,String orderBy,boolean isAsc,Object... params) throws Exception
    {
        //获取调用当前方法的类，目前没想到更好的办法，就这样先用着
        Wrapper wrapper = this.assemblyWrapperParams(
                Thread.currentThread().getStackTrace()[2].getMethodName(),
                Bean.getClassByName(Thread.currentThread().getStackTrace()[2].getClassName()),
                params
        );
        if(!ValidationUtil.isEmpty(orderBy))
            wrapper.orderBy(orderBy,isAsc);
        page = this.selectPage(page,wrapper);
        if(!ValidationUtil.isEmpty(page.getRecords()))
        {
            page.setRecords(Bean.toModels(page.getRecords(),this.getModelClass()));
        }
        return page;
    }



    /**
     * 组装查询条件，过滤为空的参数,參數順序保持一致
     * 參數名稱需要和數據庫字段名稱相互對應符合轉換規則
     * @param methodName  前置类当中不可以有方法重载
     * @param clzz
     * @param params
     * @return Map<数据库字段,参数值>
     * @throws Exception
     */
    protected  final   Map<String,Object> assemblyMapParams(String methodName, Class clzz, Object... params) throws Exception
    {
        Map<String,Object> queryParamMap    = new HashMap();
        Method crurrntMethod                = Bean.getMethodByName(methodName,clzz);

        if(!ValidationUtil.isEmpty(crurrntMethod))
        {
            String[] parameterNames    = Bean.getMethodParameterNamesByAsm4(clzz, crurrntMethod);

            //將參數名稱轉換為字段名稱
            Map<String,String> cloMap  = Bean.propertyToColum(parameterNames);

            int offset                 = parameterNames.length-params.length;//参数位置偏移量
            //組裝map格式的查詢條件
            for(int i=offset;i<parameterNames.length;i++)
            {
                if(!ValidationUtil.isEmpty(params[i-offset]))
                {
                    queryParamMap.put(
                            cloMap.get(parameterNames[i]),
                            params[i-offset]
                    );
                }
            }
        }else{

            throw new Exception("找不到指定的方法名:"+methodName);
        }
        return queryParamMap;
    }





    /**
     *
     * 根据方法参数组装查询条件，参数名称需要与对应实体的属性名称一致，
     * 过滤为空的参数,object数组元素需要与前置方法參數順序保持一致
     * 參數名稱需要和數據庫字段名稱相互對應符合轉換規則
     * @param methodName 前置方法名称，前置类当中不可以有方法重载
     * @param clzz       前置方法所在类
     * @param params     参数列表
     * @return Wrapper   返回mybatisplus的查询组装器
     * @throws Exception
     */
    protected  final Wrapper assemblyWrapperParams(String methodName, Class clzz, Object... params) throws Exception
    {

        EntityWrapper returnWrapper         =new EntityWrapper();
        returnWrapper.where("1=1");
        Method crurrntMethod                = Bean.getMethodByName(methodName,clzz);

        if(!ValidationUtil.isEmpty(crurrntMethod))
        {


            Annotation [] [] annotations    = crurrntMethod.getParameterAnnotations();

            //獲得方法的參數名稱列表
            String[] parameterNames         = Bean.getMethodParameterNamesByAsm4(clzz, crurrntMethod);

            //將參數名稱轉換為字段名稱
            Map<String,String> cloMap       = Bean.propertyToColum(parameterNames);

            int offset                      = parameterNames.length-params.length;//参数位置偏移量
            //組裝map格式的查詢條件
            for(int i=offset;i<parameterNames.length;i++)
            {
                if(!ValidationUtil.isEmpty(params[i-offset]))
                {
                    if(!ValidationUtil.isEmpty(annotations[i]) && annotations[i][0] instanceof Operate)
                    {
                        Operate operator = (Operate)annotations[i][0];
                        returnWrapper = this.paresWrapper(returnWrapper,operator.value(),cloMap.get(parameterNames[i]),params[i-offset]);
                    }else
                    {
                        //默认AND操作符
                        returnWrapper = this.paresWrapper(returnWrapper,Operator.AND,cloMap.get(parameterNames[i]),params[i-offset]);
                    }
                }
            }
        }else{
            throw new Exception("找不到指定的方法名:"+methodName);
        }

        return returnWrapper;
    }


    private EntityWrapper paresWrapper(EntityWrapper wrapper,Operator operator,String cloum,Object param)throws Exception
    {
        switch (operator)
        {
            case AND:
                wrapper.and(cloum+"={0}",String.valueOf(param));
                break;
            case LIKE:
                wrapper.like(cloum,String.valueOf(param));
                break;
            case IN:
                if(param instanceof  Collection)
                {
                    wrapper.in(cloum,(Collection) param);
                }else
                {
                    throw new Exception("can not be case to Collection");
                }
                break;
            case BETWEEN:
                if(param instanceof Object[] && ((Object[])param).length == 2)
                {
                    Object[] objArray = (Object[])param;
                    wrapper.between(cloum,objArray[0],objArray[1]);
                }else{
                    throw new Exception("can not be case to Object Array");
                }
                break;
            case NOTEQUAL:
                wrapper.and(cloum+"!={0}",String.valueOf(param));
                break;
            default:
                throw new Exception("not support operator: "+operator.name());
        }
        return wrapper;
    }

}
