package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetBook.getBook;
import static com.example.cugcsc.UserCenter.get.GetEmotion.getEmotion;
import static com.example.cugcsc.UserCenter.post.BasicApi.AddNums.addVisitNums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugcsc.data.Book;
import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.PostType;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookShowActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<Book> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_show);
        /*********设置横向列表**********/
        mRecyclerView = findViewById(R.id.emotion_list);
        //设置垂直布局
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        /*********读取数据**************/
        new Thread(() -> {
            try {
                getBook(mList);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//通知主线程更新控件
        }).start();
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHoder> {
        private Context context;
        private List<Book> data;

        public MyAdapter(Context context,List<Book> data){
            this.context=context;
            this.data=data;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(BookShowActivity.this, R.layout.book_item, null);
            MyAdapter.MyViewHoder myViewHoder = new MyAdapter.MyViewHoder(view);
            return myViewHoder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
            Book news =mList.get(position);
            holder.name.setText(news.author);
            holder.title.setText(news.title);
            //单击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /***********打开浏览器开始下载**********/
                    //根据url下载文件
                    Uri uri = Uri.parse(news.url);
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        //指定谷歌浏览器
                        intent.setClassName("com.android.chrome","com.google.android.apps.chrome.Main");
                        startActivity(intent);
                    }catch (Exception e){
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
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
            TextView title;
            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                title = itemView.findViewById(R.id.title);
            }
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }

    }


    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    mMyAdapter = new MyAdapter(BookShowActivity.this,BookShowActivity.this.mList);
                    mRecyclerView.setAdapter(mMyAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(BookShowActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);        //修改UI界面控件属性
                    break;
                default :
                    break;
            }
        }

    };

}