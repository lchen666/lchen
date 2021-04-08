package com.lchen.common.core.exception;

public class PasswordException extends RuntimeException {
    private static final long serialVersionUID = -3924220709786144773L;

    public PasswordException() {
    }

    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}