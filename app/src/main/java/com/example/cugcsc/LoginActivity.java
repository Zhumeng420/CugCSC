package com.example.cugcsc;

import static com.example.cugcsc.tool.HttpUtils.UploadImage;
import static com.example.cugcsc.tool.toast.ErrorToast;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.cugcsc.UserCenter.login.Async.LoginAsyncTaskByPassword;

import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Login;
    private EditText Phone;
    private EditText Password;
    private TextView Register;
    private TextView CodeLogin;
    private ImageView SeePass;
    private Boolean seepass=false;

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
        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
        SeePass=findViewById(R.id.see_pass);
        SeePass.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:{//个人信息模块被点击
                /*******账号密码合法性校验*******/
                if(Phone.getText().toString().length()!=11){
                    ErrorToast(this,"手机号码格式错误");
                }else if(Password.getText().toString().length()<8){//密码长度必须大于8位
                    ErrorToast(this,"密码错误");
                }else {//否则到数据库查询
                    /***********以下到异步执行登录操作，同时加载动画****************/
                    LoginAsyncTaskByPassword task=new LoginAsyncTaskByPassword(this);
                    System.out.println(Phone.getText().toString());
                    task.execute(Phone.getText().toString(),Password.getText().toString());
                }
                break;
            }
            case R.id.code_login:{//验证码登录
                startActivity(new Intent(getApplicationContext(),CodeLoginActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.register:{//注册
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                overridePendingTransition(0,0);
                break;
            }
            case R.id.see_pass:{
                if (!seepass){
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    SeePass.setImageResource(R.drawable.novisable);
                    seepass=true;
                }else{
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
                    SeePass.setImageResource(R.drawable.seepas);
                    seepass=false;
                }
                break;
            }
        }
    }


}