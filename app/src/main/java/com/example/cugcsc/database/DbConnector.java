package com.example.cugcsc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接类
 */
public class DbConnector {

    private static String username="root";//用户名
    private static String pwd="zhumeng420+";//密码
    private static String driverClass="com.mysql.jdbc.Driver";//驱动类名    MySQL8.0以上的格式为“com.mysql.cj.jdbc.Diver” 版本查看方法：select version
    private static String url="jdbc:mysql://sh-cynosdbmysql-grp-m9djtzsg.sql.tencentcdb.com:20221/cugcsc?characterEncoding=utf-8&useSSL=false";//连接地址
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);//加载驱动//throws ClassNotFoundException, SQLException  后面报错alt+enter自动生成解决方案，不用敲
        Connection conn= DriverManager.getConnection(url,username,pwd);
        return conn;
    }
    //释放连接资源
    public static void release(Connection conn, Statement st, ResultSet rs) throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (st != null) {
            st.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}
