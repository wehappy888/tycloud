package org.tycloud.core.auth.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.exception.LoginException;
import org.tycloud.core.auth.face.model.AuthModel;
import org.tycloud.core.auth.face.model.IdPasswordAuthModel;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.auth.face.service.LoginInfoService;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.foundation.utils.Encrypt;
import org.tycloud.core.foundation.utils.ValidationUtil;

@Component("idPasswordAuthenticator")
public class IdPasswordAuthenticator implements AuthenticatorHandler {

    private static final Logger logger = LoggerFactory.getLogger(IdPasswordAuthenticator.class);


    @Autowired
    private LoginInfoService loginInfoService;


    public LoginInfoModel doAuthenticate(IdType idType, UserType userType, AuthModel authModel) throws Exception {

        IdPasswordAuthModel idPasswordAuthModel = (IdPasswordAuthModel) authModel;
        String loginId = idPasswordAuthModel.getLoginId();
        String password = idPasswordAuthModel.getPassword();

        if (ValidationUtil.isEmpty(idType)
                || ValidationUtil.isEmpty(userType)
                || ValidationUtil.isEmpty(loginId)
                || ValidationUtil.isEmpty(password)) {
            throw new AuthException("登录信息不能为空。");
        }

        //modify by river at 2018-10-24 , 修改登录用户通过 login 和 userType 来判断用户，方式
        //机构用户和 管理用户串号
        LoginInfoModel loginInfoModel = this.loginInfoService.selectByLoginIdAndUserType(loginId,userType.toString());
        if(ValidationUtil.isEmpty(loginInfoModel)){
            throw new AuthException("登陆的用户信息不存在");
        }

        if (PropertyValueConstants.LOCK_STATUS_LOCK.equals(loginInfoModel.getLockStatus())) {
            throw new AuthException("当前账号已经禁用,请联系管理员");
        } else if (ValidationUtil.isEmpty(loginInfoModel)
                || !loginInfoModel.getPassword().equals(Encrypt.md5ForAuth(password, loginInfoModel.getSalt()))) {
            throw new AuthException("用户名或密码有误");
        }
        return loginInfoModel;

    }
}
