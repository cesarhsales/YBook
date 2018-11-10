package com.example.ybook;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.customexceptions.StringInvalidCharactersException;
import com.example.ybook.customexceptions.StringLengthException;
import com.example.ybook.util.StringValidation;

import org.junit.Assert;
import org.junit.Test;

public class UserAuthenticationTest {
    @Test
    public void emailFormatIsValid() throws StringFormatException {
        String email = "test@test.com";
        StringValidation.isValidEmail(email);
    }

   @Test(expected = StringFormatException.class)
    public void emailFormatIsInvalid() throws StringFormatException {
        String email = "test.com";
        StringValidation.isValidEmail(email);
   }

   @Test
    public void passwordLengthIsValid() throws StringLengthException {
        String password = "12345678";
        StringValidation.isValidPasswordLength(password);
   }

    @Test(expected = StringLengthException.class)
    public void passwordLengthIsInvalid() throws StringLengthException {
        String password = "12348";
        StringValidation.isValidPasswordLength(password);
    }

    @Test
    public void usernameIsValid() throws StringInvalidCharactersException {
        String username = "crazytaxi";
        StringValidation.isValidUsername(username);
    }

    @Test(expected = StringInvalidCharactersException.class)
    public void usernameIsInvalid() throws StringInvalidCharactersException {
        String username = "test!";
        StringValidation.isValidUsername(username);
    }

    @Test
    public void passwordsMatch() {
        String password = "aTestPassword";
        String confirmPassword = "aTestPassword";

        Assert.assertTrue(password.equals(confirmPassword));
    }

    @Test
    public void passwordsDontMatch() {
        String password = "aTestPassword";
        String confirmPassword = "testPassword";

        Assert.assertFalse(password.equals(confirmPassword));
    }
}
