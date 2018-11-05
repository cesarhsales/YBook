package com.example.ybook;

import org.junit.Assert;
import org.junit.Test;

public class LoginActivityTest {
    @Test
    public void emailFormatShouldMatch() throws StringFormatException {
        String email = "david@david.com";
        Assert.assertTrue(StringValidation.isValidEmail(email));
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


}
