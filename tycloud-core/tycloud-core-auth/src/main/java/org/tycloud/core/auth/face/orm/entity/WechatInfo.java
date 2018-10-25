package org.tycloud.core.auth.face.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;

import java.util.Date;

/**
 * <p>
 * 公网用户微信平台信息
 * </p>
 *
 * @author 子杨
 * @since 2018-08-31
 */
@TableName("public_user_wechat_info")
public class WechatInfo extends BaseEntity<WechatInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序openId
     */
	@TableField("XCX_OPEN_ID")
	private String xcxOpenId;
    /**
     * 公众号openId
     */
	@TableField("PUB_OPEN_ID")
	private String pubOpenId;
    /**
     * 国家
     */
	@TableField("COUNTRY")
	private String country;
    /**
     * 省份
     */
	@TableField("PROVINCE")
	private String province;
    /**
     * 城市
     */
	@TableField("CITY")
	private String city;
    /**
     * 性别
     */
	@TableField("SEX")
	private String sex;
    /**
     * 是否关注
     */
	@TableField("SUBSCRIBE")
	private String subscribe;
    /**
     * 关注时间
     */
	@TableField("SUBSCRIBE_TIME")
	private Date subscribeTime;
    /**
     * 来源opneID（当前系统暂时不用）
     */
	@TableField("ORIGINAL_OPEN_ID")
	private String originalOpenId;
    /**
     * 昵称
     */
	@TableField("NICK_NAME")
	private String nickName;
    /**
     * 头像
     */
	@TableField("AVATAR_URL")
	private String avatarUrl;
    /**
     * 永久二维码链接
     */
	@TableField("QRCODE_TICKET")
	private String qrcodeTicket;
    /**
     * 临时二维码链接
     */
	@TableField("TEMP_QRCODE_TICKET")
	private String tempQrcodeTicket;
    /**
     * 临时二维码截止时间
     */
	@TableField("TEMP_QRCODE_DEADLINE")
	private Date tempQrcodeDeadline;
    /**
     * 开放平台unionId
     */
	@TableField("UNION_ID")
	private String unionId;

	@TableField("USER_ID")
	private String userId;


	@TableField("AGENCY_CODE")
	private String agencyCode;


	@TableField("MALL_SEQ")
	private Long mallSeq;

	@TableField("APP_ID")
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

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getOriginalOpenId() {
		return originalOpenId;
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
