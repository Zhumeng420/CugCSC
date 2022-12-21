package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.post.BasicApi.Register.changeUsername;
import static com.example.cugcsc.tool.toast.SuccessToast;
import static com.example.cugcsc.tool.toast.WarnToast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GetHeadByAsyc;
import com.example.cugcsc.UserCenter.GlobalUserState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.sql.SQLException;
import java.util.Objects;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout UserInfo;
    private ImageView UserHead;
    private TextView UserName;
    private TextView UserPhone;
    private RelativeLayout Post;
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
        /**********发布模块被点击************/
        Post=findViewById(R.id.post);
        Post.setOnClickListener(this);
        UserHead=findViewById(R.id.user_head);
        UserName=findViewById(R.id.user_name);
        UserPhone=findViewById(R.id.user_phone);
        UserName.setOnClickListener(this);
        UserHead.setOnClickListener(this);
        /*********设置用户名和头像**********/
        if(GlobalUserState.UserPhone!=""){
            UserName.setText(GlobalUserState.UserName);
            UserPhone.setText("账号："+GlobalUserState.UserPhone);
            Typeface type = Typeface.createFromAsset(getAssets(),"login.ttf" );//设置按钮字体
            UserName.setTypeface(type);
            GetHeadByAsyc task=new GetHeadByAsyc(UserHead);
            task.execute(GlobalUserState.URL);
        }
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
                break;
            }
            case R.id.post:{//发布被点击
                if (GlobalUserState.UserPhone == "") {//如果用户没有登录

                }else{
                    Intent intent=new Intent();
                    intent.setClass(MineActivity.this,PostActivity.class);//跳转到发布分区选择界面
                    startActivityForResult(intent,0x003);
                }
            }
            case R.id.user_name:{//修改用户名
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                final EditText edit = new EditText(this);
                builder.setView(edit);
                builder.setTitle("请输入新名称");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newname=edit.getText().toString();
                        GlobalUserState.UserName=newname;
                        //Toast.makeText(mine.this, "开始后台上传，请勿退出APP", Toast.LENGTH_SHORT).show();
                        new Thread(() -> {//更新数据库
                            try {
                                changeUsername(GlobalUserState.UserPhone,newname);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(1);//通知主线程更新控件
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WarnToast(MineActivity.this,"取消修改");
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
            case R.id.user_head:{//更换头像

            }
        }
    }

    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    SuccessToast(MineActivity.this,"修改成功");
                    UserName.setText(GlobalUserState.UserName);
                default :
                    break;
            }
        }

    };

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
            GetHeadByAsyc task=new GetHeadByAsyc(UserHead);
            task.execute(GlobalUserState.URL);
        }
    }
}