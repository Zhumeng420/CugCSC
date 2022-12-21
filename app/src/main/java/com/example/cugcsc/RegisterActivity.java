package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.post.BasicApi.Register.register;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.SuccessToast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cugcsc.UserCenter.login.Async.SendCodeAsyn;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button GetCode;
    private String Phone;
    private EditText UserPhone;
    private String Code;
    private Handler mHandler = new Handler();
    public int T = 300;
    private Button Register;
    private EditText CodeInput;
    private EditText Password1;
    private EditText Password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        /*********绑定控件*************/
        UserPhone=findViewById(R.id.user_phone);
        GetCode=findViewById(R.id.get_code);
        GetCode.setOnClickListener(this);
        Register=findViewById(R.id.login);
        Register.setOnClickListener(this);
        CodeInput=findViewById(R.id.code);
        Password1=findViewById(R.id.password1);
        Password2=findViewById(R.id.password2);
        Password1.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
        Password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code: {
                Phone = UserPhone.getText().toString();
                if (Phone.length() != 11) {
                    ErrorToast(this, "请输入正确的手机号");
                } else {
                    SendCodeAsyn task = new SendCodeAsyn(this);
                    //执行异步任务
                    Code = String.format("%06d", new Random().nextInt(999999));//生成验证码
                    task.execute(Phone, Code);
                    /****************以下要设置时间间隔，禁用按钮，防止重复发送*****************/
                    new Thread(new MyCountDownTimer()).start();//开始执行
                }
                break;
            }
            case R.id.login:{
                String phone=UserPhone.getText().toString();
                if(!Objects.equals(Phone, phone)){
                    ErrorToast(this,"两次手机号码不一致");
                }else if(Code!=CodeInput.getText().toString()){
                    ErrorToast(this,"验证码错误");
                }else if(Password1.getText().toString().length()<8){
                    ErrorToast(this,"密码不得小于八位");
                }else if(!Password1.getText().toString().equals(Password2.getText().toString())){
                    ErrorToast(this,"两次密码不一致");
                }else{//向数据库插入账号
                    new Thread(() -> {
                        try {
                            register(UserPhone.getText().toString(), Password1.getText().toString());
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    SuccessToast(this,"注册成功");
                    finish();
                }
            }
        }
    }
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