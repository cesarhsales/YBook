package com.example.ybook.customexceptions;

/**
 * Exception class in case there are invalid characters in user input
 */
public class StringInvalidCharactersException extends Exception {
    public StringInvalidCharactersException(String errorMessage) {
        super(errorMessage);
    }
}
