package org.tycloud.component.snapshot.face.service;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tycloud.component.snapshot.BaseSnapshot;
import org.tycloud.component.snapshot.face.model.SnapshotGroupModel;
import org.tycloud.component.snapshot.face.orm.dao.SnapshotGroupMapper;
import org.tycloud.component.snapshot.face.orm.entity.SnapshotGroup;
import org.tycloud.component.snapshot.group.EnableSnapshotKey;
import org.tycloud.core.foundation.context.SpringContextHelper;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.model.BaseModel;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;
import org.tycloud.core.rdbms.service.BaseService;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * 快照分组信息 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-25
 */
@Service
public class SnapshotGroupService extends BaseService<SnapshotGroupModel,SnapshotGroup,SnapshotGroupMapper> implements IService<SnapshotGroup> {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotGroupService.class);



    @Autowired
    SnapshotGroupMapper snapshotGroupMapper;


    private static Map   mapperMap  = new HashMap();
    private static Map   mapperMethodMap  = new HashMap();



    public List<SnapshotGroupModel> queryForList(String groupCode,String businessCode,String snapshotType) throws Exception
    {

        List<SnapshotGroupModel> returnList = new ArrayList<>();
        Map<String,Object> params = this.assemblyMapParams(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                this.getClass(),
                groupCode,businessCode,snapshotType);


        List<SnapshotGroup> eneitis = this.selectByMap(params);
        if(!ValidationUtil.isEmpty(eneitis))
        {
            returnList = Bean.toModels(eneitis,SnapshotGroupModel.class);
        }
        return returnList;
    }




    public <M extends BaseModel> M assembleModel(String gourpCode ,String businessCode, Class<? extends BaseSnapshot> snapshotClass, String snapshotType) throws Exception
    {
        M m = null;
        List list = this.queryForList(gourpCode, businessCode, null);
        Map<String ,SnapshotGroup> map = new HashMap<String,SnapshotGroup>();
        if(!ValidationUtil.isEmpty(list))
        {
            map = Bean.listToMap(list, "snapshotType", SnapshotGroupModel.class);
        }
        else
        {
            throw new Exception("空的快照分组实例.");
        }
        for(String type : map.keySet())
        {
            SnapshotGroup snapshotGroup  = map.get(type);
            if(!ValidationUtil.isEmpty(snapshotGroup))
            {
                BaseSnapshot baseSnapshot = this.getSnptEntity(snapshotGroup.getSnapshotSequenceNbr(),snapshotClass);
                m = (M)Bean.copyExistPropertis(baseSnapshot, m.getClass().newInstance());
            }
        }
        return m;
    }












    public <T extends BaseEntity> void saveSnapshot(T mainObject,
                                                    Class<? extends BaseSnapshot> snapshotEntityClass,
                                                    Class<?extends BaseMapper> mapperClass) throws Exception {
        BaseSnapshot snapshotBean = Bean.copyExistPropertis(mainObject, snapshotEntityClass.newInstance());
        snapshotBean.setMasterRecDate( mainObject.getRecDate());
        snapshotBean.setMasterSequenceNbr(mainObject.getSequenceNbr());
        Bean.setPropertyValue("recDate", new Date(), snapshotBean);
        snapshotBean.setSequenceNbr(null);

        BaseMapper baseMapper = (BaseMapper)SpringContextHelper.getBean(mapperClass);
        baseMapper.insert(snapshotBean);
    }


    /**
     * 根据主对象的主键获得其最新快照对象的主键
     *
     * @param mainTypeSequenceNbr
     * @return
     * @throws Exception
     */
    public <T extends BaseSnapshot> Long  getLastSnptSequenceNbr(Long  mainTypeSequenceNbr, Class<T> snapshotEntityClass) throws Exception {
        Long snptSequenceNBR = null;
        T newSnapshot = this.getLastSnptEntity(mainTypeSequenceNbr, snapshotEntityClass);
        if (!ValidationUtil.isEmpty(newSnapshot) && BaseSnapshot.class.isAssignableFrom(snapshotEntityClass)) {
            snptSequenceNBR =  newSnapshot.getSequenceNbr();
        }
        return snptSequenceNBR;
    }


    /**
     * 根据主对象的主键获得其最新快照对象
     *
     * @param mainTypeSequenceNbr
     * @return
     * @throws Exception
     */
    public <T extends BaseSnapshot> T getLastSnptEntity(Long  mainTypeSequenceNbr, Class<T> snapshotEntityClass) throws Exception {

        T returnObj = null;
        String tableName = snapshotEntityClass.getAnnotation(TableName.class).value();

        List<Map<String,Object>>  snptList = this.snapshotGroupMapper.getLastSnptEntity(tableName,mainTypeSequenceNbr);

        if(!ValidationUtil.isEmpty(snptList))
        {
            Map<String,Object> resultMap = snptList.get(0);
            Map<String,Object> newMap    = new HashMap();
            for(String clunmKey : resultMap.keySet())
            {
                newMap.put(Bean.columToProperty(clunmKey),resultMap.get(clunmKey));
            }

            returnObj = (T)Bean.mapToBean(newMap,snapshotEntityClass);

        }
        return returnObj;
    }


    /**
     * 根据快照主键和快照实体获得快照记录
     * @param snptSequenceNbr
     * @param snapshotEntityClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T extends BaseSnapshot> T getSnptEntity(String  snptSequenceNbr, Class<T> snapshotEntityClass) throws Exception {
        String tableName = snapshotEntityClass.getAnnotation(TableName.class).value();

        Map result = snapshotGroupMapper.getSnptEntity(tableName,snptSequenceNbr);

        return null;
    }


    /**
     * 保存快照分组信息
     * @param businessCode 快照组业务主键
     * @param groupCode     快照组名称
     * @param groupName     快照组编号
     * @param mainObjKeyAnotations
     * @param mainObjKeyValues
     * @return
     * @throws Exception
     */
    public  List<SnapshotGroupModel> saveSnapshotGroup(String businessCode, String groupCode, String groupName,
                                                       Map<String ,EnableSnapshotKey> mainObjKeyAnotations, Map<String ,Object> mainObjKeyValues ) throws Exception
    {
        Long startTime = System.currentTimeMillis();
        List<SnapshotGroupModel> returnList = new ArrayList();

        Map<String,Object> temp = new HashMap<>();

        for(String fiedName:mainObjKeyAnotations.keySet())
        {

            String methodName = mainObjKeyAnotations.get(fiedName).mepperMethod();


            BaseService baseService = (BaseService)mapperMap.get(mainObjKeyAnotations.get(fiedName).mainService());
            if(ValidationUtil.isEmpty(baseService))
            {
                baseService = (BaseService)SpringContextHelper.getBean(mainObjKeyAnotations.get(fiedName).mainService());
                mapperMap.put(mainObjKeyAnotations.get(fiedName).mainService(),baseService);
            }

            Class snapshotClass = mainObjKeyAnotations.get(fiedName).snapshotEntity();

            Method method = (Method)mapperMethodMap.get(mainObjKeyValues.get(fiedName).getClass().getSimpleName()+methodName+baseService.getClass().getSimpleName());
            if(ValidationUtil.isEmpty(method))
            {
                method =  baseService.getClass().getMethod(methodName,mainObjKeyValues.get(fiedName).getClass());
                mapperMethodMap.put(mainObjKeyValues.get(fiedName).getClass().getSimpleName()+methodName+baseService.getClass().getSimpleName(),method);
            }
            BaseEntity baseEntity = (BaseEntity)method.invoke(baseService,mainObjKeyValues.get(fiedName));
            Long mainSequenceNbr = baseEntity.getSequenceNbr();


            Long lastSnapshotSequenceNbr = this.getLastSnptSequenceNbr(mainSequenceNbr, snapshotClass);


            if(ValidationUtil.isEmpty(temp.get(lastSnapshotSequenceNbr+baseEntity.getClass().getSimpleName())))
            {
                SnapshotGroupModel snapshotGroup = new SnapshotGroupModel();
                snapshotGroup.setBusinessCode(businessCode);
                snapshotGroup.setGroupCode(groupCode);
                snapshotGroup.setGroupName(groupName);
                snapshotGroup.setNullable(String.valueOf(false));
                snapshotGroup.setSnapshotSequenceNbr(lastSnapshotSequenceNbr);
                snapshotGroup.setSnapshotType(baseEntity.getClass().getSimpleName());
                this.createWithModel(snapshotGroup);
                temp.put(lastSnapshotSequenceNbr+baseEntity.getClass().getSimpleName(),snapshotGroup);
            }

        }
        return returnList;
    }









}
