package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetEmotion.getEmotion;
import static com.example.cugcsc.UserCenter.get.GetMusic.getMusic;

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
import com.example.cugcsc.data.Music;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MusciCenterActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<Music> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musci_center);
        /*********设置横向列表**********/
        mRecyclerView = findViewById(R.id.music_list);
        //设置垂直布局
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration mDivider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        /*******获取音乐列表********/
        new Thread(() -> {
            try {
                getMusic(mList);
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
        private List<Music> data;

        public MyAdapter(Context context,List<Music> data){
            this.context=context;
            this.data=data;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MusciCenterActivity.this, R.layout.music_item, null);
            MyAdapter.MyViewHoder myViewHoder = new MyAdapter.MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
            Music news =mList.get(position);
            holder.song.setText(news.song);
            holder.singer.setText(news.singer);
            holder.head.setImageBitmap(news.pic);
            //单击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击条目进行跳转
                    Intent intent = new Intent(context, MusicPlayActivity.class);
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
                    intent.putExtra("collect_nums",data.get(position).collect_nums);*/
                    context.startActivity(intent);
                    //Toast.makeText(context, "click" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        private byte[] Bitmap2Bytes(Bitmap bm){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        class MyViewHoder extends RecyclerView.ViewHolder {
            TextView song;
            TextView singer;
            ImageView head;
            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                head=itemView.findViewById(R.id.head);
                song=itemView.findViewById(R.id.song);
                singer=itemView.findViewById(R.id.singer);
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
                    mMyAdapter = new MyAdapter(MusciCenterActivity.this,MusciCenterActivity.this.mList);
                    mRecyclerView.setAdapter(mMyAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MusciCenterActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);        //修改UI界面控件属性
                    break;
                default :
                    break;
            }
        }

    };

}