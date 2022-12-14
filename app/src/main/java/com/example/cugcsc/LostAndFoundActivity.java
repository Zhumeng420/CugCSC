package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

public class LostAndFoundActivity extends AppCompatActivity {
    CardView Describe;
    CardView Photo;
    CardView PeopleInfo;

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
    }
}