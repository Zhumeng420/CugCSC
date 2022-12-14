package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetEmotion.getEmotion;
import static com.example.cugcsc.UserCenter.get.GetEmotion.getEmotionSearch;
import static com.example.cugcsc.UserCenter.get.GetLostAndFound.SearchLostFound;
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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugcsc.data.EmoData;
import com.example.cugcsc.data.PostType;
import com.example.cugcsc.data.SearchKey;
import com.example.cugcsc.data.Temp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmotionShowActivity extends AppCompatActivity  implements  View.OnClickListener{
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<EmoData> mList=new ArrayList<>();
    private EditText SearchContext;
    private Button SearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_show);
        /*********绑定控件***********/
        SearchContext=findViewById(R.id.search_context);
        SearchButton=findViewById(R.id.search_button);
        SearchButton.setOnClickListener(this);
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
        String table="";//首先要确定是哪个表
        if(PostType.type==1){
            table="school";
        }else if(PostType.type==2){
            table="emotion";
        }else if(PostType.type==3){
            table="interest";
        }else if(PostType.type==4){
            table="study";
        }
        if(SearchKey.flag){
            getSearch(table);
            SearchContext.setText(SearchKey.key);
        }else{
            GetData(table);
        }
        SearchKey.flag=false;
    }
    private void GetData(String table){
        new Thread(() -> {
            try {
                getEmotion(mList,table);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//通知主线程更新控件
        }).start();
    }
    private void getSearch(String table){
        new Thread(() -> {
            try {
                getEmotionSearch(mList, table,SearchKey.key);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//通知主线程更新控件
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:{
                /*****设置搜索状态*********/
                SearchKey.key=SearchContext.getText().toString();
                System.out.println(SearchKey.key);
                SearchKey.flag=true;
                /****跳转********/
                startActivity(new Intent(getApplicationContext(),EmotionShowActivity.class));
                overridePendingTransition(0,0);
            }
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
                    /********增加访问量***************/
                    String table="";//首先要确定是哪个表
                    if(PostType.type==1){
                        table="school";
                    }else if(PostType.type==2){
                        table="emotion";
                    }else if(PostType.type==3){
                        table="interest";
                    }else if(PostType.type==4){
                        table="study";
                    }
                    String finalTable = table;
                    new Thread(() -> {
                        try {
                            addVisitNums(news.id, finalTable);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);//通知主线程更新控件
                    }).start();
                    //点击条目进行跳转
                    Intent intent = new Intent(context, EmotionDetailActivity.class);
                    /***********以下为activity之间的数据传输**********/
                    intent.putExtra("id",data.get(position).id);
                    intent.putExtra("title",data.get(position).title);
                    intent.putExtra("name",data.get(position).name);
                    intent.putExtra("diarys",data.get(position).content);
                    //intent.putExtra("head",Bitmap2Bytes(compressImage(data.get(position).head)));
                    Temp.temphead=data.get(position).head;
                    context.startActivity(intent);
                    //Toast.makeText(context, "click" + position, Toast.LENGTH_SHORT).show();*/
                }
            });
        }
        public Bitmap compressImage(Bitmap image) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            return bitmap;
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