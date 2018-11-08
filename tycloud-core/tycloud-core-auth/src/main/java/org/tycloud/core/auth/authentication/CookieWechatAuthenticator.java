package org.tycloud.core.auth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.tycloud.component.cache.Redis;
import org.tycloud.component.cache.enumeration.CacheType;
import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.exception.LoginException;
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
@Component("cookieWechatAuthenticator")
public class CookieWechatAuthenticator implements AuthenticatorHandler {

    @Autowired
    private WechatInfoService wechatInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String OPEN_ID_TOKEN = "OPEN_ID_TOKEN";

    @Override
    public LoginInfoModel doAuthenticate(IdType idType, UserType userType, AuthModel authModel) throws Exception {

        if(ValidationUtil.isEmpty(idType)
                ||ValidationUtil.isEmpty(userType)
                ||ValidationUtil.isEmpty(authModel.getCookieKey()))
        {
            throw new AuthException("登录信息不能为空。");
        }

        WechatInfoModel model  = (WechatInfoModel) redisTemplate.opsForValue().get(authModel.getCookieKey());
        if(ObjectUtils.isEmpty(model)){
            redisTemplate.delete(authModel.getCookieKey());
            throw new LoginException("朋来登录已失效。");
        }
        LoginInfoModel longInfoModel = new LoginInfoModel();
        longInfoModel.setAgencyCode(model.getAgencyCode());
        longInfoModel.setUserId(model.getUserId());
        longInfoModel.setOpenId(model.getPubOpenId());
        longInfoModel.setUserType(UserType.PUBLIC_USER.name());
        longInfoModel.setLoginId(model.getPubOpenId());
        longInfoModel.setCookie(authModel.getCookieKey());
        return longInfoModel;
    }

}
