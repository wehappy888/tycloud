package org.tycloud.core.auth.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tycloud.component.cache.Redis;
import org.tycloud.core.auth.exception.AuthException;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.auth.face.orm.dao.LoginInfoMapper;
import org.tycloud.core.auth.face.orm.entity.LoginInfo;
import org.tycloud.core.foundation.constans.CoreConstans;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.utils.Encrypt;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类s
 * </p>
 *
 * @author magintursh
 * @since 2017-07-06
 */
@Service
public class LoginInfoService extends BaseService<LoginInfoModel,LoginInfo,LoginInfoMapper> implements IService<LoginInfo> {


    public static final String CACHEKEY_LOGININFO = "LOGININFO";



    @Autowired
    private RedisTemplate redisTemplate;



    public LoginInfoModel udpateLoginInfo(LoginInfoModel loginInfoModel) throws Exception
    {
        return this.updateWithModel(loginInfoModel);
    }


    public LoginInfoModel selectByLoginId(String loginId) throws Exception
    {
        return this.queryModelByParams(loginId);
    }


    public LoginInfoModel selectByLoginIdAndUserType(String loginId,String userType) throws Exception
    {
        return this.queryModelByParams(loginId,userType);
    }


    public LoginInfoModel selectByLoginIdFromCache(String loginId) throws Exception
    {
        return (LoginInfoModel) redisTemplate.opsForHash().get(CACHEKEY_LOGININFO,loginId);
    }



    public LoginInfoModel selectByLoginIdUnlock(String loginId) throws Exception
    {
        LoginInfoModel loginInfoModel = selectByLoginId( loginId);
        if(ValidationUtil.isEmpty(loginInfoModel)){
            throw new AuthException("登陆的用户信息不存在");
        }
        if(PropertyValueConstants.LOCK_STATUS_LOCK.equals(loginInfoModel.getLockStatus())){
            loginInfoModel = null;
        }
        return loginInfoModel;
    }



    public List<LoginInfoModel> queryByUserId(String userId)throws Exception
    {
        List<LoginInfoModel> models =  this.queryForList(null,false,userId);
        return models;
    }


    /**
     * 禁用指定用户的所有登录信息
     * @param userId 用户id
     * @return
     */
    public boolean lockWithUserId(String userId)throws Exception
    {
        boolean flag = false;
        List<LoginInfoModel> models = this.queryByUserId(userId);
        if(!ValidationUtil.isEmpty(models))
        {
            for(LoginInfoModel loginInfo:models)
                this.lockLoginInfo(loginInfo);
            flag = true;
        }
        return flag;
    }

    /**
     * 禁用指定的登录信息
     * @param loginId
     * @return
     */
    public LoginInfoModel lockWithLoginId(String loginId) throws Exception
    {
        return this.lockLoginInfo(this.selectByLoginId(loginId));
    }


    private LoginInfoModel lockLoginInfo(LoginInfoModel loginInfoModel) throws Exception
    {
        if(!ValidationUtil.isEmpty(loginInfoModel) && PropertyValueConstants.LOCK_STATUS_UNLOCK.equals(loginInfoModel.getLockStatus()))
        {
            loginInfoModel.setLockDate(new Date());
            loginInfoModel.setLockStatus(PropertyValueConstants.LOCK_STATUS_LOCK);
            loginInfoModel.setLockUserId(RequestContext.getExeUserId());

            loginInfoModel = this.updateWithModel(loginInfoModel);
        }
        return loginInfoModel;
    }

    public LoginInfoModel createLoginInfo(String userId,String loginId,String idType,String userType,String agencyCode) throws Exception
    {
        LoginInfoModel newLoginInfo = new LoginInfoModel();

        newLoginInfo.setUserId(userId);
        newLoginInfo.setLockStatus(PropertyValueConstants.LOCK_STATUS_UNLOCK);
        newLoginInfo.setAgencyCode(agencyCode);
        newLoginInfo.setLoginId(loginId);
        newLoginInfo.setIdType(idType);
        newLoginInfo.setSalt(Encrypt.generateSalt(CoreConstans.PASSWORD_SALT_LENGTH));
        newLoginInfo.setUserType(userType);
        newLoginInfo.setPassword(Encrypt.md5ForAuth(CoreConstans.DEFAULT_PASSWORD,newLoginInfo.getSalt()));
        return this.createWithModel(newLoginInfo);
    }


    public LoginInfoModel createLoginInfoWithPassword(String userId,String loginId,String idType,String userType,String agencyCode,String password) throws Exception
    {
        //更新logininfo表
        LoginInfoModel loginInfoModel = new LoginInfoModel();
        loginInfoModel.setAgencyCode(agencyCode);
        loginInfoModel.setLoginId(loginId);//手机号码
        loginInfoModel.setIdType(idType);
        loginInfoModel.setUserId(userId);
        loginInfoModel.setUserType(userType);
        loginInfoModel.setLockStatus(PropertyValueConstants.LOCK_STATUS_UNLOCK);
        loginInfoModel.setSalt(Encrypt.generateSalt(CoreConstans.PASSWORD_SALT_LENGTH));
        if(ValidationUtil.isEmpty(password))
        {
            loginInfoModel.setPassword(Encrypt.md5ForAuth(CoreConstans.DEFAULT_PASSWORD,loginInfoModel.getSalt()));
        }else{
            loginInfoModel.setPassword(Encrypt.md5ForAuth(password,loginInfoModel.getSalt()));
        }

        return  this.createWithModel(loginInfoModel);
    }







}
