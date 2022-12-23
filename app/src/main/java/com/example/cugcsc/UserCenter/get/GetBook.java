package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.Book;
import com.example.cugcsc.data.EmoData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetBook {
    public static Boolean  getBook(List<Book> result) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from book";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            Book temp=new Book();
            temp.title = rs.getString("title");
            temp.author=rs.getString("author");
            temp.url=rs.getString("url");
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
