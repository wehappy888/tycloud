package org.tycloud.core.restful.exception.instance;

import org.tycloud.core.foundation.exception.BaseException;

/**
 * Created by magintursh on 2017-07-05.
 */
public class BadRequest extends BaseException {

    public BadRequest(String message)
    {
        super(message,BadRequest.class.getSimpleName(),"");
        this.httpStatus = 400;
    }

}
