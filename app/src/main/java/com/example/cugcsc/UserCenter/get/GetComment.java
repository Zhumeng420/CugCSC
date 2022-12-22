package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.Comment;
import com.example.cugcsc.data.EmoData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetComment {
    public static Boolean  getcomment(List<Comment> result, String table,String id) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from comment where which_table='"+table+"' and source_id="+id;
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            Comment temp=new Comment();
            temp.name=GetName(rs.getString("phone"));
            temp.head=getURLimage(GetHeadURL(rs.getString("phone")));
            temp.date=rs.getDate("time");
            temp.content=rs.getString("context");
            temp.level=rs.getInt("which_level");
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
