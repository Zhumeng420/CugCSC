package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AddRealation {
    public static boolean postComment(String table,String context,String phone,int level,int source) throws SQLException, ClassNotFoundException {
        Connection conn=getConnection();
        String sql = "insert into comment(which_table,context,source_id,which_level,time,phone) values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, table);
        pstmt.setString(2, context);
        pstmt.setInt(3, source);
        pstmt.setInt(4, level);
        pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
        pstmt.setString(6,phone);
        pstmt.addBatch();
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        return true;
    }
}
