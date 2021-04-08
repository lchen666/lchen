package com.lchen.common.core.exception;

public class RsaException extends RuntimeException{

    private static final long serialVersionUID = 4662004651540778788L;

    public RsaException() {
    }

    public RsaException(String message) {
        super(message);
    }

    public RsaException(String message, Throwable cause) {
        super(message, cause);
    }
}
