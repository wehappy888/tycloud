package org.tycloud.core.auth.face.model;

import java.io.Serializable;

public class AuthModel implements Serializable {


    private static final long serialVersionUID = 326567545234532L;

    private String loginId;

    private String cookieKey;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }


    public String getCookieKey() {
        return cookieKey;
    }

    public void setCookieKey(String cookieKey) {
        this.cookieKey = cookieKey;
    }
}
