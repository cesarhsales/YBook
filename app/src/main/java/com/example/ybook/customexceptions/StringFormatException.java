package com.example.ybook.customexceptions;

public class StringFormatException extends Exception {
    public StringFormatException(String errorMessage) {
        super(errorMessage);
    }
}
