package com.example.cugcsc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class PostActivity extends AppCompatActivity  implements View.OnClickListener {
    private LinearLayout BackMine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
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
        /************返回个人中心***************/
        BackMine=findViewById(R.id.back_mine);
        BackMine.setOnClickListener(this);//监听点击事件
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_mine:{//返回按钮被点击
                /*******登录状态判断*******/
                if(Objects.equals(GlobalUserState.UserPhone, "")){
                    finish();//直接结束当前页面生命周期
                    break;
                }else{//否则跳转到个人中心编辑界面

                }
            }

        }
    }

}