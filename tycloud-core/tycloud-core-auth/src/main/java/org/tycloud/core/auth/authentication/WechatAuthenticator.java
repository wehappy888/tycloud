package org.tycloud.core.auth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.face.model.AuthModel;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.auth.face.model.WechatInfoModel;
import org.tycloud.core.auth.face.service.WechatInfoService;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.foundation.utils.ValidationUtil;

/**
 * @Auther: Young-牛昌
 * @Date: 2018/10/1 10:54
 * @Description:
 */
@Component("wechatAuthenticator")
public class WechatAuthenticator implements AuthenticatorHandler {

    @Autowired
    private WechatInfoService wechatInfoService;

    @Override
    public LoginInfoModel doAuthenticate(IdType idType, UserType userType, AuthModel authModel) throws Exception {
        if(ValidationUtil.isEmpty(idType)
                ||ValidationUtil.isEmpty(userType)
                ||ValidationUtil.isEmpty(authModel.getLoginId()))
        {
            throw new AuthException("登录信息不能为空.");
        }
        WechatInfoModel model =  wechatInfoService.findByOpendId(authModel.getLoginId());

        LoginInfoModel longInfoModel = new LoginInfoModel();
        longInfoModel.setAgencyCode(model.getAgencyCode());
        longInfoModel.setUserId(model.getUserId());
        longInfoModel.setOpenId(model.getPubOpenId());
        longInfoModel.setUserType(UserType.PUBLIC_USER.name());
        longInfoModel.setLoginId(authModel.getLoginId());
        return longInfoModel;
    }


}
