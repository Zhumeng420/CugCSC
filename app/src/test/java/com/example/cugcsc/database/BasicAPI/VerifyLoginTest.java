package com.example.cugcsc.database.BasicAPI;

import static com.example.cugcsc.UserCenter.login.BasicApi.VerifyLogin.VerifyPassword;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class VerifyLoginTest {

    @Test
    public void verifyPassword() throws SQLException, ClassNotFoundException {
        String phone="15972026420";
        String password="123456789";
        assertTrue(VerifyPassword(phone,password));
    }
}