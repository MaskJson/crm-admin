package com.moving.admin.bean;

import java.io.Serializable;

public class WxInfor implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String session_key;
    private String openid;

    public WxInfor() {

    }

    public WxInfor(String session_key, String openid) {
        this.session_key = session_key;
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
