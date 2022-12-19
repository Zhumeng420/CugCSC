package com.example.cugcsc.UserCenter.post.Async;

import static com.example.cugcsc.UserCenter.post.BasicApi.PostLostAndFound.postLostFound;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.SQLException;

/**
 * 异步发布博客
 */
public class PostLostAndFoundByAsync extends AsyncTask<Integer,Integer,String> {
        Context context;
        ProgressDialog progressDialog;
        Boolean type;
        String describe;
        String url;
        String url2;
        String location;
        String time;
        String name;
        String phone;
        String poster;
    public PostLostAndFoundByAsync(Context context,Boolean type,String describe,String url,
                String url2,String location,String time,String name,String phone,String poster) {//获取传入的context,为新建progressDialog引入参数做准备
            this.context=context;
            this.type=type;
            this.describe=describe;
            this.url=url;
            this.url2=url2;
            this.location=location;
            this.time=time;
            this.name=name;
            this.phone=phone;
            this.poster=poster;
        }
        //后台执行任务
        @Override//传入进度值，并调用publishProgress传入状态更新方法中，实现进度值的前进
        protected String doInBackground(Integer... integers) {
            try {
                postLostFound(type,describe,url,url2,location,time,name,phone,poster);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int i=integers[0];
            while(i<100)
            {
                i++;
                publishProgress(i);//传送进度值
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "已经更新完成！";
        }
        //任务更新
        @Override//调用setProgress时刻更新进度值
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
        //执行任务前
        @Override//设置进度对话框必要参数
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("正在上传...");
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(60);
            progressDialog.show();
        }
        @Override//任务完成之后
        //设置textview显示更新完成（s是doInbackground完成后返回的字符串）,最后关闭进度对话框显示
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
}
