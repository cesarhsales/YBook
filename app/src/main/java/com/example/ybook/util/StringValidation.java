package com.example.ybook.util;

import com.example.ybook.customexceptions.CharacterLengthException;
import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.customexceptions.StringInvalidCharactersException;
import com.example.ybook.customexceptions.StringLengthException;

import java.util.regex.Pattern;

public  class StringValidation {
    private static final Integer PASSWORD_LENGTH = 8;
    private static final Integer CHAR_LENGTH = 200;

    public static void isValidUsername(String username) throws StringInvalidCharactersException {
        String usernameInvalidCharacters = ".*[\\s!=#$%^&*(),;?\"':{}|<>].*";

        //if there are invalid characters in the string we want to throw exception
        if (testStringPatternMatch(usernameInvalidCharacters, username))
            throw new StringInvalidCharactersException(
                    "Username allowed characters: 0-9, a-zA-Z and \".\"");
    }

    public static void isValidEmail(String email) throws StringFormatException {
        String emailFormat = "^(.+)@(.+)$";

        //if the format matches we don't want to throw exception, that's why we deny "!"
        if (!testStringPatternMatch(emailFormat, email))
            throw new StringFormatException("Invalid email format");
    }

    public static void isValidPasswordLength (String password) throws StringLengthException {
        if (password.length() < PASSWORD_LENGTH)
            throw new StringLengthException("Password must contain at least 8 characters");
    }

    private static boolean testStringPatternMatch(String rule, String input) {
        Pattern pattern = Pattern.compile(rule);
        return pattern.matches(rule, input);
    }

    public static void isValidCharacterCount(String string) throws CharacterLengthException {
        if(string.length() > CHAR_LENGTH) {
            throw new CharacterLengthException("Maximum character limit allowed is 200.");
        }
    }
}
