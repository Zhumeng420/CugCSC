package com.example.cugcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EmotionDetailActivity extends AppCompatActivity {
    private TextView title;
    private ImageView head;
    private TextView name;
    private  byte[] buff;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_detail);
        /**********绑定控件*************************/
        title=findViewById(R.id.title);
        head=findViewById(R.id.user_head);
        name=findViewById(R.id.user_name);
        /**********接受从列表传输过来的数据************/
        Intent i=getIntent();
        title.setText(i.getStringExtra("title"));
        name.setText(i.getStringExtra("name"));
        buff=i.getByteArrayExtra("head");
        head.setImageBitmap(BitmapFactory.decodeByteArray(buff,0, buff.length));
        final String dataStr=i.getStringExtra("diarys");
        initWebView(dataStr);
    }

    /**
     * 初始化博客显示器
     * @param data
     */
    public void initWebView(String data) {
        WebView mWebView = findViewById(R.id.content);
        WebSettings settings = mWebView.getSettings();
        //settings.setUseWideViewPort(true);//调整到适合webview的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        mWebView.setVerticalScrollBarEnabled(false);//不能垂直滑动
        mWebView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        settings.setTextSize(WebSettings.TextSize.NORMAL);//通过设置WebSettings，改变HTML中文字的大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);//设置js可用
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.addJavascriptInterface(new AndroidJavaScript(getApplication()), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        /******  22222222  ***********************************************************************/
        data = "</Div><head><style>img{ width:100% !important;}</style></head>" + data;//给图片设置一个样式，宽满屏
        /******  2222222222  ***********************************************************************/
        mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    /**
     * AndroidJavaScript
     * 本地与h5页面交互的js类，这里写成内部类了
     * returnAndroid方法上@JavascriptInterface一定不能漏了
     */
    private class AndroidJavaScript {
        Context mContxt;
        public AndroidJavaScript(Context mContxt) {
            this.mContxt = mContxt;
        }
        @JavascriptInterface
        public void returnAndroid(String name) {//从网页跳回到APP，这个方法已经在上面的HTML中写上了
            if (name.isEmpty() || name.equals("")) {
                return;
            }
            Toast.makeText(getApplication(), name, Toast.LENGTH_SHORT).show();
            //这里写你的操作///////////////////////
            //MainActivity就是一个空页面，不影响
            Intent intent = new Intent(EmotionDetailActivity.this, MainActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}