package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.SQLException;

public class LostFoundDeatilActivity extends AppCompatActivity {
    CardView Describ;
    CardView Photo;
    CardView PeopleInfo;
    Button Lost;
    Button Find;
    EditText Describe;
    ImageView Add;
    ImageView Add2;
    Bitmap p1;
    Bitmap p2;
    EditText LostPlace;
    EditText Time;
    EditText peopple;
    EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_deatil);
        /*******设置卡片阴影*********/
        Describ=findViewById(R.id.describe_obj);
        Describ.setCardElevation(8);//设置阴影部分大小
        Describ.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        Photo=findViewById(R.id.photo);
        Photo.setCardElevation(8);//设置阴影部分大小
        Photo.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        PeopleInfo=findViewById(R.id.people_info);
        PeopleInfo.setCardElevation(8);//设置阴影部分大小
        PeopleInfo.setContentPadding(5,5,5,5);//设置图片距离阴影大小*/
        /*******填充内容**********/
        Lost=findViewById(R.id.lost);
        Find=findViewById(R.id.find);
        Intent i=getIntent();
        Boolean flag=true;
        flag=i.getBooleanExtra("label",flag);//获取状态（失物招领还是寻物启事）
        if(flag==false){
            Lost.setBackgroundResource(R.drawable.edittext_shape06);
            Find.setBackgroundResource(R.drawable.color_button);
        }
        Describe=findViewById(R.id.lost_describe);
        Describe.setText(i.getStringExtra("describe"));
        Describe.setInputType(InputType.TYPE_NULL);//设置不可输入（修改）
        Add=findViewById(R.id.add_picture);
        Add2=findViewById(R.id.add_picture2);
        new Thread(() -> {
            p1=getURLimage(i.getStringExtra("url"));
            if(i.getStringExtra("url")!="https://tse1-mm.cn.bing.net/th/id/OIP-C.Orp_AQoc00mZb-e1N-c8cgD6D6?pid=ImgDet&rs=1")
            {
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }
        }).start();
        new Thread(() -> {
            p2=getURLimage(i.getStringExtra("url2"));
            if(i.getStringExtra("url")!="https://tse1-mm.cn.bing.net/th/id/OIP-C.Orp_AQoc00mZb-e1N-c8cgD6D6?pid=ImgDet&rs=1")
            {
                handler.sendEmptyMessage(2);//通知主线程更新控件
            }
        }).start();
        LostPlace=findViewById(R.id.lost_place);
        LostPlace.setText(i.getStringExtra("location"));
        LostPlace.setInputType(InputType.TYPE_NULL);//设置不可输入（修改）
        Time=findViewById(R.id.pick_time);
        Time.setText(i.getStringExtra("time"));
        Time.setInputType(InputType.TYPE_NULL);//设置不可输入（修改）
        peopple=findViewById(R.id.contract_people);
        peopple.setText(i.getStringExtra("name"));
        peopple.setInputType(InputType.TYPE_NULL);//设置不可输入（修改）
        phone=findViewById(R.id.phone_call);
        phone.setText(i.getStringExtra("phone"));
        phone.setInputType(InputType.TYPE_NULL);//设置不可输入（修改）
    }
    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    Add.setImageBitmap(p1);
                    break;
                case 2:
                    Add2.setImageBitmap(p2);
                default :
                    break;
            }
        }
    };
}