package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PostEmotion {
    public static boolean postEmo(String phone,String title,String content) throws SQLException, ClassNotFoundException {
        Connection conn=getConnection();
        String sql = "insert into emotion(phone,title,content,post_time,visit_nums) values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, phone);
        pstmt.setString(2, title);
        pstmt.setString(3, content);
        pstmt.setTimestamp(4, new Timestamp(new Date().getTime()));
        pstmt.setInt(5,0);
        pstmt.addBatch();
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        return true;
    }
}
