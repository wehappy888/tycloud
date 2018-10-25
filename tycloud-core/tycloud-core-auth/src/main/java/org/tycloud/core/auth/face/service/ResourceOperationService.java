package org.tycloud.core.auth.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;
import org.tycloud.core.auth.face.model.ResourceOperationModel;
import org.tycloud.core.auth.face.orm.dao.ResourceOperationMapper;
import org.tycloud.core.auth.face.orm.entity.ResourceOperation;
import org.tycloud.core.rdbms.service.BaseService;

/**
 * <p>
 * 资源操作表 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-08-17
 */
@Service
public class ResourceOperationService extends BaseService<ResourceOperationModel,ResourceOperation,ResourceOperationMapper> implements IService<ResourceOperation> {
	
}
