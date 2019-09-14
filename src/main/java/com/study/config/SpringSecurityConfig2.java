package com.study.config;

import com.study.authentication.MyAuthenticationFailureHandler;
import com.study.authentication.MyAuthenticationSuccessHandler;
import com.study.filter.VerifyCodeFilter;
import com.study.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableConfigurationProperties(SecurityProperties.class)
public class SpringSecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        VerifyCodeFilter validateCodeFilter = new VerifyCodeFilter();
        validateCodeFilter.setMyAuthenticationFailureHandler(myAuthenticationFailureHandler);

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/form")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/authentication/logout")
                .logoutSuccessUrl(securityProperties.getBrowser().getLoginPage())
                .and()
                .authorizeRequests()
                .antMatchers("/authentication/require", "/authentication/verifyCode",  securityProperties.getBrowser().getLoginPage()).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable();
    }
}
