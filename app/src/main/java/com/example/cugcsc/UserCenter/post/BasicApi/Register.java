package com.example.cugcsc.UserCenter.post.BasicApi;

import static com.example.cugcsc.database.DbConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Register {
    /**
     * 注册
     * @param phone
     * @param password
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean register(String phone,String password) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "insert into user(phone,password,head) value(?,?)";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        pstmt.setString(1,phone);
        pstmt.setString(2,password);
        pstmt.setString(3,"https://p1.ssl.qhimg.com/dr/270_500_/t013db695a8693421f3.jpg?size=311x304");
        int res=pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return res>0;
    }

    /**
     * 修改密码
     * @param phone
     * @param newPassword
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void changePassword(String phone,String newPassword) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "UPDATE user set password=? where phone=?" ;
        PreparedStatement psmt=null;
        psmt=conn.prepareStatement(sql);
        psmt.setString(1,newPassword);
        psmt.setString(2,phone);
        psmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    /**
     * 修改用户名
     * @param phone
     * @param newname
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void changeUsername(String phone,String newname) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "UPDATE user set name=? where phone=?" ;
        PreparedStatement psmt=null;
        psmt=conn.prepareStatement(sql);
        psmt.setString(1,newname);
        psmt.setString(2,phone);
        psmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
