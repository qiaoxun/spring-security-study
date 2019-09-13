package com.study.properties;

import com.study.utils.LoginType;

import static com.study.utils.LoginType.JSON;

public class BrowserProperties {
    private String loginPage = "/security-login.html";

    private LoginType loginType = JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
