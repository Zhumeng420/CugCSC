package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetMovie {
    public static Boolean getMovie(List<Movie> result) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from movie";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            Movie temp=new Movie();
            temp.title = rs.getString("title");
            temp.url= rs.getString("url");
            temp.picture=getURLimage(rs.getString("picture"));
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
