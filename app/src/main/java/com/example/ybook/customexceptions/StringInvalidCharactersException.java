package com.example.ybook.customexceptions;

public class StringInvalidCharactersException extends Exception {
    public StringInvalidCharactersException(String errorMessage) {
        super(errorMessage);
    }
}
