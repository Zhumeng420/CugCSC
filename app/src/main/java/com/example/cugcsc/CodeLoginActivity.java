package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetHeadURL;
import static com.example.cugcsc.UserCenter.get.GetHeadOrNameByPhone.GetName;
import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.tool.toast.ErrorToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.UserCenter.login.Async.LoginAsyncTaskByPassword;
import com.example.cugcsc.UserCenter.login.Async.SendCodeAsyn;
import com.example.cugcsc.tool.toast;

import java.sql.SQLException;
import java.util.Random;

public class CodeLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button GetCode;
    private String Phone;
    private String Code;
    private EditText UserPhone;
    private EditText UserPassword;
    private Button Login;
    private Handler mHandler = new Handler();
    public int T = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        /**********绑定控件************/
        GetCode=findViewById(R.id.get_code);
        GetCode.setOnClickListener(this);
        UserPhone=findViewById(R.id.user_phone);
        UserPassword=findViewById(R.id.user_password);
        Login=findViewById(R.id.login);
        Login.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_code:{
                Phone=UserPhone.getText().toString();
                if(Phone.length()!=11){
                    ErrorToast(this,"请输入正确的手机号");
                }else{
                    SendCodeAsyn task=new SendCodeAsyn(this);
                    //执行异步任务
                    Code=String.format("%06d",new Random().nextInt(999999));//生成验证码
                    task.execute(Phone,Code);
                    /****************以下要设置时间间隔，禁用按钮，防止重复发送*****************/
                    new Thread(new MyCountDownTimer()).start();//开始执行
                }
                break;
            }
            case R.id.login:{
                String phone2=UserPhone.getText().toString();
                String code=UserPassword.getText().toString();
                System.out.println(code);
                System.out.println(Code);
                if(!phone2.equals(Phone)){
                    ErrorToast(this,"两次手机号不一致");
                }else if(!code.equals(Code)){
                    ErrorToast(this,"验证码错误");
                }else{
                    /*******获取头像和名称********/
                    new Thread(() -> {
                        GlobalUserState.UserPhone=Phone;
                        try {
                            GlobalUserState.UserName=GetName(Phone);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    new Thread(() -> {
                        try {
                            GlobalUserState.URL=GetHeadURL(Phone);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    /******跳转到个人中心********/
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                }
            }
        }
    }
    /**
     * 自定义倒计时类，实现Runnable接口
     */
    class MyCountDownTimer implements Runnable{

        @Override
        public void run() {
            //倒计时开始，循环
            while (T > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        GetCode.setEnabled(false);
                        GetCode.setText(T + "秒后重新开始");
                    }
                });
                try {
                    Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                T--;
            }
            //倒计时结束，也就是循环结束
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    GetCode.setEnabled(true);
                    GetCode.setText("倒计时开始");
                }
            });
            T = 10; //最后再恢复倒计时时长
        }
    }
}