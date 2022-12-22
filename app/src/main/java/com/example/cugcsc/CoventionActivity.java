package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CoventionActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout BackMine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covention);
        BackMine=findViewById(R.id.back_mine);
        BackMine.setOnClickListener(this);//监听点击事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_mine:{
                finish();
            }
        }
    }
}