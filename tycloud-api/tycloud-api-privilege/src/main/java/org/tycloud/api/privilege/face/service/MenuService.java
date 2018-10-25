package org.tycloud.api.privilege.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tycloud.api.privilege.face.model.MenuModel;
import org.tycloud.api.privilege.face.orm.dao.MenuMapper;
import org.tycloud.api.privilege.face.orm.entity.Menu;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.StringUtil;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@Service
public class MenuService extends BaseService<MenuModel,Menu,MenuMapper> implements IService<Menu> {




    @Autowired
    MenuMapper menuMapper;

    public MenuModel createMenu(MenuModel model) throws Exception
    {
        return this.createWithModel(model);
    }

    @Transactional
    public MenuModel createMenuList(MenuModel model) throws Exception
    {
        return this.createMenu(model);
    }

    public MenuModel updateMenu(MenuModel menuModel)throws Exception
    {

        MenuModel oldModel = Bean.toModel(menuMapper.selectById(menuModel.getSequenceNbr()),new MenuModel());
        if(!ValidationUtil.isEmpty(oldModel) && RequestContext.getAgencyCode().equals(oldModel.getAgencyCode()))
        {
            oldModel = Bean.copyExistPropertis(menuModel,oldModel);
        }else{
            throw new Exception("数据不存在");
        }
        this.updateWithModel(oldModel);
        return oldModel;
    }



    public String  deleteMenu(String ids) throws Exception
    {

        String [] deleteIds = StringUtil.string2Array(ids);

        for(String id:deleteIds)
        {
            Menu menu = this.menuMapper.selectById(id);
            if(!ValidationUtil.isEmpty(menu))
            {
                List<MenuModel> childs = this.queryForList(null,null,id,null);
                if(ValidationUtil.isEmpty(childs))
                {
                    this.deleteById(id);
                }else{
                    throw new Exception("请先删除其子节点[menuName:"+menu.getMenuName()+"].");
                }
            }else{
                throw new Exception("data not found.");
            }

        }
        return ids;
    }



    public List<MenuModel> queryForList(String menuName,String agencyCode,String parentId,String menuType) throws Exception
    {
        return this.queryForList(null,false,
                 menuName, agencyCode, parentId, menuType);
    }









}
