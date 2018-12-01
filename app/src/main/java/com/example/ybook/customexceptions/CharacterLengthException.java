package com.example.ybook.customexceptions;

public class CharacterLengthException extends Exception {
    public CharacterLengthException(String errorMessage) {
        super(errorMessage);
    }
}
