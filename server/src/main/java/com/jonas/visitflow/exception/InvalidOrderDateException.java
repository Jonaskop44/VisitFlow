package com.jonas.visitflow.exception;

public class InvalidOrderDateException extends RuntimeException {
    public InvalidOrderDateException(String message) {
        super(message);
    }
}
