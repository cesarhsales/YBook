package com.example.ybook;

import java.util.regex.Pattern;

public  class StringValidation {

    public static boolean isValidEmail(String email) throws StringFormatException {
        boolean isValid = false;
        String emailFormat = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailFormat);
        isValid = pattern.matches(emailFormat, email);
        if (isValid == false)
            throw new StringFormatException("Incorrect email format");
        return isValid;
    }

    public static boolean isValidPasswordLength (String password) throws StringLengthException {
          if (password.length() < 8)
            throw new StringLengthException("Password must contain at least 8 characters");
        return true;
    }
}
