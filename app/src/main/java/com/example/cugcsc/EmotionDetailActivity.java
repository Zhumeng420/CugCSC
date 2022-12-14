package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetComment.getcomment;
import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.UserCenter.post.BasicApi.AddNums.addVisitNums;
import static com.example.cugcsc.UserCenter.post.BasicApi.AddRealation.postComment;
import static com.example.cugcsc.tool.GetImageByURL.getURLimage;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.SuccessToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.data.Comment;
import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.LostAndFoundData;
import com.example.cugcsc.data.PostType;
import com.example.cugcsc.data.Temp;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EmotionDetailActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextView title;
    private ImageView head;
    private TextView name;
    private  byte[] buff;
    private int id;
    private EditText comment;
    private Button post;
    private List<Comment> mlist=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_detail);
        /**********????????????*************************/
        title=findViewById(R.id.title);
        head=findViewById(R.id.user_head);
        name=findViewById(R.id.user_name);
        comment=findViewById(R.id.comment_context);
        post=findViewById(R.id.post_comment);
        post.setOnClickListener(this);
        /**********????????????????????????????????????************/
        Intent i=getIntent();
        title.setText(i.getStringExtra("title"));
        name.setText(i.getStringExtra("name"));
        //buff=i.getByteArrayExtra("head");
        //head.setImageBitmap(BitmapFactory.decodeByteArray(buff,0, buff.length));
        head.setImageBitmap(Temp.temphead);
        id=i.getIntExtra("id",id);
        final String dataStr=i.getStringExtra("diarys");
        /********?????????webview********/
        initWebView(dataStr);
        /*********??????????????????**********/
        mRecyclerView = findViewById(R.id.comment_list);
        //??????????????????
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //?????????
        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        //mRecyclerView.setHasFixedSize(true);//??????????????????
        //mRecyclerView.setNestedScrollingEnabled(false);
        /***************??????????????????*******************/
        String table="";
        if(PostType.type==1){
            table="school";
        }else if(PostType.type==2){
            table="emotion";
        }else if(PostType.type==3){
            table="interets";
        }else if(PostType.type==4){
            table="study";
        }
        String finalTable = table;
        new Thread(() -> {
            try {
                System.out.println(finalTable);
                System.out.println(id);
                getcomment(mlist, finalTable,String.valueOf(id));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//???????????????????????????
        }).start();
        new Thread(() -> {//????????????
            GlobalUserState.Head=getURLimage(GlobalUserState.URL);
        }).start();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHoder> {
        private Context context;
        private List<Comment> data;

        public MyAdapter(Context context,List<Comment> data){
            this.context=context;
            this.data=data;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(EmotionDetailActivity.this, R.layout.comment_item, null);
            MyAdapter.MyViewHoder myViewHoder = new MyAdapter.MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
            Comment news =mlist.get(position);
            holder.name.setText(news.name);
            holder.head.setImageBitmap(news.head);
            @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            holder.date.setText(format.format(news.date));
            holder.content.setText(unicode2String(news.content));
            holder.level.setText("?????????"+ news.level);
            //????????????
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//??????????????????

                }
            });
        }
        private byte[] Bitmap2Bytes(Bitmap bm){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        class MyViewHoder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView head;
            TextView content;
            TextView date;
            TextView level;
            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                head=itemView.findViewById(R.id.head);
                date=itemView.findViewById(R.id.post_time);
                content=itemView.findViewById(R.id.content);
                level=itemView.findViewById(R.id.level);
            }
        }
        @Override
        public int getItemCount() {
            System.out.println(mlist.size());
            return mlist.size();
        }
        //  ????????????
        public void addData(int position,Comment temp) {
        //  ???list?????????????????????????????????????????????
            mlist.add(position, temp);
            //????????????
            notifyItemInserted(position);
        }
    }
    //handler??????????????????????????????
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //???????????????????????????Message???1??????????????????????????????????????????????????????
                    mMyAdapter = new MyAdapter(EmotionDetailActivity.this,EmotionDetailActivity.this.mlist);
                    mRecyclerView.setAdapter(mMyAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(EmotionDetailActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);        //??????UI??????????????????
                    break;
                default :
                    break;
            }
        }

    };

    /**
     * ????????????????????????
     * @param data
     */
    public void initWebView(String data) {
        WebView mWebView = findViewById(R.id.content);
        WebSettings settings = mWebView.getSettings();
        //settings.setUseWideViewPort(true);//???????????????webview?????????????????????????????????????????????????????????
        settings.setLoadWithOverviewMode(true);//??????WebView???????????????????????????????????????
        mWebView.setVerticalScrollBarEnabled(false);//??????????????????
        mWebView.setHorizontalScrollBarEnabled(false);//??????????????????
        settings.setTextSize(WebSettings.TextSize.NORMAL);//????????????WebSettings?????????HTML??????????????????
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//????????????JS???????????????
        //??????WebView?????????????????????Javascript??????
        mWebView.getSettings().setJavaScriptEnabled(true);//??????js??????
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.addJavascriptInterface(new AndroidJavaScript(getApplication()), "android");//??????js??????
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//????????????????????????
        /******  22222222  ***********************************************************************/
        data = "</Div><head><style>img{width:100% height:100% !important; }</style></head>" + data;//???????????????????????????????????????
        /******  2222222222  ***********************************************************************/
        mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_comment:{
                if(Objects.equals(GlobalUserState.UserPhone, "")){
                    ErrorToast(this,"???????????????????????????");
                }else{
                    /*********????????????????????????**************/
                    String table="";
                    if(PostType.type==1){
                        table="school";
                    }else if(PostType.type==2){
                        table="emotion";
                    }else if(PostType.type==3){
                        table="interets";
                    }else if(PostType.type==4){
                        table="study";
                    }
                    String finalTable = table;
                    new Thread(() -> {
                        try {
                            postComment(finalTable,string2Unicode(comment.getText().toString()),GlobalUserState.UserPhone,mlist.size(),id);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);//???????????????????????????
                    }).start();
                    /*********??????ui??????*********************/
                    Comment temp=new Comment();
                    temp.level=mlist.size()+1;
                    temp.date=new Timestamp(new Date().getTime());
                    temp.content=string2Unicode(comment.getText().toString());
                    temp.head=GlobalUserState.Head;
                    temp.name=GlobalUserState.UserName;
                    mMyAdapter.addData(mlist.size(),temp);//????????????
                    comment.setText("");//???????????????
                    SuccessToast(this,"????????????");
                }
            }
        }
    }

    public static String unicode2String(String unicode) {
        Log.e("str==", "111111111" + "\\\\u");
        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            Log.e("str==", "22222222" + "\\\\u");
            // ???????????????????????????
            int data = Integer.parseInt(hex[i], 16);

            // ?????????string
            string.append((char) data);
        }

        return string.toString();
    }

    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // ?????????????????????
            char c = string.charAt(i);
            if (c < 256)//ASC11???????????????????????????4???,???00
            {
                unicode.append("\\u00");
            } else {
                unicode.append("\\u");
            }
            // ?????????unicode
            unicode.append(Integer.toHexString(c));
        }
        return unicode.toString();
    }
    /**
     * AndroidJavaScript
     * ?????????h5???????????????js??????????????????????????????
     * returnAndroid?????????@JavascriptInterface??????????????????
     */
    private class AndroidJavaScript {
        Context mContxt;
        public AndroidJavaScript(Context mContxt) {
            this.mContxt = mContxt;
        }
        @JavascriptInterface
        public void returnAndroid(String name) {//??????????????????APP?????????????????????????????????HTML????????????
            if (name.isEmpty() || name.equals("")) {
                return;
            }
            Toast.makeText(getApplication(), name, Toast.LENGTH_SHORT).show();
            //?????????????????????///////////////////////
            //MainActivity?????????????????????????????????
            Intent intent = new Intent(EmotionDetailActivity.this, MainActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}