package org.tycloud.core.restful.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tycloud.core.foundation.context.RequestContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * <pre>
 *  Tyrest
 *  File: ResponseHelper.java
 * 
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 * 
 *  Description:
 *  TODO
 * 
 *  Notes:
 *  $Id: ResponseHelper.java  Tyrest\magintrursh $ 
 * 
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月1日		magintrursh		Initial.
 *
 * </pre>
 */
public class ResponseHelper {

	public static <T> ResponseModel<T> buildRespons(T t) {
		ResponseModel<T> response = new ResponseModel<T>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		response.setResult(t);
		response.setDevMessage("SUCCESS");
		response.setStatus(HttpStatus.OK.value());
		response.setTraceId(RequestContext.getTraceId());
		response.setUrl(request.getRequestURL().toString());
		return response;
	}
}

/*
 * $Log: av-env.bat,v $
 */