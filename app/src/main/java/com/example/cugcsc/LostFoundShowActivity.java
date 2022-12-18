package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.ItemToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.data.LostAndFoundData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LostFoundShowActivity extends AppCompatActivity {
    private RecyclerView LostList;
    private List<LostAndFoundData> mlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_show);
        getRsource();
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
                            Toast.makeText(LostFoundShowActivity.this,"click:"+pos,Toast.LENGTH_SHORT).show();
                        }
                    }));
                    LostList.addItemDecoration(new Mydecoration());
                    break;
                default :
                    break;
            }
        }

    };


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
            holder.LostPicture.setImageBitmap(mlist.get(position).picture);
            holder.LostDescribe.setText(mlist.get(position).describes);
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
            public LinearViewHolder(@NonNull View itemView) {
                super(itemView);
                LostPicture=itemView.findViewById(R.id.lost_picture);
                LostDescribe=itemView.findViewById(R.id.lost_describe);
                LostUserHead=itemView.findViewById(R.id.lost_user_head);
                LostUserName=itemView.findViewById(R.id.lost_user_name);
            }
        }
        public interface OnItemClickListener {
            void onClick(int pos);
        }
    }
}