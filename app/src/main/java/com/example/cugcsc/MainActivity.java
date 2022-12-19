package com.example.cugcsc;

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

import com.example.cugcsc.data.PostType;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


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