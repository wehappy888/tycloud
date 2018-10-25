package org.tycloud.core.auth.face.model;

import org.tycloud.core.rdbms.model.BaseModel;

import java.util.Date;

/**
 * <p>
 * 公网用户微信平台信息 model
 * </p>
 *
 * @author 子杨
 * @since 2018-08-31
 */
public class WechatInfoModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序openId
     */
    private String xcxOpenId;
    /**
     * 公众号openId
     */
    private String pubOpenId;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 性别
     */
    private String sex;
    /**
     * 是否关注
     */
    private String subscribe;
    /**
     * 关注时间
     */
    private Date subscribeTime;
    /**
     * 来源opneID（当前系统暂时不用）
     */
    private String originalOpenId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 永久二维码链接
     */
    private String qrcodeTicket;
    /**
     * 临时二维码链接
     */
    private String tempQrcodeTicket;
    /**
     * 临时二维码截止时间
     */
    private Date tempQrcodeDeadline;
    /**
     * 开放平台unionId
     */
    private String unionId;

    private String userId;


    private String agencyCode;


    private Long mallSeq;

    private String appId;

    public Long getMallSeq() {
        return mallSeq;
    }

    public void setMallSeq(Long mallSeq) {
        this.mallSeq = mallSeq;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getXcxOpenId() {
        return xcxOpenId;
    }

    public void setXcxOpenId(String xcxOpenId) {
        this.xcxOpenId = xcxOpenId;
    }

    public String getPubOpenId() {
        return pubOpenId;
    }

    public void setPubOpenId(String pubOpenId) {
        this.pubOpenId = pubOpenId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public String getOriginalOpenId() {
        return originalOpenId;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public void setOriginalOpenId(String originalOpenId) {
        this.originalOpenId = originalOpenId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getQrcodeTicket() {
        return qrcodeTicket;
    }

    public void setQrcodeTicket(String qrcodeTicket) {
        this.qrcodeTicket = qrcodeTicket;
    }

    public String getTempQrcodeTicket() {
        return tempQrcodeTicket;
    }

    public void setTempQrcodeTicket(String tempQrcodeTicket) {
        this.tempQrcodeTicket = tempQrcodeTicket;
    }

    public Date getTempQrcodeDeadline() {
        return tempQrcodeDeadline;
    }

    public void setTempQrcodeDeadline(Date tempQrcodeDeadline) {
        this.tempQrcodeDeadline = tempQrcodeDeadline;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
