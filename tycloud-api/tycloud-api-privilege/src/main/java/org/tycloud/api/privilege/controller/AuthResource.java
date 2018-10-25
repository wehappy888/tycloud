package org.tycloud.api.privilege.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tycloud.core.auth.authentication.Authenticator;
import org.tycloud.core.auth.enumeration.AuthType;
import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.enumeration.ProvidedAuthType;
import org.tycloud.core.auth.face.model.*;
import org.tycloud.core.foundation.constans.ParamsConstants;
import org.tycloud.core.foundation.enumeration.UserType;
import org.tycloud.core.restful.doc.TycloudOperation;
import org.tycloud.core.restful.doc.TycloudResource;
import org.tycloud.core.restful.utils.APILevel;
import org.tycloud.core.restful.utils.ResponseHelper;
import org.tycloud.core.restful.utils.ResponseModel;
import org.tycloud.core.restful.utils.RestfulConstans;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by magintursh on 2017-05-03.
 */
@TycloudResource(module = "privilege",value = "AuthResource")
@RequestMapping(value = "/v1/privilege/auth")
@Api(value = "privilege-登录验证")
@RestController
public class AuthResource {

    private final Logger logger = Logger.getLogger(AuthResource.class);

    @Autowired
    Authenticator authenticator;

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="公网用户名密码登录")
    @RequestMapping(value = "/public/idpassword", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> idPasswordAuthForPublic(@RequestBody IdPasswordAuthModel model) throws Exception
    {
        return  this.doAuthenticate(IdType.userName, AuthType.ID_PASSWORD, UserType.PUBLIC_USER,model);
    }

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="机构用户名密码登录")
    @RequestMapping(value = "/agency/idpassword", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> idPasswordAuthForAgency(@RequestBody IdPasswordAuthModel model) throws Exception
    {
        return  this.doAuthenticate(IdType.userName, AuthType.ID_PASSWORD, UserType.AGENCY_USER,model);
    }

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="平台用户名密码登录")
    @RequestMapping(value = "/super/idpassword", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> idPasswordAuthForSuper(@RequestBody IdPasswordAuthModel model) throws Exception
    {
        return  this.doAuthenticate(IdType.userName, AuthType.ID_PASSWORD, UserType.SUPER_ADMIN,model);
    }

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="短信登录")
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> smsAuth(@RequestBody SmsAuthModel model) throws Exception
    {
        return null;
    }

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="第三方登录")
    @RequestMapping(value = "/openId", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> thirdPartyAuth(@RequestBody IdPasswordAuthModel model) throws Exception
    {
        return null;
    }

    @TycloudOperation( ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value="匿名用户登录")
    @RequestMapping(value = "/anonymous", method = RequestMethod.POST)
    public ResponseModel<LoginInfoModel> anonymousAuth(@RequestBody IdPasswordAuthModel model) throws Exception
    {
        return null;
    }

    private ResponseModel doAuthenticate(IdType idType, ProvidedAuthType authType, UserType userType, AuthModel authModel) throws Exception
    {
        Map<String, Object> result = authenticator.authLogin(idType, authType, userType, authModel);
        return ResponseHelper.buildRespons(result);
    }

}