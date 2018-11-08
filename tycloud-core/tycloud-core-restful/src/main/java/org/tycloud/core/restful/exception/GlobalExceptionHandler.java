package org.tycloud.core.restful.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.restful.exception.instance.BadRequest;
import org.tycloud.core.restful.exception.instance.BizException;
import org.tycloud.core.restful.utils.ResponseHelper;
import org.tycloud.core.restful.utils.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Created by magintursh on 2017-07-05.
 * @modify river on 2018-10-13
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);


    /**
     * @param req
     * @param response
     * @param e
     * @return
     * @author river
     */
    @ExceptionHandler(value = BadRequest.class)
    @ResponseBody
    public ResponseModel<String> badRequestException(HttpServletRequest req, HttpServletResponse response, Exception e) {
        logger.info("in exception handler -- BadRequest ");
        BadRequest badRequest = (BadRequest) e;
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setUrl(req.getRequestURL().toString());
        response.setStatus(HttpStatus.OK.value());
        responseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseModel.setDevMessage(badRequest.getDevMessage());
        responseModel.setMessage(e.getMessage());
        return responseModel;
    }

    /**
     * @param req
     * @param response
     * @param e
     * @return
     * @author river
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseModel<String> bizException(HttpServletRequest req, HttpServletResponse response, Exception e) {
        logger.info("in exception handler -- bizException ");
        response.setStatus(HttpStatus.OK.value());
        BizException bizException = (BizException) e;
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setStatus(bizException.getCode());
        responseModel.setDevMessage("异常信息");
        responseModel.setMessage(bizException.getMessage());
        return responseModel;
    }

    /**
     * @param req
     * @param response
     * @param e
     * @return
     * @author river
     */
    @ExceptionHandler(value = AuthException.class)
    @ResponseBody
    public ResponseModel<String> authException(HttpServletRequest req, HttpServletResponse response, Exception e) {
        logger.info("in exception handler -- AuthException ");
        AuthException authException = (AuthException) e;
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setUrl(req.getRequestURL().toString());
        response.setStatus(HttpStatus.OK.value());
        responseModel.setStatus(authException.getHttpStatus());
        responseModel.setDevMessage(authException.getMessage());
        responseModel.setMessage(authException.getMessage());
        return responseModel;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseModel<String> defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        logger.info("in exception handler - defaultErrorHandler");
        logger.error(e.getMessage(), e);
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setUrl(req.getRequestURL().toString());
        responseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseModel.setDevMessage(e.getMessage());
        responseModel.setMessage("未知错误");
        logger.error(e.getMessage(), e);
        //http返回码永远都是200，真是的返回码见responseBody中的status字段
        response.setStatus(HttpStatus.OK.value());
        return responseModel;
    }

    /**
     * @param req
     * @param response
     * @param e
     * @return
     * @author river
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseModel<String> httpMessageNotReadableException(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.info(" HttpMessageNotReadableException ");
        logger.error(e.getMessage(), e);
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setUrl(req.getRequestURL().toString());
        responseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseModel.setDevMessage(e.getMessage());
        responseModel.setMessage("请求参数异常");
        response.setStatus(HttpStatus.OK.value());
        return responseModel;
    }

    /**
     * @param req
     * @param response
     * @param e
     * @return
     * @author river
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseModel<String> jsonErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.info("in exception handler - RuntimeException");
        logger.error(e.getMessage(), e);
        ResponseModel responseModel = ResponseHelper.buildRespons("");
        responseModel.setUrl(req.getRequestURL().toString());
        responseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseModel.setDevMessage(e.getMessage());
        responseModel.setMessage("运行时异常");
        response.setStatus(HttpStatus.OK.value());
        return responseModel;
    }
}