package com.example.cugcsc.UserCenter.login.BasicApi;

import static com.example.cugcsc.UserCenter.login.BasicApi.GetUserInfoByPhone.GetUserInfo;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class GetUserInfoByPhoneTest {

    @Test
    public void getUserInfo() throws SQLException, ClassNotFoundException {
        assertTrue(GetUserInfo("15972026420"));
    }
}