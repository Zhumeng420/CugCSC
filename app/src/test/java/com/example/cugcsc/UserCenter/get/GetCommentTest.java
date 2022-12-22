package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetComment.getcomment;
import static org.junit.Assert.*;

import com.example.cugcsc.data.Comment;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetCommentTest {

    @Test
    public void getcommentTest() throws SQLException, ClassNotFoundException {
        List<Comment> mlist=new ArrayList<>();
        Boolean back=getcomment(mlist, "study","1");
        for(int i=0;i<mlist.size();i++){
            System.out.println(mlist.get(i).content);
        }
        assertTrue(back);
    }
}