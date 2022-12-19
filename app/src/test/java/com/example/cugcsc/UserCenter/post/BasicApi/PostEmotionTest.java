package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.UserCenter.post.BasicApi.PostEmotion.postEmo;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class PostEmotionTest {
    @Test
    public void postEmotest() throws SQLException, ClassNotFoundException {
        assertTrue(postEmo("15972026420","ggg","ssss"));
    }
}