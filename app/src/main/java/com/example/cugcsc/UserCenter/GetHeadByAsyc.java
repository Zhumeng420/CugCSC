package com.example.cugcsc.UserCenter;


import static com.example.cugcsc.tool.GetImageByURL.getURLimage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 异步加载用户头像
 */
public class GetHeadByAsyc extends AsyncTask<String,Void, Bitmap> {
    private ImageView head;
    public GetHeadByAsyc(ImageView head) {
        this.head=head;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String url=params[0];
        return getURLimage(url);
    }
    @Override
    protected void onPostExecute(Bitmap data) {
            head.setImageBitmap(data);
    }
}
