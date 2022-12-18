package com.example.cugcsc;

import static com.example.cugcsc.tool.HttpUtils.UploadImage;
import static com.example.cugcsc.tool.toast.SuccessToast;
import static com.example.cugcsc.tool.toast.WarnToast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.UserCenter.post.Async.PostLostAndFoundByAsync;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LostAndFoundActivity extends AppCompatActivity implements View.OnClickListener {
    CardView Describe;
    CardView Photo;
    CardView PeopleInfo;
    Boolean Flag;
    Button Lost;
    Button Find;
    ImageView AddPicture;
    ImageView AddPicture2;
    String path;
    String url;
    String url2;
    TextView PickTime;
    TextView LostPlace;
    TextView ContractPeople;
    TextView PeopleCall;
    TextView LostDescribe;
    Button LostPost;
    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;
    private EditText editText;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        /**************卡片设置阴影***********/
        Describe=findViewById(R.id.describe_obj);
        Describe.setCardElevation(8);//设置阴影部分大小
        Describe.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        Photo=findViewById(R.id.photo);
        Photo.setCardElevation(8);//设置阴影部分大小
        Photo.setContentPadding(5,5,5,5);//设置图片距离阴影大小
//        TypeChoose=findViewById(R.id.type_choose);
//        TypeChoose.setCardElevation(8);//设置阴影部分大小
//        TypeChoose.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        PeopleInfo=findViewById(R.id.people_info);
        PeopleInfo.setCardElevation(8);//设置阴影部分大小
        PeopleInfo.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        /********绑定点击事件********/
        Flag=true;
        Lost=findViewById(R.id.lost);
        Lost.setOnClickListener(this);
        Find=findViewById(R.id.find);
        Find.setOnClickListener(this);
        AddPicture=findViewById(R.id.add_picture);
        AddPicture.setOnClickListener(this);
        AddPicture2=findViewById(R.id.add_picture2);
        AddPicture2.setOnClickListener(this);
        PickTime=findViewById(R.id.pick_time);
        PickTime.setOnClickListener(this);
        PickTime.setInputType(InputType.TYPE_NULL);
        LostPlace=findViewById(R.id.lost_place);
        ContractPeople=findViewById(R.id.lost_place);
        PeopleCall=findViewById(R.id.phone_call);
        LostPost=findViewById(R.id.lost_submit);
        LostPost.setOnClickListener(this);
        LostDescribe=findViewById(R.id.lost_describe);
        /*************文件读写权限*************/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
                Environment.isExternalStorageManager()
        ) {
            SuccessToast(this,"获取到文件访问权限");
        }
        /************日期时间选择对话框**************/
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        dateDialog = new DatePickerDialog(this, (arg0, year, monthOfYear, dayOfMonth) -> {
            // 把获取的日期显示在文本框内，月份从0开始计数，所以要加1
            String text = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            //Toast.makeText(LostAndFoundActivity.this, text, Toast.LENGTH_LONG).show();
            PickTime.setText(text);
        }, year, monthOfYear, dayOfMonth);
        timeDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // TODO Auto-generated method stub
            //Toast.makeText(LostAndFoundActivity.this, hourOfDay + ":" + minute,
                    //Toast.LENGTH_LONG).show();
            PickTime.setText(PickTime.getText().toString()+" "+hourOfDay + ":" + minute);
        }, hourOfDay, minute, true); // 最后一个参数设置是否为24小时制
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.lost:{
                if(!Flag){
                    Lost.setBackgroundResource(R.drawable.color_button);
                    Find.setBackgroundResource(R.drawable.edittext_shape06);
                    Flag=true;
                }
                break;
            }
            case R.id.find:{
                if (Flag){
                    Find.setBackgroundResource(R.drawable.color_button);
                    Lost.setBackgroundResource(R.drawable.edittext_shape06);
                    Flag=false;
                }
                break;
            }
            case R.id.add_picture:{
                /*******打开文件管理器选择图片*********/
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                this.startActivityForResult(intent, 0x005);
                break;
            }
            case R.id.add_picture2: {
                /*******打开文件管理器选择图片*********/
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                this.startActivityForResult(intent, 0x006);
                break;
            }
            case R.id.pick_time:{
                timeDialog.show();
                dateDialog.show();
                break;
            }
            case R.id.lost_submit:{
                String describe=LostDescribe.getText().toString();
                String location=LostPlace.getText().toString();
                String losttime=PickTime.getText().toString();
                String contract=ContractPeople.getText().toString();
                String call=PeopleCall.getText().toString();
                AlertDialog alert=new AlertDialog.Builder(this).create();
                alert.setTitle("系统提示");//设置对话框的标题
                alert.setMessage("是否确定发布？");//设置对话框显示的内容
                //添加“取消”按钮
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //Toast.makeText(LostAndFoundActivity.this, "取消发布", Toast.LENGTH_SHORT).show();
                                WarnToast(LostAndFoundActivity.this, "取消发布");
                            }
                        });
                //添加“确定”按钮
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                /*******************以下向数据库保存博客********************/
                                PostLostAndFoundByAsync task=new PostLostAndFoundByAsync(LostAndFoundActivity.this,
                                        Flag,describe,url,url2,location,losttime,contract,call, GlobalUserState.UserPhone);
                                task.execute(0);
                                //Toast.makeText(PostBlogActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                SuccessToast(LostAndFoundActivity.this,"发布成功");
                            }
                        });
                alert.show();//显示对话框
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
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
        if (requestCode == 0x005) {
            AddPicture.setImageURI(uri);
            new Thread(() -> {//更新数据库
                url=UploadImage(path);
                //handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
            //mEditor.insertImage(ImageUrl.image_url, "dachshund");
        }else if(requestCode==0x006){
            AddPicture2.setImageURI(uri);
            new Thread(() -> {//更新数据库
                url2=UploadImage(path);
                //handler.sendEmptyMessage(2);//通知主线程更新控件
            }).start();
        }
    }
    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Bitmap bmp = getURLimage(url);
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = bmp;
                            System.out.println("000");
                            handle.sendMessage(msg);
                        }
                    }).start();
                    break;
                case 2:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Bitmap bmp = getURLimage(url2);
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = bmp;
                            System.out.println("000");
                            handle.sendMessage(msg);
                        }
                    }).start();
                    break;
                default :
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                System.out.println("111");
                Bitmap bmp = (Bitmap) msg.obj;
                AddPicture.setImageBitmap(bmp);
            }else if(msg.what==4){
                System.out.println("111");
                Bitmap bmp = (Bitmap) msg.obj;
                AddPicture2.setImageBitmap(bmp);
            }
        };
    };

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
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