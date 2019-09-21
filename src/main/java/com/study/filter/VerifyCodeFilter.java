package com.study.filter;

import com.study.authentication.MyAuthenticationFailureHandler;
import com.study.exception.VerifyCodeWrongException;
import com.study.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * If it's rest api, we can also validate the verify code at login method
 */
public class VerifyCodeFilter extends AbstractAuthenticationProcessingFilter {

    public static final String VERIFY_CODE = "VERIFY_CODE";
    public static final String SPRING_SECURITY_FORM_VERIFY_CODE_KEY = "verifyCode";

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    private String loginProcessingUrl;

    public VerifyCodeFilter(String loginProcessingUrl) {
        super(new AntPathRequestMatcher(loginProcessingUrl, "GET"));
        this.loginProcessingUrl = loginProcessingUrl;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request, response)) {
            HttpSession session = request.getSession();
            String verifyCode = request.getParameter(SPRING_SECURITY_FORM_VERIFY_CODE_KEY);
            String verifyCodeInSession = (String) session.getAttribute(VERIFY_CODE);
            session.removeAttribute(VERIFY_CODE);

            if (null != verifyCodeInSession && !verifyCodeInSession.equals(verifyCode)) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeWrongException("Verify Code is wrong"));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return null;
    }

    public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return myAuthenticationFailureHandler;
    }

    public void setMyAuthenticationFailureHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }
}
