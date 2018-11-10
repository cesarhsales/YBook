package com.example.ybook.customexceptions;

public class StringLengthException extends Exception {
    public StringLengthException(String errorMessage) {
        super(errorMessage);
    }
}
