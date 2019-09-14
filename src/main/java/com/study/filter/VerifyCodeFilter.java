package com.study.filter;

import com.study.authentication.MyAuthenticationFailureHandler;
import com.study.exception.VerifyCodeWrongException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VerifyCodeFilter extends UsernamePasswordAuthenticationFilter {

    public static final String VERIFY_CODE = "VERIFY_CODE";
    public static final String SPRING_SECURITY_FORM_VERIFY_CODE_KEY = "verifyCode";

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    public VerifyCodeFilter() {
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        HttpSession session = request.getSession();
        String verifyCode = request.getParameter(SPRING_SECURITY_FORM_VERIFY_CODE_KEY);
        String verifyCodeInSession = (String) session.getAttribute(VERIFY_CODE);
        session.removeAttribute(VERIFY_CODE);

        if (null != verifyCodeInSession && verifyCodeInSession.equals(verifyCode)) {
            throw new VerifyCodeWrongException("Verify Code is wrong");
        }
        return super.attemptAuthentication(request, response);
    }

    public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return myAuthenticationFailureHandler;
    }

    public void setMyAuthenticationFailureHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }
}
