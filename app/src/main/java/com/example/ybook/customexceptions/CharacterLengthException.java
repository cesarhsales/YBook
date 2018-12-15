package com.example.ybook.customexceptions;

/**
 * Exception class in case user input is longer than it should
 */
public class CharacterLengthException extends Exception {
    public CharacterLengthException(String errorMessage) {
        super(errorMessage);
    }
}
