package org.tycloud.core.auth.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tycloud.component.cache.Redis;
import org.tycloud.component.cache.enumeration.CacheType;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.auth.face.model.SsoSessionsModel;
import org.tycloud.core.auth.face.model.WechatInfoModel;
import org.tycloud.core.auth.face.orm.dao.SsoSessionsMapper;
import org.tycloud.core.auth.face.orm.entity.SsoSessions;
import org.tycloud.core.foundation.exception.BaseException;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录SESSION 服务实现类
 * </p>
 *
 * @author magintursh
 * @since 2017-07-06
 */
@Service
public class SsoSessionsService extends BaseService<SsoSessionsModel,SsoSessions,SsoSessionsMapper> implements IService<SsoSessions> {


    public static final String SESSION = "SESSION";
    public static final String SESSION_TOKEN = "SESSION_TOKEN";

    public static final String WECHAT_TOKEN_TYPE="wechat";


    public static final Long DEFAULT_SESSION_EXPIRATION = 2592000L;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private WechatInfoService wechatInfoService;

    //@EntityCache(keyPrefix = {SsoSessionsService.SESSION,SsoSessionsService.SESSION_TOKEN},storageMode = StorageMode.VALUE,
    //		expire = 2592000L)


    public static String sessionCacheKeyWithToken(String token,String actionByProduct)
    {
        return Redis.genKey(CacheType.MANUALLY_CACHE.name(),SESSION_TOKEN,actionByProduct,token);
    }

    public static String sessionCacheKeyWithLoginId(String loginId,String actionByProduct)
    {
        return Redis.genKey(CacheType.MANUALLY_CACHE.name(),SESSION,actionByProduct,loginId);
    }

    /**
     * 刷新session
     * @param token
     * @param actionByProduct
     * @return
     * @throws Exception
     */
    public  SsoSessionsModel refreshSession(String token,String actionByProduct,String tokenType) throws Exception{

        //SsoSessionsModel sessionsModel             = this.refreshCacheExpire(token,actionByProduct);
        SsoSessionsModel sessionsModel = (SsoSessionsModel)this.redisTemplate.opsForValue().get(sessionCacheKeyWithToken(token,actionByProduct));

        if(!ValidationUtil.isEmpty(sessionsModel))
        {

            redisTemplate.expire(sessionCacheKeyWithToken(token,actionByProduct),DEFAULT_SESSION_EXPIRATION, TimeUnit.SECONDS);
            if(StringUtils.isBlank(tokenType)){
                LoginInfoModel loginInfo            = loginInfoService.selectByLoginId(sessionsModel.getLoginId());
                if(ValidationUtil.isEmpty(loginInfo)) {
                    throw new Exception("用户信息异常.");
                }
            }
            if(WECHAT_TOKEN_TYPE.equals(tokenType)){
                WechatInfoModel wechatInfoModel =  wechatInfoService.findByOpendId(sessionsModel.getLoginId());
                if(ValidationUtil.isEmpty(wechatInfoModel)){
                    throw new Exception("用户信息异常.");
                }
            }
            sessionsModel.setSessionExpiration(SsoSessionsService.DEFAULT_SESSION_EXPIRATION);
        }
        return sessionsModel;
    }



    @Transactional(rollbackFor = {Exception.class, BaseException.class})
    public SsoSessionsModel createSession(SsoSessionsModel sessionsModel) throws Exception
    {
        this.removeSession(sessionsModel.getActionByProduct(),sessionsModel.getLoginId());
        this.redisTemplate.opsForValue().set(sessionCacheKeyWithToken(sessionsModel.getToken(),sessionsModel.getActionByProduct()),sessionsModel);
        this.redisTemplate.opsForValue().set(sessionCacheKeyWithLoginId(sessionsModel.getLoginId(),sessionsModel.getActionByProduct()),sessionsModel);

        redisTemplate.expire(sessionCacheKeyWithToken(sessionsModel.getToken(),sessionsModel.getActionByProduct()),DEFAULT_SESSION_EXPIRATION, TimeUnit.SECONDS);
        redisTemplate.expire(sessionCacheKeyWithLoginId(sessionsModel.getLoginId(),sessionsModel.getActionByProduct()),DEFAULT_SESSION_EXPIRATION, TimeUnit.SECONDS);


        return sessionsModel;
    }



    public void removeSession(String  actionByProduct ,String loginId) throws Exception
    {
        SsoSessionsModel sessionsModel = (SsoSessionsModel)this.redisTemplate.opsForValue().get(sessionCacheKeyWithLoginId(loginId,actionByProduct));
        if(!ValidationUtil.isEmpty(sessionsModel))
        {
            this.redisTemplate.delete(sessionCacheKeyWithLoginId(loginId,actionByProduct));
            this.redisTemplate.delete(sessionCacheKeyWithToken(sessionsModel.getToken(),actionByProduct));
        }

    }

    public void removeAllSession(String[] products ,String loginId) throws Exception
    {
        for(String product : products)
        {
            this.removeSession(product,loginId);
        }
    }


    public  SsoSessionsModel queryByToken(String  actionByProduct ,String token)throws Exception
    {

        SsoSessionsModel sessionsModel = (SsoSessionsModel)this.redisTemplate.opsForValue().get(sessionCacheKeyWithToken(token,actionByProduct));
        return sessionsModel;
    }

    public  SsoSessionsModel queryByLoginId(String  actionByProduct ,String loginId)throws Exception
    {

        SsoSessionsModel sessionsModel = (SsoSessionsModel)this.redisTemplate.opsForValue().get(sessionCacheKeyWithLoginId(loginId,actionByProduct));
        return sessionsModel;
    }





}
