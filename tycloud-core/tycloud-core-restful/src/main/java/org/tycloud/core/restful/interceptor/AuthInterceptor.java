package org.tycloud.core.restful.interceptor;

/**
 * Created by magintursh on 2017-07-04.
 */

import com.baomidou.mybatisplus.toolkit.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.face.model.SsoSessionsModel;
import org.tycloud.core.auth.face.service.SsoSessionsService;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.foundation.utils.DateUtil;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.restful.auth.AuthHandler;
import org.tycloud.core.restful.auth.AuthWithSessionHandler;
import org.tycloud.core.restful.doc.TycloudOperation;
import org.tycloud.core.restful.utils.APILevel;
import org.tycloud.core.restful.utils.RequestUtil;
import org.tycloud.core.restful.utils.RestfulConstans;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 自定义拦截器1
 *
 * @author   Angel
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private static final  Sequence sequence = new Sequence();

    private static SortedMap<Integer,AuthHandler> authHandlers                      = new TreeMap<>();
    private static SortedMap<Integer,AuthWithSessionHandler> authWithSessionHandlers = new TreeMap<>();




    @Autowired
    private SsoSessionsService ssoSessionsService;


    public static void addAuthHandler(AuthHandler authHandler) throws Exception
    {
        if(!ValidationUtil.isEmpty(authHandlers.get(authHandler.order())))
            throw new Exception("the order of "+authHandler.order()+ " have bean exist");
       authHandlers.put(authHandler.order(),authHandler);
    }

    public static void addAuthWithSessionHandler(AuthWithSessionHandler authWithSessionHandler) throws Exception
    {
        if(!ValidationUtil.isEmpty(authWithSessionHandlers.get(authWithSessionHandler.order())))
            throw new Exception("the order of "+authWithSessionHandler.order()+ " have bean exist");
        authWithSessionHandlers.put(authWithSessionHandler.order(),authWithSessionHandler);
    }




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    //System.out.println(">>>AuthInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");

        if(HttpMethod.OPTIONS.name().equals(request.getMethod()))
        {
            return true;
        }

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    RequestContext.clean();

    String appkey       = request.getHeader(RestfulConstans.APPKEY);
    String token        = request.getHeader(RestfulConstans.TOKEN);
    String product      = request.getHeader(RestfulConstans.PRODUCT);
    String traceId      = request.getHeader(RestfulConstans.TRACEID);
    String requestIp    = RequestUtil.getRemoteIp(request);
    String userAgent    = request.getHeader(RestfulConstans.USER_AGENT);
    String tokenType = request.getHeader(RestfulConstans.TOKEN_TYPE);
    String sessionId = request.getHeader(RestfulConstans.SESSION_ID);

    if(ObjectUtils.isEmpty(token) && ! ObjectUtils.isEmpty(request.getCookies())){
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(RestfulConstans.SESSION_ID)){
                token = cookie.getValue();
            }
        }
        if(!ObjectUtils.isEmpty(token)){
            token = token.split(":")[1];
        }
    }


    //设置请求标识
    traceId =setTraceId(traceId);

    RequestContext.setTraceId(traceId);
    RequestContext.setRequestIP(requestIp);
    RequestContext.setProduct(product);
    RequestContext.setUserAgent(userAgent);
    RequestContext.setToken(token);

    StringBuilder logInfo = new StringBuilder();
    logInfo.append("\n**********************************************************");
    logInfo.append("\n* APP_KEY     :" + appkey);
    logInfo.append("\n* TOKEN       :" + token);
    logInfo.append("\n* PRODUCT     :" + product);
    logInfo.append("\n* REQUEST_IP  :" + requestIp);
    logInfo.append("\n* USER_AGENT  :" + userAgent);
    logInfo.append("\n* TRACE_ID    :" + traceId);
    logInfo.append("\n* SESSION_ID  :" + sessionId);
    logInfo.append("\n* HANDLER     :" + handlerMethod.getBean().getClass());
    logInfo.append("\n* METHOD      :" + handlerMethod.getMethod().getName());
    logInfo.append("\n* ACCESS_TIME :" + DateUtil.getNow(DateUtil.Y_M_D_HMS));
    logInfo.append("\n**********************************************************");
    logger.info(logInfo.toString());



    return doAuth(handlerMethod,token,appkey,product,tokenType,sessionId);
}




    private boolean doAuth(HandlerMethod handlerMethod,
                           String token, String appKey, String product,String tokenType,String sessionId) throws Exception {

        /**
         * 扩展的验证规则
         */
        if(!ValidationUtil.isEmpty(authHandlers))
            for(Integer key:authHandlers.keySet())
                authHandlers.get(key).doAuth(handlerMethod,token,appKey,product);


        TycloudOperation tycloudOperation = handlerMethod.getMethodAnnotation(TycloudOperation.class);

        if(ValidationUtil.isEmpty(token) && tycloudOperation.needAuth())
        {
            throw new AuthException("请求未包含认证信息.");
        }else if(!tycloudOperation.needAuth())
        {
            return true;
        }

        //驗證appkey @TODO

        SsoSessionsModel sessionsModel = null;
        if(!ValidationUtil.isEmpty(token))
        {
            //刷新session：判断用户状态，登录信息状态，session是否过期,然后重置session过期时间
             sessionsModel =  ssoSessionsService.refreshSession(token,product,tokenType);

            if(ValidationUtil.isEmpty(sessionsModel))
            {
                throw new AuthException("登录信息失效，请重新登录");
            }

            //用户权限与接口权限验证
            //this.checkUserType(sessionsModel.getUserType(),tycloudOperation.ApiLevel());


            //扩展的验证规则
            if(!ValidationUtil.isEmpty(authWithSessionHandlers))
                for(Integer key :authWithSessionHandlers.keySet())
                    authWithSessionHandlers.get(key).doAuth(sessionsModel,handlerMethod,token,appKey,product);

        }
        if (!ValidationUtil.isEmpty(sessionsModel)) {
            setUser2ThreadLocal(sessionsModel);
        }
        return true;
    }


    private void setUser2ThreadLocal(SsoSessionsModel session) {

        RequestContext.setExeUserId(session.getUserId());

        RequestContext.setAgencyCode(session.getAgencyCode());

        RequestContext.setUserType(UserType.getUserType(session.getUserType()));

        RequestContext.setOpenId(session.getOpenId());

    }



    private void checkUserType(String userTypeStr, APILevel apiLevel) throws Exception
    {
        UserType userType = UserType.getUserType(userTypeStr);
        if(userType.getValue() < apiLevel.getValue())
        {
            throw new AuthException("用户权限不够.");
        }
    }




    /**
     * 设置请求的跟踪ID
     * @param traceId
     */
    private String setTraceId(String traceId) throws Exception {
        if (ValidationUtil.isEmpty(traceId)) {
            traceId = String.valueOf(sequence.nextId());
        }

        return traceId;
    }
}