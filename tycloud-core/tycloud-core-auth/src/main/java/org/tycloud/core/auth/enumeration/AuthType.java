package org.tycloud.core.auth.enumeration;

/** 
 * 
 * <pre>
 *  Tyrest
 *  File: AuthType.java
 * 
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 * 
 *  Description:
 *  TODO
 * 
 *  Notes:
 *  $Id: AuthType.java  Tyrest\magintrursh $ 
 * 
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月17日		magintrursh		Initial.
 *
 * </pre>
 */
public enum AuthType implements ProvidedAuthType{
	ID_PASSWORD("idPasswordAuthenticator"),
	WECHAT("wechatAuthenticator");



	 AuthType(String authenticator)
	{
			this.authenticator = authenticator;
	}

	private String  authenticator;


	public String getAuthenticator() {
		return authenticator;
	}

	public String getAuthType()
	{
		return this.name();
	}

	public static AuthType getAuthType(String name){
		AuthType authType = null;
		for(AuthType at : AuthType.values()){
			if(at.name().equals(name)){
				authType = at;
			}
		}
		return authType;
	}

}

/*
 * $Log: lexingbuild.bat,v $
 */