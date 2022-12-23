package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.UserCenter.get.GetLostAndFound.SearchLostFound;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.ItemToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.data.LostAndFoundData;
import com.example.cugcsc.data.SearchKey;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LostFoundShowActivity extends AppCompatActivity implements  View.OnClickListener {
    private RecyclerView LostList;
    private List<LostAndFoundData> mlist=new ArrayList<>();
    private Button serach;
    private EditText key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_show);
        /*******绑定搜索按钮*******/
        serach=findViewById(R.id.search_button);
        serach.setOnClickListener(this);
        key=findViewById(R.id.search_context);
        /********搜索按钮更改字体*********/
        Button SearchButton=findViewById(R.id.search_button);
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );//设置按钮字体
        SearchButton.setTypeface(type);
        /*******获取失物招领列表********/
        if(SearchKey.flag){//如果是搜索则部分加载
            getSearch();
            key.setText(SearchKey.key);
        }else{//否则全部加载
            getRsource();
        }
        /*****关闭搜索状态*****/
        SearchKey.flag=false;
    }

    private void getRsource(){
        new Thread(() -> {
            try {
                GetLostFound(mlist);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//通知主线程更新控件
        }).start();
    }
    private void getSearch(){
        new Thread(() -> {
            try {
                SearchLostFound(mlist,SearchKey.key);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);//通知主线程更新控件
        }).start();
    }
    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    LostList=findViewById(R.id.recycler_view_lost);
                    LostList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    LostList.setAdapter(new StaggeredGrideViewRecAdapter(LostFoundShowActivity.this,mlist,new StaggeredGrideViewRecAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(LostFoundShowActivity.this,"click:"+pos,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LostFoundShowActivity.this,LostFoundDeatilActivity.class);
                            intent.putExtra("label",mlist.get(pos).type);
                            intent.putExtra("describe",mlist.get(pos).describes);
                            intent.putExtra("url",mlist.get(pos).url);
                            intent.putExtra("url2",mlist.get(pos).url2);
                            intent.putExtra("location",mlist.get(pos).location);
                            intent.putExtra("time",mlist.get(pos).time);
                            intent.putExtra("name",mlist.get(pos).contract);
                            intent.putExtra("phone",mlist.get(pos).phone);
                            LostFoundShowActivity.this.startActivity(intent);
                        }
                    }));
                    LostList.addItemDecoration(new Mydecoration());
                    break;
                default :
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:{
                /*****设置搜索状态*********/
                SearchKey.key=key.getText().toString();
                SearchKey.flag=true;
                /****跳转********/
                startActivity(new Intent(getApplicationContext(), LostFoundShowActivity.class));
                overridePendingTransition(0,0);
            }
        }
    }


    class  Mydecoration extends  RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(getResources().getDimensionPixelOffset(R.dimen.dividerHeigh),getResources().getDimensionPixelOffset(R.dimen.dividerHeigh),getResources().getDimensionPixelOffset(R.dimen.dividerHeigh),getResources().getDimensionPixelOffset(R.dimen.dividerHeigh));
        }
    }
    public static class StaggeredGrideViewRecAdapter extends RecyclerView.Adapter<StaggeredGrideViewRecAdapter.LinearViewHolder> {
        private Context mcontext;
        private OnItemClickListener mlistener;
        private List<LostAndFoundData> mlist=new ArrayList<>();
        public StaggeredGrideViewRecAdapter(Context context,List<LostAndFoundData> mlist,OnItemClickListener mlistener){
            this.mlistener=mlistener;
            this.mcontext=context;
            this.mlist=mlist;
        }

        @NonNull
        @Override
        public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.lost_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {
            /*if(position %2==0) {
                holder.LostPicture.setImageResource(R.drawable.cry);
            }
            else if (position % 2 == 1) {
                holder.LostPicture.setImageResource(R.drawable.home);
            }*/
            /*********图片大小调整***********/
            int height=mlist.get(position).picture.getHeight();
            int width=mlist.get(position).picture.getWidth();
            double NewHeight=(double)height/(double)width*140;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(140*3, (int) NewHeight*3);//两个400分别为添加图片的大小
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(140*3, (int) NewHeight*3);//两个400分别为添加图片的大小
            holder.LostPicture.setLayoutParams(params);
            holder.Card.setLayoutParams(params2);
            /**********Texview拓宽***********/
            if(mlist.get(position).describes.length()>=7) {
                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(140 * 3, (int) NewHeight * 3 + 40);//两个400分别为添加图片的大小
                holder.Card.setLayoutParams(params3);
                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(140 * 3, 30 * 3 + 40);//两个400分别为添加图片的大小
                holder.LostDescribe.setLayoutParams(params4);
            }
            /*******内容填充****************/
            holder.LostPicture.setImageBitmap(mlist.get(position).picture);
            holder.LostDescribe.setText(mlist.get(position).describes);
            holder.LostUserName.setText(mlist.get(position).username);
            holder.LostUserHead.setImageBitmap(mlist.get(position).head);
            if(!mlist.get(position).type){
                holder.Lable.setBackgroundResource(R.drawable.findlabel);
                holder.Lable.setText("寻物启事");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mlist.size();
        }

        class LinearViewHolder extends RecyclerView.ViewHolder{
            private ImageView LostPicture;
            private TextView LostDescribe;
            private ImageView LostUserHead;
            private TextView LostUserName;
            private Button Lable;
            private CardView Card;
            public LinearViewHolder(@NonNull View itemView) {
                super(itemView);
                LostPicture=itemView.findViewById(R.id.lost_picture);
                LostDescribe=itemView.findViewById(R.id.lost_describe);
                LostUserHead=itemView.findViewById(R.id.lost_user_head);
                LostUserName=itemView.findViewById(R.id.lost_user_name);
                Lable=itemView.findViewById(R.id.label);
                Card=itemView.findViewById(R.id.lost_card);
            }
        }

        public interface OnItemClickListener {
            void onClick(int pos);
        }
    }
}