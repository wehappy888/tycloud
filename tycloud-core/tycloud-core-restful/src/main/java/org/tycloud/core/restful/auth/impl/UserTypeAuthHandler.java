package org.tycloud.core.restful.auth.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.face.model.SsoSessionsModel;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.restful.auth.AuthWithSessionHandler;
import org.tycloud.core.restful.doc.TycloudOperation;
import org.tycloud.core.restful.interceptor.AuthInterceptor;

/**
 * Created by yaohelang on 2018/6/27.
 */
@Component
public class UserTypeAuthHandler implements AuthWithSessionHandler ,InitializingBean {



    public void afterPropertiesSet() throws Exception
    {
        AuthInterceptor.addAuthWithSessionHandler(this);
    }


    /**
     * 获取session之后验证
     * @param ssoSessionsModel
     * @param handlerMethod
     * @param token
     * @param appKey
     * @param product
     */
    public void doAuth(SsoSessionsModel ssoSessionsModel, HandlerMethod handlerMethod,
                String token, String appKey, String product) throws Exception
    {
        TycloudOperation tycloudOperation = handlerMethod.getMethodAnnotation(TycloudOperation.class);
        UserType userType = UserType.getUserType(ssoSessionsModel.getUserType());
        if(userType.getValue() < tycloudOperation.ApiLevel().getValue())
        {
            throw new AuthException("用户权限不够.");
        }
    }




    /**
     * 执行顺序
     * @return
     */
    public int  order()
    {
        return 2;
    }

}

