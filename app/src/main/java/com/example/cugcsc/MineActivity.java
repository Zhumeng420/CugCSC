package com.example.cugcsc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout UserInfo;
    private ImageView UserHead;
    private TextView UserName;
    private TextView UserPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.mine);//默认选择主页
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
                        startActivity(new Intent(getApplicationContext(),MessageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mine:
                        return true;
                }
                return false;
            }
        });
        /**********登录/个人信息模块************/
        UserInfo=findViewById(R.id.user_info);
        UserInfo.setOnClickListener(this);//监听点击事件
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_info:{//个人信息模块被点击
                /*******登录状态判断*******/
                if(Objects.equals(GlobalUserState.UserPhone, "")){//如果用户没用登录，则跳转到登录界面
                    Intent intent=new Intent();
                    intent.setClass(MineActivity.this,LoginActivity.class);
                    startActivityForResult(intent,0x001);
                    break;
                }
            }
        }
    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x001&resultCode==0x002) {//登录完成后跳转回主页
            UserHead=findViewById(R.id.user_head);
            UserName=findViewById(R.id.user_name);
            UserPhone=findViewById(R.id.user_phone);
            UserName.setText(GlobalUserState.UserName);
            UserPhone.setText("账号："+GlobalUserState.UserPhone);
            Typeface type = Typeface.createFromAsset(getAssets(),"login.ttf" );//设置按钮字体
            UserName.setTypeface(type);
            //GetHeadByAsyc task=new GetHeadByAsyc(head);
            //task.execute(user.url);
        }
    }
}