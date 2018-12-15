package com.example.ybook.customexceptions;

/**
 * Exception class in case the input format is incorrect
 */
public class StringFormatException extends Exception {
    public StringFormatException(String errorMessage) {
        super(errorMessage);
    }
}
