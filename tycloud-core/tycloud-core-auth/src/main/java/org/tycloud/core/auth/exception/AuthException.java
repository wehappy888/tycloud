package org.tycloud.core.auth.exception;

import org.tycloud.core.foundation.exception.BaseException;

public class AuthException  extends BaseException{

    public AuthException(String message)
    {
        super(message,AuthException.class.getSimpleName(),"权限认证失败.");
        this.httpStatus = 401;
    }
}
