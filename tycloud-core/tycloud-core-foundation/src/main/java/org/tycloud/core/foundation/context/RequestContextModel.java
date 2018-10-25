package org.tycloud.core.foundation.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.tycloud.core.foundation.enumeration.UserType;

/**
 * 
 * <pre>
 *  Tyrest
 *  File: RequestContextModel.java
 * 
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 * 
 *  Description:
 *  TODO
 * 
 *  Notes:
 *  $Id: RequestContextModel.java  Tyrest\magintrursh $ 
 * 
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月1日		magintrursh		Initial.
 *
 * </pre>
 */
public class RequestContextModel implements Serializable {
	private static final long serialVersionUID = -6237296664652045754L;

	private String agencyCode;

	private String traceId;

	private String excutedUserId;

	private UserType userType;

	private String requestIP;

	private String userAgent;

	private String businessTransactionId;

	private String product;// 调用API的设备信息

	private String appVersion;

	private String token;

	private String loginId;

	private String openId;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private Map<RequestContextEntityType, Object> extra;

	public String getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getExcutedUserId() {
		return excutedUserId;
	}

	public void setExcutedUserId(String excutedUserId) {
		this.excutedUserId = excutedUserId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getBusinessTransactionId() {
		return businessTransactionId;
	}

	public void setBusinessTransactionId(String businessTransactionId) {
		this.businessTransactionId = businessTransactionId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setAttribute(RequestContextEntityType key, Object value) {
		if (extra == null) {
			extra = new HashMap<RequestContextEntityType, Object>();
		}
		extra.put(key, value);
	}

	public Object getAttribute(RequestContextEntityType key) {
		if (extra == null) {
			return null;
		}
		return extra.get(key);
	}

	public void clean() {
		this.traceId = null;
		this.excutedUserId = null;
		this.agencyCode = null;
		this.userType = null;
		this.userAgent = null;
		this.businessTransactionId = null;
		this.product = null;
		this.appVersion = null;
		this.openId = null;
		if (this.extra != null) {
			this.extra = null;
		}
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
