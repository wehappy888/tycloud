package org.tycloud.core.foundation.exception;

/**
 * Created by 子杨 on 2017/4/20.
 */
public class BaseException extends Exception{

    protected int httpStatus;
    protected String errorCode;
    protected String devMessage;

    public BaseException(String message, String errorCode,String devMessage)
    {
        super(message);
        this.errorCode = errorCode;
        this.devMessage = devMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDevMessage()
    {
        return devMessage;
    }


}
