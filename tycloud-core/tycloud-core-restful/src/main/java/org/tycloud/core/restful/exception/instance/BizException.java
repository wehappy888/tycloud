package org.tycloud.core.restful.exception.instance;

/**
 * @author river
 * @date  2018-10-13.
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    public int getCode(){
        return code;
    }

    public BizException(int code, String message)
    {
        super(message);
        this.code = code;
    }

    public static void throwException(int code, String message) throws BizException {
        throw new BizException(code, message);
    }
}
