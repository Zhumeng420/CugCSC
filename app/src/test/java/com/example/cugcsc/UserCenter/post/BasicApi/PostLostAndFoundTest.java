package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.UserCenter.post.BasicApi.PostLostAndFound.postLostFound;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class PostLostAndFoundTest {

    @Test
    public void postBlog() throws SQLException, ClassNotFoundException {
        assertTrue(postLostFound(false,"一个东西","www","www","图书馆四楼","2022-10-26 08:26","朱萌","15972026420","15972026420"));
    }
}