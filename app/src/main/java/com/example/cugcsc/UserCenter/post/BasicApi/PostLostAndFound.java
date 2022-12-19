package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PostLostAndFound {
    public static boolean postLostFound(Boolean type,String describe,String url,String url2,String location,String time,
                                   String contract,String phone,String poster) throws SQLException, ClassNotFoundException {
        Connection conn=getConnection();
        String sql = "insert into lost_and_found(type,describes,url,url2," +
                "location,time,contract,phone,posttime,poster) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, type);
        pstmt.setString(2, describe);
        pstmt.setString(3, url);
        pstmt.setString(4, url2);
        pstmt.setString(5, location);
        pstmt.setString(6, time);
        pstmt.setString(7, contract);
        pstmt.setString(8, phone);
        pstmt.setTimestamp(9,  new Timestamp(new Date().getTime()));
        pstmt.setString(10, poster);
        pstmt.addBatch();
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        return true;
    }
}
