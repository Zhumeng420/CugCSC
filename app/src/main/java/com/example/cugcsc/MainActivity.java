package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.UserCenter.post.BasicApi.Register.register;
import static com.example.cugcsc.tool.toast.SuccessToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.UserCenter.login.Async.LoginAsyncTaskByPassword;
import com.example.cugcsc.data.PostType;
import com.example.cugcsc.tool.FileoOperations;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    ImageView LostCenter;
    ImageView EmotionCenter;
    ImageView StudyCenter;
    ImageView QuestionCenter;
    ImageView InterestCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /********搜索按钮更改字体*********/
        Button SearchButton=findViewById(R.id.search_button);
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );//设置按钮字体
        SearchButton.setTypeface(type);
        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.home);//默认选择主页
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.library:
                        startActivity(new Intent(getApplicationContext(),LibraryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(),MessageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mine:
                        startActivity(new Intent(getApplicationContext(),MineActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        /*********设置背景图片透明度*********/
        View v = findViewById(R.id.main_background);
        v.getBackground().setAlpha(200);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.serach_view);
        v.getBackground().setAlpha(0);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.search_context);
        v.getBackground().setAlpha(160);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.search_button);
        v.getBackground().setAlpha(40);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.content_view);
        v.getBackground().setAlpha(160);//0~255透明度值 ，0为完全透明，255为不透明
        /********绑定事件****************/
        LostCenter=findViewById(R.id.lost_center);
        LostCenter.setOnClickListener(this);
        EmotionCenter=findViewById(R.id.emotion_center);
        EmotionCenter.setOnClickListener(this);
        StudyCenter=findViewById(R.id.study_center);
        StudyCenter.setOnClickListener(this);
        QuestionCenter=findViewById(R.id.question_center);
        QuestionCenter.setOnClickListener(this);
        InterestCenter=findViewById(R.id.interest_center);
        InterestCenter.setOnClickListener(this);
        /********单点登录************/
        if(!FileoOperations.isFolderExists("/storage/emulated/0/cugcsc")){
            FileoOperations.makeDirectory("/storage/emulated/0/cugcsc");//创建文件夹
        }
        //判断文件是否存在
        File file = new File("/storage/emulated/0/cugcsc/user.txt");
        ArrayList info=new ArrayList<String>();//读取账号密码信息
        if(file.exists() && file.isFile())//如果本地有记录，则直接登录
        {
            /*****从文件中读取账号密码********/
            InputStream instream = null;
            try {
                instream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (instream != null)
            {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line="";
                //分行读取
                while (true) {
                    try {
                        if (!(( line = buffreader.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    info.add(line);
                }
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*****登录*********/
            GlobalUserState.UserPhone=info.get(0).toString();
            new Thread(() -> {
                try {
                    GlobalUserState.UserName=GetName(info.get(0).toString());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    GlobalUserState.URL=GetHeadURL(info.get(0).toString());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
            SuccessToast(this,"自动登录成功");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lost_center:{//失物招领分区
                startActivity(new Intent(getApplicationContext(),LostFoundShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.emotion_center:{
                PostType.type=2;
                startActivity(new Intent(getApplicationContext(),EmotionShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.study_center:{
                PostType.type=4;
                startActivity(new Intent(getApplicationContext(),EmotionShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.interest_center:{
                PostType.type=3;
                startActivity(new Intent(getApplicationContext(),EmotionShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.question_center:{
                PostType.type=1;
                startActivity(new Intent(getApplicationContext(),EmotionShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }
        }
    }
}