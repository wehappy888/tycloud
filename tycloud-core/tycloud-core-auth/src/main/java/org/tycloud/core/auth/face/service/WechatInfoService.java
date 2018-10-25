package org.tycloud.core.auth.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Component;
import org.tycloud.core.auth.face.model.WechatInfoModel;
import org.tycloud.core.auth.face.orm.dao.WechatMapper;
import org.tycloud.core.auth.face.orm.entity.WechatInfo;
import org.tycloud.core.rdbms.service.BaseService;

/**
 * <p>
 * 公网用户微信平台信息 服务类
 * 微信用户创建和更新逻辑：
 * 1.微信用户进入商城以后根据商城信息获取微信密钥，然后从微信平台获取用户基础信息，保存或更新对应的微信用户记录，
 * 2.在用户使用关键业务的时候要求填写详细信息，手机号、姓名等等，生成公网用户信息记录，以手机号作为唯一标识，并使用当前公网用户的userId更新到微信用户记录中
 * 3.微信用户再次进入商城时可以根据获取到的微信信息（openId）进行隐式登陆操作，拿到平台token进行所有的业务。
 * </p>
 *
 * @author 子杨
 * @since 2018-08-31
 */
@Component
public class WechatInfoService extends BaseService<WechatInfoModel, WechatInfo, WechatMapper> implements IService<WechatInfo> {


    /**
     * 根据openId 查询 微信用户信息
     * @param pubOpenId
     * @return
     * @throws Exception
     */
    public WechatInfoModel findByOpendId(String pubOpenId) throws Exception {
        return  queryModelByParams(pubOpenId);
    }


}
