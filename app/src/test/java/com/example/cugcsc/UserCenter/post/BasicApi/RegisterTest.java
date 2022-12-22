package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.UserCenter.post.BasicApi.Register.register;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class RegisterTest {

    @Test
    public void registertest() throws SQLException, ClassNotFoundException {
        assertTrue(register("15307181699","123456789"));
    }
}