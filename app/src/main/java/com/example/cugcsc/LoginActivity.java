package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GlobalUserState;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button Login;
    EditText Phone;
    EditText Password;
    TextView Register;
    TextView CodeLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*********设置背景图片透明度*********/
        View v = findViewById(R.id.login_backgorund1);
        v.getBackground().setAlpha(190);//0~255透明度值 ，0为完全透明，255为不透明
        v = findViewById(R.id.login_backgorund2);
        v.getBackground().setAlpha(190);
        v = findViewById(R.id.login_backgorund8);
        v.getBackground().setAlpha(190);
        /********设置字体**************/
        TextView welcome=findViewById(R.id.login_welcome);//欢迎字体
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );
        welcome.setTypeface(type);
        TextView login=findViewById(R.id.login);//登录按钮字体
        type = Typeface.createFromAsset(getAssets(),"search.otf" );
        login.setTypeface(type);
        /******登录按钮监听点击事件*******/
        Login=findViewById(R.id.login);
        Login.setOnClickListener(this);
        /*****绑定账号、密码、验证码登录、注册用户点击事件**********/
        Phone=findViewById(R.id.user_phone);
        Password=findViewById(R.id.user_password);
        Register=findViewById(R.id.register);
        CodeLogin=findViewById(R.id.code_login);
        Phone.setOnClickListener(this);
        Password.setOnClickListener(this);
        Register.setOnClickListener(this);
        CodeLogin.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:{//个人信息模块被点击
                /*******账号密码合法性校验*******/
                if(Phone.getText().toString().length()!=11){
                    Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}