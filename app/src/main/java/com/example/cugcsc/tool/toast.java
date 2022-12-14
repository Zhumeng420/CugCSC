package com.example.cugcsc.tool;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.R;

public class toast {
    public static  void WarnToast(Context context,String messgage){
        android.widget.Toast toast2=new android.widget.Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.toast, null);
        ImageView imageview= (ImageView) view.findViewById(R.id.tip_img);
        TextView textview= (TextView) view.findViewById(R.id.tip_info);
        imageview.setImageResource(R.drawable.warn);
        textview.setText(messgage);
        toast2.setView(view);
        toast2.setGravity(Gravity.TOP, 0, 50);
        toast2.setDuration(android.widget.Toast.LENGTH_SHORT);//显示的时间长短
        toast2.show();
    }
    public static  void ErrorToast(Context context,String messgage){
        android.widget.Toast toast2=new android.widget.Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.toast, null);
        ImageView imageview= (ImageView) view.findViewById(R.id.tip_img);
        TextView textview= (TextView) view.findViewById(R.id.tip_info);
        imageview.setImageResource(R.drawable.error);
        textview.setText(messgage);
        toast2.setView(view);
        toast2.setGravity(Gravity.TOP, 0, 50);
        toast2.setDuration(android.widget.Toast.LENGTH_SHORT);//显示的时间长短
        toast2.show();
    }
    public static  void LoadToast(Context context,String messgage){
        android.widget.Toast toast2=new android.widget.Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.toast, null);
        ImageView imageview= (ImageView) view.findViewById(R.id.tip_img);
        TextView textview= (TextView) view.findViewById(R.id.tip_info);
        imageview.setImageResource(R.drawable.load);
        textview.setText(messgage);
        toast2.setView(view);
        toast2.setGravity(Gravity.TOP, 0, 50);
        toast2.setDuration(android.widget.Toast.LENGTH_SHORT);//显示的时间长短
        toast2.show();
    }
    public static  void SuccessToast(Context context,String messgage){
        android.widget.Toast toast2=new android.widget.Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.toast, null);
        ImageView imageview= (ImageView) view.findViewById(R.id.tip_img);
        TextView textview= (TextView) view.findViewById(R.id.tip_info);
        imageview.setImageResource(R.drawable.sucess);
        textview.setText(messgage);
        toast2.setView(view);
        toast2.setGravity(Gravity.TOP, 0, 50);
        toast2.setDuration(android.widget.Toast.LENGTH_SHORT);//显示的时间长短
        toast2.show();
    }
}
