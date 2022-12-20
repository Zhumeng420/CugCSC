package com.example.cugcsc;



import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cugcsc.tool.LrcView.Lrc;
import com.example.cugcsc.tool.LrcView.LrcHelper;
import com.example.cugcsc.tool.LrcView.LrcView;
import com.example.cugcsc.tool.download.DownloadConfiguration;
import com.example.cugcsc.tool.download.DownloadManager;
import com.example.cugcsc.tool.download.FileDownloadTask;
import com.example.cugcsc.tool.download.FileType;
import com.example.cugcsc.tool.download.listener.OnDownloadingListener;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{
    private static final String TAG = MusicPlayActivity.class.getSimpleName();
    public MediaPlayer mediaPlayer; // 媒体播放器
    private Timer timer = new Timer(); // 计时器
    private boolean isPrepare = false;
    private SeekBar seekBar;
    private static  String PATH = "http://81.70.13.188:9000/cugsdn/平行恋人 (女版)-刘至佳_1671458082656.mp3";
    private static  String PATH2 = "http://81.70.13.188:9000/cugsdn/平行恋人 (女版)-刘至佳_1671541207394.lrc";

    private TextView tvCurrent;
    private TextView tvDuration;
    private Button btnPlay;
    private CardView MusicPicture;
    private Animation animation;//用于控制画面旋转
    private LrcView mLrcView;//歌词显示
    private byte[] buff;//用于旋转的图标显示
    private ImageView pic;//显示旋转图片
    private TextView song;
    private TextView singer;
    /******线程池调度服务******/
    public static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                    .daemon(true).build());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        /**********初始化线程池***************/
        DownloadConfiguration downloadConfiguration = new DownloadConfiguration.Builder(getApplicationContext())
                .setCacheDir(getExternalCacheDir())        //设置下载缓存目录，必须设置
                .setTaskExecutor(executorService)    //同上传类似
                .setThreadPriority(5)  //同上传类似
                .setThreadPoolCoreSize(5)  //同上传类似
                .build();
        DownloadManager.getInstance(this).init(downloadConfiguration);
        /**********绑定控件**************/
        MusicPicture=findViewById(R.id.music_picture);
        View v =findViewById(R.id.music_picture);//设置透明，只留下圆形图片
        v.getBackground().setAlpha(0);//0~255透明度值 ，0为完全透明，255为不透明
        seekBar = findViewById(R.id.seekBar);
        tvCurrent = findViewById(R.id.tv_current);
        tvDuration = findViewById(R.id.tv_duration);
        btnPlay = findViewById(R.id.btn_play);
        pic=findViewById(R.id.user_head);
        song=findViewById(R.id.song);
        singer=findViewById(R.id.singer);
        btnPlay.setEnabled(false);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setText("继续");
                    MusicPicture.clearAnimation();
                    mLrcView.pause();//歌词暂停

                } else {
                    /*******设置图片选择动画*********/
                    animation = AnimationUtils.loadAnimation(MusicPlayActivity.this, R.anim.img_animation);
                    LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
                    animation.setInterpolator(lin);
                    MusicPicture.startAnimation(animation);
                    /*******音乐开始播放*********/
                    play();
                    btnPlay.setText("暂停");

                    mLrcView.resume();//歌词继续
                }
            }
        });
        /***********接受数据**************/
        Intent i=getIntent();
        buff=i.getByteArrayExtra("pic");
        pic.setImageBitmap(BitmapFactory.decodeByteArray(buff,0,buff.length));//设置旋转图片
        PATH=i.getStringExtra("url");
        PATH2=i.getStringExtra("lrc");
        song.setText(i.getStringExtra("song"));
        singer.setText(i.getStringExtra("singer"));
        System.out.println(PATH);
        System.out.println(PATH2);
        /*******初始化进度条**********/
        seekBar.setProgress(0);//设置进度为0
        seekBar.setSecondaryProgress(0);//设置缓冲进度为0
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar mSeekBar, int progress, boolean fromUser) {
                tvCurrent.setText(getTime(mSeekBar.getProgress()));

                int currentPosition = mediaPlayer.getCurrentPosition();//更新歌词
                mLrcView.updateTime(currentPosition);
            }
            @Override
            public void onStartTrackingTouch(SeekBar mSeekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar mSeekBar) {
                if (mediaPlayer.isPlaying()) {
                    /******音乐播放*******/
                    mediaPlayer.seekTo(mSeekBar.getProgress());
                    tvCurrent.setText(getTime(mSeekBar.getProgress()));

                    int currentPosition = mediaPlayer.getCurrentPosition();//更新歌词
                    mLrcView.updateTime(currentPosition);
                } else {
                    if (isPrepare) {
                        mediaPlayer.seekTo(mSeekBar.getProgress());
                        tvCurrent.setText(getTime(mSeekBar.getProgress()));

                        int currentPosition = mediaPlayer.getCurrentPosition();//更新歌词
                        mLrcView.updateTime(currentPosition);

                        mediaPlayer.start();
                        btnPlay.setText("暂停");
                    }
                }
            }
        });
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 每一秒触发一次
        timer.schedule(timerTask, 0, 1000);
        init(PATH,PATH2);
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null) {
                return;
            }
            if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            tvCurrent.setText(getTime(seekBar.getProgress()));
        }
    };

    public void play() {
        if (isPrepare) {
            mediaPlayer.start();
        }
    }
    /**
     * @param url url地址
     */
    public void init(String url,String url2) {
        //下载和在线播放同步进行，因为音频文件受网速和大小影响，不能确定哪种方法能最快速播放，当下载完在线播放还没能开始时，就可以使用下载文件播放了
        //①下载歌曲
        DownloadManager.getInstance(this).downloadFile(FileType.TYPE_AUDIO, "111", url, new OnDownloadingListener() {
            @Override
            public void onDownloadFailed(FileDownloadTask task, int errorType, String msg) {
                Log.e(TAG, "ERR: " + msg);
            }
            @Override
            public void onDownloadSucc(FileDownloadTask task, File outFile) {
                Log.e(TAG, "file : " + outFile.getAbsolutePath());
                if (!isPrepare) {
                    seekBar.setSecondaryProgress(seekBar.getMax());
                    try {
                        /****音乐播放****/
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(outFile.getAbsolutePath());
                        mediaPlayer.prepareAsync();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //下载歌词
        DownloadManager.getInstance(this).downloadFile(FileType.TYPE_AUDIO, "112", url2, new OnDownloadingListener() {
            @Override
            public void onDownloadFailed(FileDownloadTask task, int errorType, String msg) {
                Log.e(TAG, "ERR: " + msg);
            }
            @Override
            public void onDownloadSucc(FileDownloadTask task, File outFile) {
                Log.e(TAG, "file : " + outFile.getAbsolutePath());
                if (!isPrepare) {
                    try {
                        /**********歌词控件*****************/
                        ///歌词控件
                        //List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "test.lrc");//加载歌词文件
                        List<Lrc> lrcs = LrcHelper.parseLrcFromFile(new File(outFile.getAbsolutePath()));
                        mLrcView = findViewById(R.id.lrc_view);
                        mLrcView.setLrcData(lrcs);
                        mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
                            @Override
                            public void onPlay(long time, String content) {
                                mediaPlayer.seekTo((int) time);
                            }
                        });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //②在线播放
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(seekBar.getMax() * percent / 100);
        int currentProgress = seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.e(currentProgress + "% play", percent + " buffer");
        Log.e(TAG, "缓冲更新cur: " + mediaPlayer.getCurrentPosition() + " , dur: " + mediaPlayer.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "onCompletion");
        isPrepare = false;
        btnPlay.setText("开始");
        tvCurrent.setText("00:00:00");
        seekBar.setProgress(0);
        init(PATH,PATH2);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepare = true;
        seekBar.setEnabled(true);
        btnPlay.setEnabled(true);
        seekBar.setMax(mp.getDuration());
        tvDuration.setText(getTime(mp.getDuration()));
        Log.e(TAG, "onPrepared");
        Log.e(TAG, "总长: " + mp.getDuration());
    }

    private String getTime(int duration) {
        int i = duration / 1000;
        int h = i / (60 * 60);
        String sh;
        if (h == 0) {
            sh = "00";
        } else {
            sh = String.valueOf(h);
        }
        int m = i / 60 % 60;
        String sm;
        if (m == 0) {
            sm = "00";
        } else {
            sm = String.valueOf(m);
            if (sm.length() == 1) {
                sm = "0" + sm;
            }
        }
        int s = i % 60;
        String ss;
        if (s == 0) {
            ss = "00";
        } else {
            ss = String.valueOf(s);
            if (ss.length() == 1) {
                ss = "0" + ss;
            }
        }
        return sh + ":" + sm + ":" + ss;
    }
}