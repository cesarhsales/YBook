package com.example.ybook;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.customexceptions.StringInvalidCharactersException;
import com.example.ybook.customexceptions.StringLengthException;
import com.example.ybook.util.StringValidation;

import org.junit.Test;

public class UserAuthenticationTest {
    @Test
    public void emailFormatShouldMatch() throws StringFormatException {
        String email = "test@tes.com";
        StringValidation.isValidEmail(email);
    }

   @Test(expected = StringFormatException.class)
    public void emailFormatShouldNotMatch() throws StringFormatException {
        String email = "daviddavid.com";
        StringValidation.isValidEmail(email);
   }

   @Test
    public void passwordLengthShouldBe() throws StringLengthException {
        String password = "12345678";
        StringValidation.isValidPasswordLength(password);
   }

    @Test(expected = StringLengthException.class)
    public void passwordLengthShouldNotBe() throws StringLengthException {
        String password = "12348";
        StringValidation.isValidPasswordLength(password);
    }

    @Test
    public void usernameCharactersAreValid() throws StringInvalidCharactersException {
        String username = "cesarsales";
        StringValidation.isValidUsername(username);
    }

    @Test(expected = StringInvalidCharactersException.class)
    public void usernameCharactersAreInvalid() throws StringInvalidCharactersException {
        String username = "select * from Users;";
        StringValidation.isValidUsername(username);
    }
}
