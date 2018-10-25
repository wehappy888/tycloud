package org.tycloud.core.restful.auth;

import org.springframework.web.method.HandlerMethod;
import org.tycloud.core.auth.face.model.SsoSessionsModel;

/**
 * Created by yaohelang on 2018/6/27.
 */
public interface AuthWithSessionHandler {


    /**
     * 获取session之后验证
     * @param ssoSessionsModel
     * @param handlerMethod
     * @param token
     * @param appKey
     * @param product
     */
    void doAuth(SsoSessionsModel ssoSessionsModel, HandlerMethod handlerMethod,
                String token, String appKey, String product) throws Exception;




    /**
     * 执行顺序
     * @return
     */
    int  order();

}
