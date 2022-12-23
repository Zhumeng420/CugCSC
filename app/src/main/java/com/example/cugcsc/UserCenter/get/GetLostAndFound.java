package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.UserCenter.login.BasicApi.GetUserInfoByPhone.GetUserInfo;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.LostAndFoundData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetLostAndFound {
    public static Boolean  GetLostFound(List<LostAndFoundData> result) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from lost_and_found";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            LostAndFoundData temp=new LostAndFoundData();
            temp.type = rs.getBoolean("type");
            temp.describes= rs.getString("describes");
            temp.url=rs.getString("url");
            temp.url2=rs.getString("url2");
            temp.location=rs.getString("location");
            temp.time=rs.getString("time");
            temp.contract=rs.getString("contract");
            temp.phone=rs.getString("phone");
            temp.poster=rs.getString("poster");
            temp.picture=getURLimage(rs.getString("url"));
            temp.head=getURLimage(GetHeadURL(temp.poster));
            temp.username=GetName(temp.poster);
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
    public static Boolean  SearchLostFound(List<LostAndFoundData> result,String key) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from lost_and_found where describes like '%"+key+"%'";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            LostAndFoundData temp=new LostAndFoundData();
            temp.type = rs.getBoolean("type");
            temp.describes= rs.getString("describes");
            temp.url=rs.getString("url");
            temp.url2=rs.getString("url2");
            temp.location=rs.getString("location");
            temp.time=rs.getString("time");
            temp.contract=rs.getString("contract");
            temp.phone=rs.getString("phone");
            temp.poster=rs.getString("poster");
            temp.picture=getURLimage(rs.getString("url"));
            temp.head=getURLimage(GetHeadURL(temp.poster));
            temp.username=GetName(temp.poster);
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }

}
