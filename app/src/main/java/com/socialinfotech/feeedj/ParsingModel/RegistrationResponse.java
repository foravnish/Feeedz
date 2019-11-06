package com.socialinfotech.feeedj.ParsingModel;

/**
 * Created by Social Infotech on 6/22/2016.
 */
public class RegistrationResponse {

    /**
     * access_token : TpqEN9NsAERbUbMHA1nZaV70ALOLxbDLhBg-3BB2Zr5CYs2rwqnlN3wesY_rTlF0GJUPRO0zoFETsMOcUs0zygER9DL4voqns5dINAfDJKzIsKg58hvuVIUFG-3PcYh1Wu3G9tXrjT9zOn7_OMNIwOBQQH0LMsKxHMB6p-l1O-fgpVAXkKMe4LVlfKkPFW14OaOFFEru3qk3b1t2WWxtCnGsd6uKJB-6SKZUYxd0HC38qFnaU-BhzknXF_xWo8X7YfIdon2Xf8kwEYznwQoPK8yuSrLc_RJza0oiDnaWHlHD_2HmsKgD6uStXYz_Z8xhQMWtRSkbwX6kJon09XTulw
     * token_type : bearer
     * expires_in : 31536000
     * userName : piyu
     * .issued : Wed, 22 Jun 2016 09:53:20 GMT
     * .expires : Thu, 22 Jun 2017 09:53:20 GMT
     */

    private String access_token;
    private String token_type;
    private String expires_in;
    private String userName;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
