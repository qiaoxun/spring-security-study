package com.study.filter;

import com.study.authentication.MyAuthenticationFailureHandler;
import com.study.exception.VerifyCodeWrongException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VerifyCodeFilter_V1 extends OncePerRequestFilter {

    public static final String VERIFY_CODE = "VERIFY_CODE";
    public static final String SPRING_SECURITY_FORM_VERIFY_CODE_KEY = "verifyCode";

    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    public VerifyCodeFilter_V1() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if ("/authentication/form".equals(requestURI)) {
            HttpSession session = request.getSession();
            String verifyCode = request.getParameter(SPRING_SECURITY_FORM_VERIFY_CODE_KEY);
            String verifyCodeInSession = (String) session.getAttribute(VERIFY_CODE);
            session.removeAttribute(VERIFY_CODE);
            if (null != verifyCodeInSession && !verifyCodeInSession.equals(verifyCode)) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeWrongException("Verify Code is wrong"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        if (!requiresAuthentication(request, response)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        HttpSession session = request.getSession();
//        String verifyCode = request.getParameter(SPRING_SECURITY_FORM_VERIFY_CODE_KEY);
//        String verifyCodeInSession = (String) session.getAttribute(VERIFY_CODE);
//        session.removeAttribute(VERIFY_CODE);
//
//        if (null != verifyCodeInSession && verifyCodeInSession.equals(verifyCode)) {
//            myAuthenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeWrongException("Verify Code is wrong"));
//            return;
//        }
//
//        chain.doFilter(req, res);
//    }


//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        HttpSession session = request.getSession();
//        String verifyCode = request.getParameter(SPRING_SECURITY_FORM_VERIFY_CODE_KEY);
//        String verifyCodeInSession = (String) session.getAttribute(VERIFY_CODE);
//        session.removeAttribute(VERIFY_CODE);
//
//        if (null != verifyCodeInSession && verifyCodeInSession.equals(verifyCode)) {
//            throw new VerifyCodeWrongException("Verify Code is wrong");
//        }
//        return super.attemptAuthentication(request, response);
//    }

    public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return myAuthenticationFailureHandler;
    }

    public void setMyAuthenticationFailureHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }
}
