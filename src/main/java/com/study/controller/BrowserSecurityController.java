package com.study.controller;

import com.study.domain.SimpleResponse;
import com.study.filter.VerifyCodeFilter;
import com.study.properties.SecurityProperties;
import com.study.utils.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(BrowserSecurityController.class);

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requestAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetURL = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求时: " + targetURL);
            if (StringUtils.endsWithIgnoreCase(targetURL, ".html")) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        logger.info("new SimpleResponse(\"请引导用户到登录页面\")");
//        Thread.dumpStack();
        return new SimpleResponse("请引导用户到登录页面");
    }

    /**
     * TODO 一次请求为什么会被调用两次？？？
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/authentication/verifyCode")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        session.setAttribute(VerifyCodeFilter.VERIFY_CODE, verifyCode);
        int w = 80, h = 32;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    }

}
