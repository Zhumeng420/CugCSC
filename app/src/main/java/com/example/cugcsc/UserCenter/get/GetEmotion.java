package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.LostAndFoundData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetEmotion {
    public static Boolean  getEmotion(List<EmoData> result,String table) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from "+table;
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            EmoData temp=new EmoData();
            temp.title = rs.getString("title");
            temp.content= rs.getString("content");
            temp.post_time=rs.getTimestamp("post_time");
            temp.visit_nums=rs.getInt("visit_nums");
            temp.name=GetName(rs.getString("phone"));
            temp.head=getURLimage(GetHeadURL(rs.getString("phone")));
            temp.id=rs.getInt("id");
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
    public static Boolean  getEmotionSearch(List<EmoData> result,String table,String key) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from "+table+" where title like '%"+key+"%'";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            EmoData temp=new EmoData();
            temp.title = rs.getString("title");
            temp.content= rs.getString("content");
            temp.post_time=rs.getTimestamp("post_time");
            temp.visit_nums=rs.getInt("visit_nums");
            temp.name=GetName(rs.getString("phone"));
            temp.head=getURLimage(GetHeadURL(rs.getString("phone")));
            temp.id=rs.getInt("id");
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
