package com.example.cugcsc;

import static com.example.cugcsc.tool.toast.WarnToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LibraryActivity extends AppCompatActivity  implements  View.OnClickListener{
    private CardView MusicCenter;
    private CardView MovieCenter;
    private CardView BookCard;
    private CardView CodeCenter;
    private CardView DataCenter;
    private CardView PaperCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.library);//默认选择主页
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.library:
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
        /********搜索按钮更改字体*********/
        Button SearchButton=findViewById(R.id.search_button2);
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );//设置按钮字体
        SearchButton.setTypeface(type);
        /*********设置背景图片透明度*********/
        View v = findViewById(R.id.main_background2);
        v.getBackground().setAlpha(200);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.serach_view2);
        v.getBackground().setAlpha(0);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.search_context2);
        v.getBackground().setAlpha(160);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.search_button2);
        v.getBackground().setAlpha(40);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.content_view2);
        v.getBackground().setAlpha(160);//0~255透明度值 ，0为完全透明，255为不透明
        /*******绑定点击事件***********/
        MusicCenter=findViewById(R.id.music_center);
        MusicCenter.setOnClickListener(this);
        MovieCenter=findViewById(R.id.movie_center);
        MovieCenter.setOnClickListener(this);
        BookCard=findViewById(R.id.book_card);
        BookCard.setOnClickListener(this);
        CodeCenter=findViewById(R.id.code_center);
        CodeCenter.setOnClickListener(this);
        DataCenter=findViewById(R.id.data_center);
        DataCenter.setOnClickListener(this);
        PaperCenter=findViewById(R.id.paper_center);
        PaperCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_center:{
                startActivity(new Intent(getApplicationContext(),MusciCenterActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.movie_center:{
                startActivity(new Intent(getApplicationContext(),MovieCenterActivity.class));
                overridePendingTransition(0,0);
                break;
            }case R.id.book_card:{
                startActivity(new Intent(getApplicationContext(),BookShowActivity.class));
                overridePendingTransition(0,0);
                break;
            }case R.id.code_center:{
                WarnToast(this,"尚未开放，敬请期待");
                break;
            } case R.id.data_center:{
                WarnToast(this,"尚未开放，敬请期待");
                break;
            } case R.id.paper_center:{
                WarnToast(this,"尚未开放，敬请期待");
                break;
            }
        }
    }
}