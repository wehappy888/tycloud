package org.tycloud.api.privilege.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tycloud.api.privilege.face.model.MenuModel;
import org.tycloud.api.privilege.face.model.RoleModel;
import org.tycloud.api.privilege.face.model.UserRoleModel;
import org.tycloud.api.privilege.face.orm.dao.MenuMapper;
import org.tycloud.api.privilege.face.orm.dao.RoleMapper;
import org.tycloud.api.privilege.face.orm.dao.UserRoleMapper;
import org.tycloud.api.privilege.face.orm.entity.Menu;
import org.tycloud.api.privilege.face.orm.entity.Role;
import org.tycloud.api.privilege.face.orm.entity.UserRole;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@Service
public class UserRoleService extends BaseService<UserRoleModel,UserRole,UserRoleMapper> implements IService<UserRole> {



    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuMapper menuMapper;

    public List<RoleModel> selectRoleByUser(String userId, String agencyCode) throws Exception
    {
        List<RoleModel> returnList = new ArrayList<>();
        List<Role> roleList = roleMapper.selectByUser(userId,agencyCode);
        if(!ValidationUtil.isEmpty(roleList))
        {
            returnList = Bean.toModels(roleList,RoleModel.class);
        }
        return returnList;

    }



    public List<MenuModel> selectMenuByUser(String userId, String agencyCode)throws Exception
    {

        List<MenuModel> returnList = new ArrayList<>();
        List<Menu> menus = menuMapper.selectByUser(userId,agencyCode);

        if(!ValidationUtil.isEmpty(menus))
        {
            returnList = Bean.toModels(menus,MenuModel.class);
        }
        return returnList;
    }






}
