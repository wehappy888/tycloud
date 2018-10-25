package org.tycloud.api.privilege.face.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;
import org.tycloud.api.privilege.face.model.RoleModel;
import org.tycloud.api.privilege.face.orm.dao.RoleMapper;
import org.tycloud.api.privilege.face.orm.entity.Role;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.annotation.Operate;
import org.tycloud.core.rdbms.annotation.Operator;
import org.tycloud.core.rdbms.service.BaseService;
import org.tycloud.core.restful.exception.instance.BadRequest;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@Service
public class RoleService extends BaseService<RoleModel,Role,RoleMapper> implements IService<Role> {





    public RoleModel createRole(RoleModel model)throws Exception
    {
        return     this.createWithModel(model);
    }

    public RoleModel updateRole(RoleModel roleModel)throws Exception
    {

        RoleModel oldModel = this.queryBySeq(roleModel.getSequenceNbr());
        if(!ValidationUtil.isEmpty(oldModel) && RequestContext.getAgencyCode().equals(oldModel.getAgencyCode()))
        {
            oldModel = Bean.copyExistPropertis(roleModel,oldModel);
        }else{
            throw new BadRequest("数据不存在");
        }
        this.updateWithModel(oldModel);
        return oldModel;
    }


    public boolean deleteRole(Long seq)throws Exception

    {
        return this.deleteBySeq(seq);
    }



    public Page<RoleModel> qeuryByName(Page page, @Operate(value = Operator.LIKE) String roleName ,
                                               String agencyCode) throws Exception
    {
       return this.queryForPage(page,null,false,roleName,agencyCode);
    }



    public List<RoleModel> selectByAgency(String agencyCode) throws Exception
    {
        return this.queryForList(null,false,agencyCode);
    }



    public RoleModel updateRoleLockStatus(Long  sequenceNbr) throws Exception
    {
        RoleModel role = this.queryBySeq(sequenceNbr);
        if(!ValidationUtil.isEmpty(role))
        {
            if(PropertyValueConstants.LOCK_STATUS_UNLOCK.equals(role.getLockStatus()))
            {
                role.setLockStatus(PropertyValueConstants.LOCK_STATUS_LOCK);
            }

            if(PropertyValueConstants.LOCK_STATUS_LOCK.equals(role.getLockStatus()))
            {
                role.setLockStatus(PropertyValueConstants.LOCK_STATUS_UNLOCK);
            }
            role.setLockUserId(RequestContext.getExeUserId());
            role.setLockDate(new Date());
        }
        return Bean.toModel(role,new RoleModel());
    }





	
}
