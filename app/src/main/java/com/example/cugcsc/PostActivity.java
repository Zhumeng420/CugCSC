package com.example.cugcsc;

import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.SuccessToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.data.PostType;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class PostActivity extends AppCompatActivity  implements View.OnClickListener {
    private LinearLayout BackMine;
    private CardView Emotion;
    private CardView LostAndFound;
    private CardView SchoolQuestion;
    private CardView InterestCommunicate;
    private CardView StduyCommunitcate;
    //请求码，自己设置，保证 > 0并且不冲突
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    //要申请的权限（可以一次申请多个权限），存放到数组里
    private static final String permission[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        /***********绑定各分区的跳转*************/
        Emotion=findViewById(R.id.emotion_communicate);
        Emotion.setOnClickListener(this);
        LostAndFound=findViewById(R.id.lost_and_found);
        LostAndFound.setOnClickListener(this);
        SchoolQuestion=findViewById(R.id.school_question);
        SchoolQuestion.setOnClickListener(this);
        InterestCommunicate=findViewById(R.id.interest_communicate);
        InterestCommunicate.setOnClickListener(this);
        StduyCommunitcate=findViewById(R.id.stduy_communitcate);
        StduyCommunitcate.setOnClickListener(this);
        /**********申请手机权限******************/
        requestPermission(this);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_mine:{//返回按钮被点击
                finish();//直接结束当前页面生命周期
                break;
            }
            case R.id.emotion_communicate:{//当情感交流分区被点击
                PostType.type=2;
                startActivity(new Intent(getApplicationContext(),EmotionActivity.class));//直接覆盖当前界面（减小内存开销）
                overridePendingTransition(0,0);
                break;
            }
            case R.id.school_question:{//校园咨询
                PostType.type=1;
                startActivity(new Intent(getApplicationContext(),EmotionActivity.class));//直接覆盖当前界面（减小内存开销）
                overridePendingTransition(0,0);
                break;
            }
            case R.id.interest_communicate:{//兴趣交流
                PostType.type=3;
                startActivity(new Intent(getApplicationContext(),EmotionActivity.class));//直接覆盖当前界面（减小内存开销）
                overridePendingTransition(0,0);
                break;
            }
            case R.id.stduy_communitcate:{//学习交流
                PostType.type=4;
                startActivity(new Intent(getApplicationContext(),EmotionActivity.class));//直接覆盖当前界面（减小内存开销）
                overridePendingTransition(0,0);
                break;
            }
            case R.id.lost_and_found:{//失物招领
                startActivity(new Intent(getApplicationContext(),LostAndFoundActivity.class));//直接覆盖当前界面（减小内存开销）
                overridePendingTransition(0,0);
                break;
            }
        }
    }
    /*******************以下是权限动态权限获取**************************/
    private void requestPermission(Context context){
        // 通过api判断手机当前版本号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 安卓11，判断有没有“所有文件访问权限”权限
            if (Environment.isExternalStorageManager()) {
                //SuccessToast(this,"获取到文件访问权限");
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                startActivityForResult(intent,5555);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //SuccessToast(this,"获取到文件访问权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6666);
            }
        } else {
            //SuccessToast(this,"获取到文件访问权限");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6666) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                SuccessToast(this,"获取到文件访问权限");
            } else {
                ErrorToast(this,"文件访问权限获取失败");
            }
        }
    }
}