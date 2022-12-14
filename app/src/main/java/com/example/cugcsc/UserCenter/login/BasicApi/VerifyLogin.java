package com.example.cugcsc.UserCenter.login.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/*****登录校验类*****/
public class VerifyLogin {
    /**
     * 校验用户账号密码登录
     * @param phone
     * @param password
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean  VerifyPassword(String phone,String password) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select password from user where phone="+phone;
        ResultSet rs = stmt.executeQuery(sql);
        String pw="";
        while(rs.next()){
            pw=rs.getString("password");
            System.out.println(pw);
        }
        rs.close();
        stmt.close();
        conn.close();
        return Objects.equals(pw, password);
    }
}
