package com.student.exception;

import org.springframework.security.core.AuthenticationException;

public class VerificationException extends AuthenticationException {
    public VerificationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public VerificationException(String msg) {
        super(msg);
    }
}
