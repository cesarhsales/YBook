package com.example.ybook.customexceptions;

/**
 * Exception class in case input string is too long
 */
public class StringLengthException extends Exception {
    public StringLengthException(String errorMessage) {
        super(errorMessage);
    }
}
