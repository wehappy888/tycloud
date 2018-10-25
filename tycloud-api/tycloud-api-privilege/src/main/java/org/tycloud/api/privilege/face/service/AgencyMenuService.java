package org.tycloud.api.privilege.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;
import org.tycloud.api.privilege.face.model.AgencyMenuModel;
import org.tycloud.api.privilege.face.orm.dao.AgencyMenuMapper;
import org.tycloud.api.privilege.face.orm.entity.AgencyMenu;
import org.tycloud.core.rdbms.service.BaseService;

/**
 * <p>
 * 角色与菜单关系表 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@Service
public class AgencyMenuService extends BaseService<AgencyMenuModel,AgencyMenu,AgencyMenuMapper> implements IService<AgencyMenu> {
	
}
