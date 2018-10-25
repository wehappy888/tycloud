package org.tycloud.core.auth.face.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;
import org.tycloud.core.auth.face.model.LoginHistoryModel;
import org.tycloud.core.auth.face.orm.dao.LoginHistoryMapper;
import org.tycloud.core.auth.face.orm.entity.LoginHistory;

import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

/**
 * <p>
 * 用户登录记录 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-07-06
 */
@Service
public class LoginHistoryService extends BaseService<LoginHistoryModel,LoginHistory, LoginHistoryMapper>implements IService<LoginHistory> {




    public LoginHistoryModel createLoginHistory(LoginHistoryModel model) throws Exception
    {
        this.createWithModel(model);
        return model;
    }


    // 获取最后一个记录
    public LoginHistoryModel queryLastHistr(Page page,String userId, String loginId) throws Exception
    {
        LoginHistoryModel  model = null;
        page = new Page(0,1);
        page = this.queryForPage(page,"SESSION_CREATE_TIME",false,userId,loginId);

        if(!ValidationUtil.isEmpty(page.getRecords()))
        {
            model = (LoginHistoryModel)page.getRecords().get(0);
        }
        return model;
    }


	
}
