package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetEmotion.getEmotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.PostType;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmotionShowActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<EmoData> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_show);
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
        /********读取数据***********/
        if(PostType.type==1){
            new Thread(() -> {
                try {
                    getEmotion(mList,"school");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
        }else if(PostType.type==2){
            new Thread(() -> {
                try {
                    getEmotion(mList,"emotion");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
        }else if(PostType.type==3){
            new Thread(() -> {
                try {
                    getEmotion(mList,"interest");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
        }else if(PostType.type==4){
            new Thread(() -> {
                try {
                    getEmotion(mList,"study");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
        }

    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHoder> {
        private Context context;
        private List<EmoData> data;

        public MyAdapter(Context context,List<EmoData> data){
            this.context=context;
            this.data=data;
        }

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(EmotionShowActivity.this, R.layout.emo_item, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
            EmoData news =mList.get(position);
            holder.name.setText(news.name);
            holder.title.setText(news.title);
            holder.head.setImageBitmap(news.head);
            holder.visit.setText(news.visit_nums+"阅读 ");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            holder.date.setText( format.format(news.post_time));
            //单击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击条目进行跳转
                    /*Intent intent = new Intent(context, HtmlForBlogDetail.class);
                    /*************以下增加该帖子的访问量**************/
                    /*new Thread(() -> {
                        try {
                            addVisitNums(news.id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);//通知主线程更新控件
                    }).start();
                    /***********以下为activity之间的数据传输**********/
                   /* intent.putExtra("id",data.get(position).id);
                    intent.putExtra("title",data.get(position).title);
                    intent.putExtra("name",data.get(position).name);
                    intent.putExtra("diarys",data.get(position).content);
                    intent.putExtra("head",Bitmap2Bytes(data.get(position).head));
                    intent.putExtra("like_nums",data.get(position).like_nums);
                    intent.putExtra("collect_nums",data.get(position).collect_nums);
                    context.startActivity(intent);
                    //Toast.makeText(context, "click" + position, Toast.LENGTH_SHORT).show();*/
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
            ImageView head;
            TextView visit;
            TextView like;
            TextView date;
            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                title = itemView.findViewById(R.id.title);
                head=itemView.findViewById(R.id.head);
                visit=itemView.findViewById(R.id.visit_nums);
                date=itemView.findViewById(R.id.post_time);
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
                    mMyAdapter = new MyAdapter(EmotionShowActivity.this,EmotionShowActivity.this.mList);
                    mRecyclerView.setAdapter(mMyAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(EmotionShowActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);        //修改UI界面控件属性
                    break;
                default :
                    break;
            }
        }

    };
}