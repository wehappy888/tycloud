package org.tycloud.core.auth.authentication;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.enumeration.ProvidedAuthType;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.face.model.AuthModel;
import org.tycloud.core.auth.face.model.LoginHistoryModel;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.auth.face.model.SsoSessionsModel;
import org.tycloud.core.auth.face.service.LoginHistoryService;
import org.tycloud.core.auth.face.service.SsoSessionsService;
import org.tycloud.core.foundation.constans.CoreConstans;
import org.tycloud.core.foundation.constans.ParamsConstants;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.context.SpringContextHelper;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.foundation.exception.BaseException;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.CommonUtil;
import org.tycloud.core.foundation.utils.Sequence;
import org.tycloud.core.foundation.utils.ValidationUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by magintursh on 2017-07-17.
 */

@Component
public   class Authenticator  {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);



    @Autowired
    SsoSessionsService ssoSessionsService;

    @Autowired
    LoginHistoryService loginHistoryService;



    private   static AuthenticatorHandler getAuthenticator(ProvidedAuthType authType) throws Exception {
        return (AuthenticatorHandler)SpringContextHelper.getBean(authType.getAuthenticator());
    }


    @Transactional(rollbackFor = {Exception.class, BaseException.class})
    public SsoSessionsModel createSession(IdType idType, ProvidedAuthType authType, UserType userType, AuthModel authModel) throws Exception
    {
        String product = RequestContext.getProduct();
        String requestIP = RequestContext.getRequestIP();
        String userAgent = RequestContext.getUserAgent();


        if(ValidationUtil.isEmpty(product)
                || ValidationUtil.isEmpty(requestIP)
                || ValidationUtil.isEmpty(userAgent)){
            throw new AuthException("请求头信息不完整.");
        }

        SsoSessionsModel ssoSessionsModel  = new SsoSessionsModel();
        ssoSessionsModel.setActionByAgent(userAgent);
        ssoSessionsModel.setActionByIp(requestIP);
        ssoSessionsModel.setActionByProduct(product);
        ssoSessionsModel.setSessionCreateTime(new Date());
        ssoSessionsModel.setSessionExpiration(SsoSessionsService.DEFAULT_SESSION_EXPIRATION);
        ssoSessionsModel.setSessionStatus(PropertyValueConstants.SESSION_STATUS_ACTIVE);
        ssoSessionsModel.setUserType(userType.name());
        ssoSessionsModel.setToken(CommonUtil.getUUID());
        if(UserType.ANONYMOUS.equals(userType))
        {
            ssoSessionsModel.setAgencyCode(CoreConstans.CODE_SUPER_ADMIN);
            ssoSessionsModel.setLoginId(Sequence.generatorUserId());
            ssoSessionsModel.setUserId(ssoSessionsModel.getLoginId());
            ssoSessionsModel.setUserName(userType.getLabel());
        }else
        {
            LoginInfoModel loginInfoModel = getAuthenticator(authType).doAuthenticate(idType, userType, authModel);
            if(ValidationUtil.isEmpty(loginInfoModel)){
                throw new AuthException("登陆的用户信息不存在");
            }
            ssoSessionsModel.setAgencyCode(loginInfoModel.getAgencyCode());
            ssoSessionsModel.setLoginId(loginInfoModel.getLoginId());
            ssoSessionsModel.setUserId(loginInfoModel.getUserId());
            ssoSessionsModel.setOpenId(loginInfoModel.getOpenId());
           // ssoSessionsModel.setUserName(loginInfoModel.get);
            ssoSessionsModel.setUserType(userType.name());

        }
        //保存session和登陆记录
        return createLoginHistory(ssoSessionsModel);
    }



    public Map<String,Object> authLogin(IdType idType, ProvidedAuthType authType, UserType userType, AuthModel authModel) throws Exception
    {
        SsoSessionsModel ssoSessionsModel = this.createSession(idType, authType, userType, authModel);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ParamsConstants.TOKEN, ssoSessionsModel.getToken());
        result.put(ParamsConstants.EXPIRE, ssoSessionsModel.getSessionExpiration());
        result.put(ParamsConstants.USERID, ssoSessionsModel.getUserId());
        result.put(ParamsConstants.OPEN_ID,ssoSessionsModel.getOpenId());
        return result;
    }



    private SsoSessionsModel createLoginHistory(SsoSessionsModel ssoSessionsModel) throws Exception
    {


            RequestContext.setExeUserId(ssoSessionsModel.getUserId().toString());

            RequestContext.setAgencyCode(ssoSessionsModel.getAgencyCode());

            RequestContext.setUserType(UserType.getUserType(ssoSessionsModel.getUserType()));
            RequestContext.setOpenId(ssoSessionsModel.getOpenId());


            //清理旧的session
            ssoSessionsService.removeSession(ssoSessionsModel.getActionByProduct(),ssoSessionsModel.getLoginId());

             //新增登陆记录
            LoginHistoryModel loginHistory = Bean.copyExistPropertis(ssoSessionsModel,new LoginHistoryModel());
            loginHistoryService.createLoginHistory(loginHistory);
            return ssoSessionsService.createSession(ssoSessionsModel);
    }







}
