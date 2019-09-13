package com.study.exception;

import org.springframework.security.core.AuthenticationException;

public class VerifyCodeWrongException extends AuthenticationException {
    public VerifyCodeWrongException(String msg) {
        super(msg);
    }
}
