package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MoviePlayActivity extends AppCompatActivity {
    private WebView MovieShow;
    private FrameLayout fullVideo;
    private View customView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_play);
        MovieShow=findViewById(R.id.movie_show);
        fullVideo=findViewById(R.id.full_video);
        MovieShow.setWebChromeClient(new MyWebChromeClient());
        MovieShow.setWebViewClient(new WebViewClient());
        /******接受数据*******/
        Intent i=getIntent();
        String url=i.getStringExtra("url");
        MovieShow.loadUrl(url);
        /******浏览器中允许返回*************/
        WebSettings webSettings=MovieShow.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
    /*******全屏设置***********/
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onHideCustomView() {
            //退出全屏
            if (customView == null){
                return;
            }
            //移除全屏视图并隐藏
            fullVideo.removeView(customView);
            fullVideo.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏

        }
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //进入全屏
            customView = view;
            fullVideo.setVisibility(View.VISIBLE);
            fullVideo.addView(customView);
            fullVideo.bringToFront();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        }
    }
    @Override
    public void onBackPressed(){
        if(MovieShow.canGoBack()){
            MovieShow.goBack();
        }else{
            super.onBackPressed();
        }
    }
}