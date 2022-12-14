package com.example.cugcsc.tool;



import static com.example.cugcsc.tool.HttpUtils.UploadImage;

import android.os.AsyncTask;

public class UploadImageByAsync extends AsyncTask<String,Void,Boolean> {

    private String path;
    private String url;
    public UploadImageByAsync(String path, String url) {
        this.path=path;
        this.url=url;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        this.url=UploadImage(path);
        //return SendPhoneCaptcha(params[0],params[1]);
        //String json=HttpUtils.getJSON(params[0]);
        return true;
    }
    @Override
    protected void onPostExecute(Boolean flag) {

    }
}
