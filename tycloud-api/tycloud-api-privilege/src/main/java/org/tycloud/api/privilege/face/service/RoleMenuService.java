package org.tycloud.api.privilege.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tycloud.api.privilege.face.model.MenuModel;
import org.tycloud.api.privilege.face.model.RoleMenuModel;
import org.tycloud.api.privilege.face.orm.dao.MenuMapper;
import org.tycloud.api.privilege.face.orm.dao.RoleMapper;
import org.tycloud.api.privilege.face.orm.dao.RoleMenuMapper;
import org.tycloud.api.privilege.face.orm.entity.Menu;
import org.tycloud.api.privilege.face.orm.entity.Role;
import org.tycloud.api.privilege.face.orm.entity.RoleMenu;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色与菜单关系表 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@Service
public class RoleMenuService extends BaseService<RoleMenuModel,RoleMenu,RoleMenuMapper> implements IService<RoleMenu> {



    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    MenuMapper menuMapper;


    @Autowired
    RoleMapper roleMapper;

    public List<MenuModel> selectByRole(String roleSequenceNbr, String agencyCode ) throws Exception
    {

        List<MenuModel> returnModels = new ArrayList<>();

        List<Menu> menus = menuMapper.selectByRole(roleSequenceNbr,agencyCode);

        if(!ValidationUtil.isEmpty(menus))
        {
            returnModels = Bean.toModels(menus,MenuModel.class);
        }
        return returnModels;
    }


    /**
     * 更新角色菜单权限
     */
    public  List<RoleMenuModel> updateByRole(String [] menuIds,String roleSequenceNbr,String agencyCode)  throws  Exception
    {

        List<RoleMenuModel> returnList = new ArrayList<>();

        //验证角色信息
        Role role                       = roleMapper.selectById(roleSequenceNbr);
        if(!ValidationUtil.isEmpty(role) && PropertyValueConstants.LOCK_STATUS_UNLOCK.equals(role.getLockStatus()))
        {
            //删除已有菜单角色
            Map<String,Object> params   = this.assemblyMapParams(
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    this.getClass(),
                    roleSequenceNbr,agencyCode);
            this.deleteByMap(params);

            //保存新的菜单角色
            for(String menuId : menuIds)
            {
                Menu menu = this.menuMapper.selectById(menuId);
                if(!ValidationUtil.isEmpty(menu) && menu.getAgencyCode().equals(agencyCode))
                {
                    returnList.add(this.createRoleMenu(roleSequenceNbr,menuId,agencyCode));
                }else{
                    throw new  Exception("菜单权限信息不存在.");
                }

            }
        }else{
            throw new  Exception("角色信息异常.");
        }
        return returnList;
    }

    public RoleMenuModel createRoleMenu(String  roleSequenceNbr ,String menuSequenceNbr,String agencyCode) throws Exception
    {

        RoleMenuModel newRoleMenuModel = new RoleMenuModel();
        newRoleMenuModel.setAgencyCode(agencyCode);
        newRoleMenuModel.setCreateTime(new Date());
        newRoleMenuModel.setCreateUserId(RequestContext.getExeUserId());
        newRoleMenuModel.setMenuSequenceNbr(menuSequenceNbr);
        newRoleMenuModel.setRoleSequenceNbr(roleSequenceNbr);
        return  this.createWithModel(newRoleMenuModel);

    }



}
