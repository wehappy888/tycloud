package org.tycloud.core.restful.exception.instance;

import org.tycloud.core.foundation.exception.BaseException;

/**
 * Created by yaohelang on 2018/5/31.
 */
public class TooManyRequests extends BaseException {

    public TooManyRequests(String message)
    {
        super(message,RequestForbidden.class.getSimpleName(),"请求太过频繁.");
        this.httpStatus = 429;
    }

    public TooManyRequests(String message,String errorCode)
    {
        super(message,errorCode,"请求太过频繁.");
        this.httpStatus = 429;
    }
}
