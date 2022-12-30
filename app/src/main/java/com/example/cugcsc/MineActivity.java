package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.UserCenter.post.BasicApi.Register.changeHead;
import static com.example.cugcsc.UserCenter.post.BasicApi.Register.changeUsername;
import static com.example.cugcsc.tool.HttpUtils.UploadImage;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.SuccessToast;
import static com.example.cugcsc.tool.toast.WarnToast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GetHeadByAsyc;
import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.tool.FileoOperations;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout UserInfo;
    private RelativeLayout LogOut;
    private RelativeLayout Convention;
    private ImageView UserHead;
    private TextView UserName;
    private TextView UserPhone;
    private RelativeLayout Post;
    private String path;
    private String url;
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
        /**********模块被点击************/
        Post=findViewById(R.id.post);
        Post.setOnClickListener(this);
        UserHead=findViewById(R.id.user_head);
        UserName=findViewById(R.id.user_name);
        UserPhone=findViewById(R.id.user_phone);
        UserName.setOnClickListener(this);
        UserHead.setOnClickListener(this);
        LogOut=findViewById(R.id.logout);
        LogOut.setOnClickListener(this);
        Convention=findViewById(R.id.convention);
        Convention.setOnClickListener(this);
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
                if (Objects.equals(GlobalUserState.UserPhone, "")) {//如果用户没有登录
                    ErrorToast(this,"您尚未登录，请登录");
                }else{
                    Intent intent=new Intent();
                    intent.setClass(MineActivity.this,PostActivity.class);//跳转到发布分区选择界面
                    startActivityForResult(intent,0x003);
                }
                break;
            }
            case R.id.user_name:{//修改用户名
                if(GlobalUserState.UserPhone!=""){
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
                    break;
                }
            }
            case R.id.user_head:{//更换头像
                if(GlobalUserState.UserPhone!=""){
                    /******打开图片选择器**********/
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    this.startActivityForResult(intent, 0x005);
                    break;
                }
            }
            case R.id.logout:{
                if(GlobalUserState.UserPhone!=""){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("确定要退出登录？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileoOperations.verifyStoragePermissions(MineActivity.this);
                            String path=MineActivity.this.getExternalCacheDir().getAbsolutePath();
                            System.out.println(path);
                            path=path+"/cugcsc/user.txt";
                            File file = new File(path);
                            if(file.exists() && file.isFile())//删除本地记录
                            {
                                file.delete();
                            }
                            UserName.setText("未登录");
                            UserPhone.setText("");
                            UserHead.setImageResource(R.drawable.user);
                            GlobalUserState.UserPhone="";
                            GlobalUserState.UserName="";
                            GlobalUserState.URL="";
                            SuccessToast(MineActivity.this,"退出登录成功");
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WarnToast(MineActivity.this,"取消");
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    break;
                }
            }
            case R.id.convention:{
                startActivity(new Intent(getApplicationContext(),CoventionActivity.class));
                overridePendingTransition(0,0);
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
                    break;
                case 2:{
                    GlobalUserState.URL=url;//更新头像url
                    new Thread(() -> {
                        try {
                            changeHead(GlobalUserState.UserPhone,url);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);//通知主线程更新控件
                    }).start();
                    break;
                }
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
        } else if (requestCode == 0x005) {
            if (data == null) {
                // 用户未选择任何文件，直接返回
                return;
            }
            /******************以下根据Uri获取文件的真实路径********************************/
            Uri uri = data.getData(); // 获取用户选择文件的URI
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    path = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            //path = UriUtil.getPath(mine.this, uri);
            System.out.println("测试文件上传"+data.getData());
            System.out.println("测试路径"+path);
            UserHead.setImageURI(uri);//更新图像
            new Thread(() -> {//更新数据库
                url=UploadImage(path);
                handler.sendEmptyMessage(2);//通知主线程更新控件
            }).start();
            //mEditor.insertImage(ImageUrl.image_url, "dachshund");
        }
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}