package org.tycloud.core.auth.exception;

import org.tycloud.core.foundation.exception.BaseException;

/**
 * @Auther: Young-牛昌
 * @Date: 2018/11/1 20:53
 * @Description:
 */
public class LoginException  extends BaseException {


    public LoginException(String message) {
        super(message,LoginException.class.getSimpleName(),"权限认证失败.");
    }
}
