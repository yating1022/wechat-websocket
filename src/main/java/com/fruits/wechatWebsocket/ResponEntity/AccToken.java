package com.fruits.wechatWebsocket.ResponEntity;

public class AccToken {

    private Integer errcode;
    private String errmsg;
    private String access_token;
    private Integer expires_in;

    public AccToken(Integer errcode, String errmsg, String access_token, Integer expires_in) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public AccToken() {
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
