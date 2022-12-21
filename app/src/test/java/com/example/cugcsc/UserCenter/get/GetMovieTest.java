package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetMovie.getMovie;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import com.example.cugcsc.data.Movie;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMovieTest {
    @Test
    public void testGetMovie() throws SQLException, ClassNotFoundException {
        List<Movie> mlist=new ArrayList<>();
        getMovie(mlist);
        System.out.println(mlist.size());
        for(int i=0;i<mlist.size();i++){
            System.out.println(mlist.get(i).title);
        }
        assertTrue(true);
    }
}