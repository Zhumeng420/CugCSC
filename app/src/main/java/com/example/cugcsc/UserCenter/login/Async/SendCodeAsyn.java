package com.example.cugcsc.UserCenter.login.Async;

import static com.example.cugcsc.tool.PhoneCaptcha.SendPhoneCaptcha;
import static com.example.cugcsc.tool.toast.SuccessToast;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

public class SendCodeAsyn extends AsyncTask<String,Void,Boolean> {
    private Activity activity;
    private String phone;
    public SendCodeAsyn(Activity activity) {
        this.activity=activity;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        this.phone=params[0];
        //return SendPhoneCaptcha(params[0],params[1]);
        //String json=HttpUtils.getJSON(params[0]);
        return SendPhoneCaptcha(params[0],params[1]);
    }
    @Override
    protected void onPostExecute(Boolean flag) {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        SuccessToast(activity,"验证码已经成功发送");
    }
}
