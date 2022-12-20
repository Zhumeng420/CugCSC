package com.example.cugcsc.UserCenter.get;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.database.DbConnector.getConnection;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.Music;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GetMusic {
    public static Boolean  getMusic(List<Music> result) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from music";
        ResultSet rs = stmt.executeQuery(sql);
        //rs.beforeFirst();
        while(rs.next()){
            Music temp=new Music();
            temp.song = rs.getString("song");
            temp.singer= rs.getString("singer");
            temp.picture=rs.getString("picture");
            temp.url=rs.getString("url");
            temp.pic=getURLimage(rs.getString("picture"));
            temp.lrc=rs.getString("lrc");
            result.add(temp);
        }
        rs.close();
        stmt.close();
        conn.close();
        return true;
    }
}
