package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.database.DbConnector.getConnection;

import com.example.cugcsc.UserCenter.GlobalUserState;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetHeadOrNameByPhone {
    public static String GetName(String phone) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select name from user where phone="+phone;
        ResultSet rs = stmt.executeQuery(sql);
        String name=null;
        while(rs.next()){
            name=rs.getString("name");
        }
        rs.close();
        stmt.close();
        conn.close();
        return name;
    }
    public static String GetHeadURL(String phone) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select head from user where phone="+phone;
        ResultSet rs = stmt.executeQuery(sql);
        String head=null;
        while(rs.next()){
            head=rs.getString("head");
        }
        rs.close();
        stmt.close();
        conn.close();
        return head;
    }
}
