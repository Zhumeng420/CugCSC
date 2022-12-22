package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AddNums {
    public static void addVisitNums(int id,String table) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "UPDATE "+table+" set visit_nums=visit_nums+1 where id=?" ;
        PreparedStatement psmt=null;
        psmt=conn.prepareStatement(sql);
        psmt.setInt(1,id);
        psmt.executeUpdate();
        stmt.close();
        conn.close();
    }

}
