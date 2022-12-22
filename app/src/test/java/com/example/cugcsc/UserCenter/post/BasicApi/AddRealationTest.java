package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.UserCenter.post.BasicApi.AddRealation.postComment;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class AddRealationTest {

    @Test
    public void postCommenttest() throws SQLException, ClassNotFoundException {
        assertTrue(postComment("study","很有帮助，感谢分享","18971236061",1,1));
    }
}