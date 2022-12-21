package com.example.cugcsc;

import static com.example.cugcsc.UserCenter.get.GetLostAndFound.GetLostFound;
import static com.example.cugcsc.UserCenter.get.GetMovie.getMovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cugcsc.data.LostAndFoundData;
import com.example.cugcsc.data.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieCenterActivity extends AppCompatActivity {
    private RecyclerView MovieList;
    private List<Movie> mlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_center);
        /********搜索按钮更改字体*********/
        Button SearchButton=findViewById(R.id.search_button);
        Typeface type = Typeface.createFromAsset(getAssets(),"search.otf" );//设置按钮字体
        SearchButton.setTypeface(type);
        /********获取电影列表***********/
        new Thread(() -> {
            try {
                getMovie(mlist);
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
                    System.out.println("sss"+mlist.size());
                    MovieList=findViewById(R.id.movie_list);
                    MovieList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    MovieList.setAdapter(new StaggeredGrideViewRecAdapter(MovieCenterActivity.this,mlist,new StaggeredGrideViewRecAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(LostFoundShowActivity.this,"click:"+pos,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MovieCenterActivity.this,MoviePlayActivity.class);
                            intent.putExtra("url",mlist.get(pos).url);
                            MovieCenterActivity.this.startActivity(intent);
                        }
                    }));
                    MovieList.addItemDecoration(new MovieCenterActivity.Mydecoration());
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
        private StaggeredGrideViewRecAdapter.OnItemClickListener mlistener;
        private List<Movie> mlist;
        public StaggeredGrideViewRecAdapter(Context context, List<Movie> mlist, StaggeredGrideViewRecAdapter.OnItemClickListener mlistener){
            this.mlistener=mlistener;
            this.mcontext=context;
            this.mlist=mlist;
        }

        @NonNull
        @Override
        public StaggeredGrideViewRecAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new StaggeredGrideViewRecAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.movie_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull StaggeredGrideViewRecAdapter.LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {
            /*if(position %2==0) {
                holder.Picture.setImageResource(R.drawable.cry);
            }
            else if (position % 2 == 1) {
                holder.Picture.setImageResource(R.drawable.home);
            }*/
            /*********图片大小调整***********/
            int height=mlist.get(position).picture.getHeight();
            int width=mlist.get(position).picture.getWidth();
            double NewHeight=(double)height/(double)width*140;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(140*3, (int) NewHeight*3);//两个400分别为添加图片的大小
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(140*3, (int) NewHeight*3);//两个400分别为添加图片的大小
            holder.Picture.setLayoutParams(params);
            holder.Card.setLayoutParams(params2);
            /********TextView拓宽*********/
            if(mlist.get(position).title.length()>=7) {
                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(140 * 3, (int) NewHeight * 3 + 40);//两个400分别为添加图片的大小
                holder.Card.setLayoutParams(params3);
                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(140 * 3, 30 * 3 + 40);//两个400分别为添加图片的大小
                holder.Title.setLayoutParams(params4);
            }
            /*******内容填充****************/
            holder.Picture.setImageBitmap(mlist.get(position).picture);
            holder.Title.setText(mlist.get(position).title);
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
            private ImageView Picture;
            private TextView Title;
            private CardView Card;
            public LinearViewHolder(@NonNull View itemView) {
                super(itemView);
                Picture=itemView.findViewById(R.id.moive_picture);
                Title=itemView.findViewById(R.id.movie_title);
                Card=itemView.findViewById(R.id.movie_card);
            }
        }

        public interface OnItemClickListener {
            void onClick(int pos);
        }
    }
}