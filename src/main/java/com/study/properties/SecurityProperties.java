package com.study.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "study.security")
public class SecurityProperties {
    private int port;
    private BrowserProperties browser = new BrowserProperties();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
