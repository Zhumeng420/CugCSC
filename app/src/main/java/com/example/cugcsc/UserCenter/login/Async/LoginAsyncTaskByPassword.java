package com.example.cugcsc.UserCenter.login.Async;
import static com.example.cugcsc.UserCenter.login.BasicApi.GetUserInfoByPhone.GetUserInfo;
import static com.example.cugcsc.UserCenter.login.BasicApi.VerifyLogin.VerifyPassword;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.LoadToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import java.sql.SQLException;

public class LoginAsyncTaskByPassword extends AsyncTask<String, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private final Activity current;
    public LoginAsyncTaskByPassword(Activity current) {
        this.current=current;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LoadToast(current,"登录中...");
    }
    @Override
    protected void onPostExecute(Boolean flag) {
        if(flag){
            current.setResult(0x002);
            current.finish();
        }
        else {
            ErrorToast(current,"密码错误");
        }
    }
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            if(VerifyPassword(params[0],params[1])){
                return GetUserInfo(params[0]);
            };
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
