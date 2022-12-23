
package com.example.cugcsc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MessageActivity extends AppCompatActivity implements  View.OnClickListener{
    private TextView Notice;
    private CardView Weather;
    private CardView Shudo;
    private CardView Paint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        /******绑定控件*********/
        Notice=findViewById(R.id.notice);//让通知滚动显示
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );//设置字体
        Notice.setTypeface(type);
        Notice.setMovementMethod(LinkMovementMethod.getInstance());
        Notice.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        Notice.setSingleLine(true);
        Notice.setSelected(true);
        Notice.setFocusable(true);
        Notice.setFocusableInTouchMode(true);
        Weather=findViewById(R.id.weather);
        Weather.setOnClickListener(this);
        Shudo=findViewById(R.id.shudu);
        Shudo.setOnClickListener(this);
        Paint=findViewById(R.id.paint);
        Paint.setOnClickListener(this);
        /********设置透明********/
        View v = findViewById(R.id.serach_view2);
        v.getBackground().setAlpha(200);//0~255透明度值 ，0为完全透明，255为不透明
        v=findViewById(R.id.content_view2);
        v.getBackground().setAlpha(160);
        v=findViewById(R.id.content_view3);
        v.getBackground().setAlpha(160);
        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.message);//默认选择主页
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
                        startActivity(new Intent(getApplicationContext(),LibraryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.message:
                        return true;
                    case R.id.mine:
                        startActivity(new Intent(getApplicationContext(),MineActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather:{
                Intent intent=new Intent(MessageActivity.this,WebShowActivity.class);
                intent.putExtra("url","https://www.qweather.com/");
                MessageActivity.this.startActivity(intent);
                break;
            }
            case R.id.shudu:{
                Intent intent=new Intent(MessageActivity.this,WebShowActivity.class);
                intent.putExtra("url","https://sudoku-cn.com/");
                MessageActivity.this.startActivity(intent);
                break;
            }
            case R.id.paint:{
                Intent intent=new Intent(MessageActivity.this,WebShowActivity.class);
                intent.putExtra("url","http://yige.baidu.com/");
                MessageActivity.this.startActivity(intent);
                break;
            }
        }
    }
}