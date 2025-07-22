package com.jonas.visitflow.exception;

public class LinkAlreadyUsedException extends RuntimeException {
    public LinkAlreadyUsedException(String message) {
        super(message);
    }
}
