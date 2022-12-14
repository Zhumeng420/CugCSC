package com.example.cugcsc.UserCenter.login.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import com.example.cugcsc.UserCenter.GlobalUserState;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetUserInfoByPhone {
    /**
     * 将获取到的用户信息存储到全局用户信息重
     * @return
     */
    public static boolean GetUserInfo(String phone) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from user where phone="+phone;
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            GlobalUserState.UserName=rs.getString("name");
            GlobalUserState.UserPhone=rs.getString("phone");
            GlobalUserState.URL=rs.getString("head");
        }
        System.out.println(GlobalUserState.UserName);
        System.out.println(GlobalUserState.UserPhone);
        System.out.println(GlobalUserState.URL);
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
