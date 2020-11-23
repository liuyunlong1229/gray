package com.rainbow.gray.framework.exception;

public class GrayException extends RuntimeException {
    private static final long serialVersionUID = 7975167663357170655L;

    public GrayException() {
        super();
    }

    public GrayException(String message) {
        super(message);
    }

    public GrayException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrayException(Throwable cause) {
        super(cause);
    }
}