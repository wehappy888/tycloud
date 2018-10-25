package org.tycloud.core.restful.interceptor;




import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tycloud.core.foundation.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 *
 *  Tyrest
 *  File: RestEventInterceptor.java
 *
 *  Tyrest, Inc.
 *  Copyright (C): 2015
 *
 *  Description:系统的业务事件拦截器
 *  TODO 用于拦截所有的业务事件，并交给监听器处理
 *
 *  Notes:
 *  $Id: RestEventInterceptor.java 31101200-9 2014-10-14 16:43:51Z Tyrest\magintursh $
 *
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2015年6月23日		magintursh		Initial.
 *
 * </pre>
 */
@Aspect
@Order(value = 1)
@Component
public class RestResourceInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestResourceInterceptor.class);


    /*定义切入点，所有那些触发业务事件的方法都会成为切入点*/
    @Pointcut("execution(* org.tycloud.api.*.controller.*.*Resource*.*(..))" +
            "   || execution(* org.tycloud.api.*.controller.*Resource*.*(..))")
    public void restEventOccured() {
    }

    /*在触发业务事件的方法执行完之后触发系统业务事件*/
    @Around("restEventOccured()")
    public Object fireEvent(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object[] args = pjp.getArgs();
        List<Object> objectList = Arrays.asList(args).stream().filter(
                t-> (t instanceof HttpServletRequest) == false
                    && (t instanceof HttpServletResponse) == false)
                .collect(Collectors.toList());

        logger.info("请求 args =  "+ new Gson().toJson(objectList));
        Object retVal = pjp.proceed();

        logger.info("请求耗时-TRACEID："+RequestContext.getTraceId()+" : "+(System.currentTimeMillis()-time));
        logger.info("请求 result "+ new Gson().toJson(retVal));

        return retVal;
    }
}

/*
*$Log: av-env.bat,v $
*/