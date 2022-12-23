package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebShowActivity extends AppCompatActivity {
    private WebView Show;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_show);
        Show=findViewById(R.id.movie_show);
        Show.setWebChromeClient(new WebChromeClient());
        //Show.setWebViewClient(new WebViewClient());
        /******接受数据*******/
        Intent i=getIntent();
        String url=i.getStringExtra("url");
        Show.loadUrl(url);
        /******浏览器中允许返回*************/
        WebSettings webSettings=Show.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
    }
    @Override
    public void onBackPressed(){
        if(Show.canGoBack()){
            Show.goBack();
        }else{
            super.onBackPressed();
        }
    }
}