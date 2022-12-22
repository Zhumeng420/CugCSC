package com.example.cugcsc;

import static com.example.cugcsc.tool.HttpUtils.UploadImage;
import static com.example.cugcsc.tool.toast.ErrorToast;
import static com.example.cugcsc.tool.toast.SuccessToast;
import static com.example.cugcsc.tool.toast.WarnToast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugcsc.UserCenter.GlobalUserState;
import com.example.cugcsc.UserCenter.post.Async.PostEmotionByAsync;
import com.example.cugcsc.data.PostType;
import com.example.cugcsc.tool.ViewUtil;
import com.example.cugcsc.view.ColorPickerView;
import com.example.cugcsc.view.RichEditor;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.net.MalformedURLException;
import java.net.URL;

public class EmotionActivity extends AppCompatActivity implements  View.OnClickListener {
    /********************View**********************/
    //文本编辑器
    private RichEditor mEditor;
    //加粗按钮
    private ImageView mBold;
    //颜色编辑器
    private TextView mTextColor;
    //显示显示View
    private LinearLayout llColorView;
    //预览按钮
    private TextView mPreView;
    //图片按钮
    private TextView mImage;
    //按序号排列（ol）
    private ImageView mListOL;
    //按序号排列（ul）
    private ImageView mListUL;
    //字体下划线
    private ImageView mLean;
    //字体倾斜
    private ImageView mItalic;
    //字体左对齐
    private ImageView mAlignLeft;
    //字体右对齐
    private ImageView mAlignRight;
    //字体居中对齐
    private ImageView mAlignCenter;
    //字体索引
    private ImageView mBlockquote;
    //字体中划线
    private ImageView mStrikethrough;
    //保存博客
    private TextView saveBlog;
    //表情
    private ImageView emoj;
    //标题
    private EditText title;
    //表情抽屉
    private View emojContainer;
    //抽屉状态
    BottomSheetBehavior<View> behavior;
    /********************boolean开关**********************/
    //是否加粗
    boolean isClickBold = false;
    //是否正在执行动画
    boolean isAnimating = false;
    //是否按ol排序
    boolean isListOl = false;
    //是否按ul排序
    boolean isListUL = false;
    //是否下划线字体
    boolean isTextLean = false;
    //是否下倾斜字体
    boolean isItalic = false;
    //是否左对齐
    boolean isAlignLeft = false;
    //是否右对齐
    boolean isAlignRight = false;
    //是否中对齐
    boolean isAlignCenter = false;
    //是否索引
    boolean isBlockquote = false;
    //字体中划线
    boolean isStrikethrough = false;
    //图片路径
    String path;
    String url;
    /********************变量**********************/
    //折叠视图的宽高
    private int mFoldedViewMeasureHeight;
    private boolean flag;
    public String imageUrl="";
    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
    /*****************表情***************************/
    private ImageView cool;
    private ImageView shutup;
    private ImageView afraid;
    private ImageView laughtOut;
    private ImageView laugh;
    private ImageView laugh2;
    private ImageView afread2;
    private ImageView cool2;
    private ImageView shutup2;
    private ImageView afread3;
    private ImageView laughtout2;
    private ImageView noemo;
    private ImageView poisoning;
    private ImageView like;
    private ImageView noemo2;
    private ImageView think;
    private ImageView badlaugh;
    private ImageView sad;
    private ImageView evil;
    private ImageView love;
    private ImageView confuse;
    private ImageView mask;
    private ImageView cry;
    private ImageView sad2;
    private ImageView angry;
    private ImageView kiss;
    private ImageView lookdown;
    private ImageView hurt;
    private ImageView noemo3;
    private ImageView cry2;
    private ImageView split;
    private ImageView kiss2;
    private ImageView smile;
    private ImageView dizzy;
    private ImageView poisoning2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        initView();
        initEmoj();
        initClickListener();
        flag=true;
        /*************文件读写权限*************/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
                Environment.isExternalStorageManager()
        ) {
            SuccessToast(this,"获取到文件访问权限");
        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("成功");
            builder.setMessage("修改密码成功");
            AlertDialog dialog=builder.create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(intent);
                        }
                    });
            dialog.show();
        }
        flag=false;
        /************标题透明***********/
        View v = findViewById(R.id.title);
        v.getBackground().setAlpha(100);//0~255透明度值 ，0为完全透明，255为不透明
    }

    /**
     * 初始化View
     */
    private void initView() {
        initEditor();
        initMenu();
        initColorPicker();
    }

    /**
     * 初始化表情
     */
    private void initEmoj(){
        cool=findViewById(R.id.cool);
        cool.setOnClickListener(this);
        shutup=findViewById(R.id.shutup);
        shutup.setOnClickListener(this);
        laughtOut=findViewById(R.id.laught_out);
        laughtOut.setOnClickListener(this);
        afraid=findViewById(R.id.afraid);
        afraid.setOnClickListener(this);
        laugh=findViewById(R.id.laugh);
        laugh.setOnClickListener(this);
        laugh2=findViewById(R.id.laugh2);
        laugh2.setOnClickListener(this);
        afread2=findViewById(R.id.afread2);
        afread2.setOnClickListener(this);
        cool2=findViewById(R.id.cool2);
        cool2.setOnClickListener(this);
        shutup2=findViewById(R.id.shutup2);
        shutup2.setOnClickListener(this);
        afread3=findViewById(R.id.afraid3);
        afread3.setOnClickListener(this);
        laughtout2=findViewById(R.id.laught_out2);
        laughtout2.setOnClickListener(this);
        noemo=findViewById(R.id.noemo);
        noemo.setOnClickListener(this);
        poisoning=findViewById(R.id.poisoning);
        poisoning.setOnClickListener(this);
        like=findViewById(R.id.like);
        like.setOnClickListener(this);
        noemo2=findViewById(R.id.noemo2);
        noemo2.setOnClickListener(this);
        think=findViewById(R.id.think);
        think.setOnClickListener(this);
        badlaugh=findViewById(R.id.badlaugh);
        badlaugh.setOnClickListener(this);
        sad=findViewById(R.id.sad);
        sad.setOnClickListener(this);
        evil=findViewById(R.id.evil);
        evil.setOnClickListener(this);
        love=findViewById(R.id.love);
        love.setOnClickListener(this);
        confuse=findViewById(R.id.confuse);
        confuse.setOnClickListener(this);
        mask=findViewById(R.id.mask);
        mask.setOnClickListener(this);
        cry=findViewById(R.id.cry);
        cry.setOnClickListener(this);
        sad2=findViewById(R.id.sad2);
        sad2.setOnClickListener(this);
        angry=findViewById(R.id.angry);
        angry.setOnClickListener(this);
        kiss=findViewById(R.id.kiss);
        kiss.setOnClickListener(this);
        lookdown=findViewById(R.id.lookdown);
        lookdown.setOnClickListener(this);
        hurt=findViewById(R.id.hurt);
        hurt.setOnClickListener(this);
        noemo3=findViewById(R.id.noemo3);
        noemo3.setOnClickListener(this);
        cry2=findViewById(R.id.cry2);
        cry2.setOnClickListener(this);
        split=findViewById(R.id.split);
        split.setOnClickListener(this);
        kiss2=findViewById(R.id.kiss2);
        kiss2.setOnClickListener(this);
        smile=findViewById(R.id.smile);
        smile.setOnClickListener(this);
        dizzy=findViewById(R.id.dizzy);
        dizzy.setOnClickListener(this);
        poisoning2=findViewById(R.id.poisoning2);
        poisoning2.setOnClickListener(this);
    }
    /**
     * 初始化文本编辑器
     */
    private void initEditor() {
        mEditor = findViewById(R.id.re_main_editor);
        //mEditor.setEditorHeight(400);
        //输入框显示字体的大小
        mEditor.setEditorFontSize(18);
        //输入框显示字体的颜色
        mEditor.setEditorFontColor(Color.BLACK);
        //输入框背景设置
        mEditor.setEditorBackgroundColor(Color.WHITE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        //输入框文本padding
        mEditor.setPadding(10, 10, 10, 10);
        //输入提示文本
        mEditor.setPlaceholder("请输入编辑内容");
        //是否允许输入
        //mEditor.setInputEnabled(false);
        //文本输入框监听事件
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d("mEditor", "html文本：" + text);
            }
        });

    }

    /**
     * 初始化颜色选择器
     */
    private void initColorPicker() {
        ColorPickerView right = findViewById(R.id.cpv_main_color);
        right.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                mTextColor.setBackgroundColor(color);
                mEditor.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(ColorPickerView picker) {

            }

            @Override
            public void onStopTrackingTouch(ColorPickerView picker) {

            }
        });
    }

    /**
     * 初始化菜单按钮
     */
    private void initMenu() {
        mBold = findViewById(R.id.button_bold);
        mTextColor = findViewById(R.id.button_text_color);
        llColorView = findViewById(R.id.ll_main_color);
        mPreView = findViewById(R.id.tv_main_preview);
        mImage = findViewById(R.id.button_image);
        mListOL = findViewById(R.id.button_list_ol);
        mListUL = findViewById(R.id.button_list_ul);
        mLean = findViewById(R.id.button_underline);
        mItalic = findViewById(R.id.button_italic);
        mAlignLeft = findViewById(R.id.button_align_left);
        mAlignRight = findViewById(R.id.button_align_right);
        mAlignCenter = findViewById(R.id.button_align_center);
        mBlockquote = findViewById(R.id.action_blockquote);
        mStrikethrough = findViewById(R.id.action_strikethrough);
        saveBlog=findViewById(R.id.save_blog);
        emoj=findViewById(R.id.emoj);
        title=findViewById(R.id.title);
        emojContainer=findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(emojContainer);
        getViewMeasureHeight();
    }

    /**
     * 获取控件的高度
     */
    private void getViewMeasureHeight() {
        //获取像素密度
        float mDensity = getResources().getDisplayMetrics().density;
        //获取布局的高度
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        llColorView.measure(w, h);
        int height = llColorView.getMeasuredHeight();
        mFoldedViewMeasureHeight = (int) (mDensity * height + 0.5);
    }

    private void initClickListener() {
        mBold.setOnClickListener( this);
        mTextColor.setOnClickListener( this);
        mPreView.setOnClickListener( this);
        mImage.setOnClickListener(this);
        mListOL.setOnClickListener(this);
        mListUL.setOnClickListener(this);
        mLean.setOnClickListener( this);
        mItalic.setOnClickListener(this);
        mAlignLeft.setOnClickListener( this);
        mAlignRight.setOnClickListener(this);
        mAlignCenter.setOnClickListener( this);
        mBlockquote.setOnClickListener( this);
        mStrikethrough.setOnClickListener(this);
        saveBlog.setOnClickListener(this);
        emoj.setOnClickListener(this);
        emojContainer.setOnClickListener(this);
        title.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_bold) {//字体加粗
            if (isClickBold) {
                mBold.setImageResource(R.mipmap.bold);
            } else {  //加粗
                mBold.setImageResource(R.mipmap.bold_);
            }
            isClickBold = !isClickBold;
            mEditor.setBold();
        } else if (id == R.id.button_text_color) {//设置字体颜色
            //如果动画正在执行,直接return,相当于点击无效了,不会出现当快速点击时,
            // 动画的执行和ImageButton的图标不一致的情况
            if (isAnimating) return;
            //如果动画没在执行,走到这一步就将isAnimating制为true , 防止这次动画还没有执行完毕的
            //情况下,又要执行一次动画,当动画执行完毕后会将isAnimating制为false,这样下次动画又能执行
            isAnimating = true;
            if (llColorView.getVisibility() == View.GONE) {
                //打开动画
                animateOpen(llColorView);
            } else {
                //关闭动画
                animateClose(llColorView);
            }
        } else if (id == R.id.button_image) {//插入图片
            //这里的功能需要根据需求实现，通过insertImage传入一个URL或者本地图片路径都可以，这里用户可以自己调用本地相
            //或者拍照获取图片，传图本地图片路径，也可以将本地图片路径上传到服务器（自己的服务器或者免费的七牛服务器），
            //返回在服务端的URL地址，将地址传如即可（我这里传了一张写死的图片URL，如果你插入的图片不现实，请检查你是否添加
            // 网络请求权限<uses-permission android:name="android.permission.INTERNET" />）
                /*Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);*/
            //System.out.println("插入内容："+this.imageUrl);
            flag=true;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            this.startActivityForResult(intent, 0x005);
            //mEditor.insertImage(url, "dachshund");
        }else if(id==R.id.emoj){//如果是选择表情包
            int state = behavior.getState();
            if (state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }else{
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            /**************如果输入法打开了要关闭输入法****************/
            ViewUtil.hideOneInputMethod(this,mEditor);
        } else if (id == R.id.button_list_ol) {
            if (isListOl) {
                mListOL.setImageResource(R.mipmap.list_ol);
            } else {
                mListOL.setImageResource(R.mipmap.list_ol_);
            }
            isListOl = !isListOl;
            mEditor.setNumbers();
        } else if (id == R.id.button_list_ul) {
            if (isListUL) {
                mListUL.setImageResource(R.mipmap.list_ul);
            } else {
                mListUL.setImageResource(R.mipmap.list_ul_);
            }
            isListUL = !isListUL;
            mEditor.setBullets();
        } else if (id == R.id.button_underline) {
            if (isTextLean) {
                mLean.setImageResource(R.mipmap.underline);
            } else {
                mLean.setImageResource(R.mipmap.underline_);
            }
            isTextLean = !isTextLean;
            mEditor.setUnderline();
        } else if (id == R.id.button_italic) {
            if (isItalic) {
                mItalic.setImageResource(R.mipmap.lean);
            } else {
                mItalic.setImageResource(R.mipmap.lean_);
            }
            isItalic = !isItalic;
            mEditor.setItalic();
        } else if (id == R.id.button_align_left) {
            if (isAlignLeft) {
                mAlignLeft.setImageResource(R.mipmap.align_left);
            } else {
                mAlignLeft.setImageResource(R.mipmap.align_left_);
            }
            isAlignLeft = !isAlignLeft;
            mEditor.setAlignLeft();
        } else if (id == R.id.button_align_right) {
            if (isAlignRight) {
                mAlignRight.setImageResource(R.mipmap.align_right);
            } else {
                mAlignRight.setImageResource(R.mipmap.align_right_);
            }
            isAlignRight = !isAlignRight;
            mEditor.setAlignRight();
        } else if (id == R.id.button_align_center) {
            if (isAlignCenter) {
                mAlignCenter.setImageResource(R.mipmap.align_center);
            } else {
                mAlignCenter.setImageResource(R.mipmap.align_center_);
            }
            isAlignCenter = !isAlignCenter;
            mEditor.setAlignCenter();
        } else if (id == R.id.action_blockquote) {
            if (isBlockquote) {
                mBlockquote.setImageResource(R.mipmap.blockquote);
            } else {
                mBlockquote.setImageResource(R.mipmap.blockquote_);
            }
            isBlockquote = !isBlockquote;
            mEditor.setBlockquote();
        } else if (id == R.id.action_strikethrough) {
            if (isStrikethrough) {
                mStrikethrough.setImageResource(R.mipmap.strikethrough);
            } else {
                mStrikethrough.setImageResource(R.mipmap.strikethrough_);
            }
            isStrikethrough = !isStrikethrough;
            mEditor.setStrikeThrough();
        }
        else if (id == R.id.tv_main_preview) {//预览
            Intent intent = new Intent(EmotionActivity.this, WebDataActivity.class);
            intent.putExtra("diarys", mEditor.getHtml());
            startActivity(intent);
        }
        else if(id == R.id.save_blog){
                String title_text=title.getText().toString();//获取标题
                String content=mEditor.getHtml();//获取内容
                AlertDialog alert=new AlertDialog.Builder(this).create();
                alert.setTitle("系统提示");//设置对话框的标题
                alert.setMessage("是否确定发布？");//设置对话框显示的内容
                //添加“取消”按钮
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                WarnToast(EmotionActivity.this, "取消发布");
                            }
                        });
                //添加“确定”按钮
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //以下向数据库保存博客
                                System.out.println(GlobalUserState.UserPhone);
                                String table="";
                                if(PostType.type==1){
                                    table="school";
                                }else if(PostType.type==2){
                                    table="emotion";
                                }else if(PostType.type==3){
                                    table="interest";
                                }else if(PostType.type==4){
                                    table="study";
                                }
                                PostEmotionByAsync task=new PostEmotionByAsync(EmotionActivity.this,table,title_text,content, GlobalUserState.UserPhone);
                                task.execute(0);
                                //SuccessToast(EmotionActivity.this, "发布成功");
                            }
                        });
                alert.show();//显示对话框
        } else if(id==R.id.cool){
            insertEmoj("http://81.70.13.188:9000/cugsdn/cool_1670915706169.png");
            fold();
        }else if(id==R.id.shutup){
            insertEmoj("http://81.70.13.188:9000/cugsdn/shutup_1670979004155.png");
            fold();
        }else if(id==R.id.afraid){
            insertEmoj("http://81.70.13.188:9000/cugsdn/afraid_1671065733952.png");
            fold();
        }else if(id==R.id.laught_out){
            insertEmoj("http://81.70.13.188:9000/cugsdn/laguhtout_1671065796047.png");
            fold();
        }else if(id==R.id.laugh){
            insertEmoj("http://81.70.13.188:9000/cugsdn/laugh_1671065983258.png");
            fold();
        }else if(id==R.id.laugh2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/laugh2_1671066014596.png");
            fold();
        }else if(id==R.id.afread2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/afread2_1671066065502.png");
            fold();
        }else if(id==R.id.cool2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/cool2_1671109971394.png");
            fold();
        }else if(id==R.id.shutup2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/shutup2_1671110018591.png");
            fold();
        }else if(id==R.id.afraid3){
            insertEmoj("http://81.70.13.188:9000/cugsdn/afread3_1671110035938.png");
            fold();
        }else if(id==R.id.laught_out2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/laughtout2_1671110058186.png");
            fold();
        }else if(id==R.id.noemo){
            insertEmoj("http://81.70.13.188:9000/cugsdn/noemo_1671110090157.png");
            fold();
        }else if(id==R.id.poisoning){
            insertEmoj("http://81.70.13.188:9000/cugsdn/poisoning_1671110127069.png");
            fold();
        }else if(id==R.id.like){
            insertEmoj("http://81.70.13.188:9000/cugsdn/like_1671110152161.png");
            fold();
        }else if(id==R.id.noemo2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/noemo2_1671110180177.png");
            fold();
        }else if(id==R.id.think){
            insertEmoj("http://81.70.13.188:9000/cugsdn/think_1671110222389.png");
            fold();
        }else  if(id==R.id.badlaugh){
            insertEmoj("http://81.70.13.188:9000/cugsdn/badlaugh_1671111716502.png");
            fold();
        }else if(id==R.id.sad){
            insertEmoj("http://81.70.13.188:9000/cugsdn/sad_1671111747077.png");
            fold();
        }else if(id==R.id.evil){
            insertEmoj("http://81.70.13.188:9000/cugsdn/evil_1671111777708.png");
            fold();
        }else if(id==R.id.love){
            insertEmoj("http://81.70.13.188:9000/cugsdn/love_1671111791798.png");
            fold();
        }else if(id==R.id.confuse){
            insertEmoj("http://81.70.13.188:9000/cugsdn/confuse_1671111813923.png");
            fold();
        }else if(id==R.id.mask){
            insertEmoj("http://81.70.13.188:9000/cugsdn/mask_1671111832301.png");
            fold();
        }else if(id==R.id.cry){
            insertEmoj("http://81.70.13.188:9000/cugsdn/cry_1671111894557.png");
            fold();
        }else if(id==R.id.sad2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/sad2_1671111959077.png");
            fold();
        }else if(id==R.id.angry){
            insertEmoj("http://81.70.13.188:9000/cugsdn/angry_1671111992982.png");
            fold();
        }else if(id==R.id.kiss){
            insertEmoj("http://81.70.13.188:9000/cugsdn/kiss_1671112014042.png");
            fold();
        }else if(id==R.id.lookdown){
            insertEmoj("http://81.70.13.188:9000/cugsdn/lookdown_1671112037338.png");
            fold();
        }else if(id==R.id.hurt){
            insertEmoj("http://81.70.13.188:9000/cugsdn/hurt_1671112059609.png");
            fold();
        }else if(id==R.id.cry2){
            insertEmoj("http://81.70.13.188:9000/cugsdn/cry2_1671112199091.png");
            fold();
        }
    }
    /***折叠表情栏***/
    private void fold(){
        int state = behavior.getState();
        if (state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else{
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    /***插入表情*****/
    private void insertEmoj(String url){
        try {
            mEditor.insertEmotion(new URL(url), "dachshund");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x005) {
            // 从相册返回的数据
            /*Log.e(this.getClass().getName(), "Result:" + data.toString());
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                mEditor.insertImage(uri, "dachshund");
            }*/
            if (data == null) {
                // 用户未选择任何文件，直接返回
                return;
            }
            /******************以下根据Uri获取文件的真实路径********************************/
            Uri uri = data.getData(); // 获取用户选择文件的URI
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    path = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            //path = UriUtil.getPath(mine.this, uri);
            System.out.println("测试文件上传"+data.getData());
            System.out.println("测试路径"+path);
            new Thread(() -> {//更新数据库
                url=UploadImage(path);
                handler.sendEmptyMessage(1);//通知主线程更新控件
            }).start();
            //mEditor.insertImage(ImageUrl.image_url, "dachshund");
        }
    }
    //handler为线程之间通信的桥梁
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    System.out.println("测试是否进入");
                    URL aim=null;
                    try {
                        aim=new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    mEditor.insertImage(aim, "dachshund");
                    break;
                default :
                    break;
            }
        }

    };

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    /**
     * 开启动画
     *
     * @param view 开启动画的view
     */
    private void animateOpen(LinearLayout view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0, mFoldedViewMeasureHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        animator.start();
    }

    /**
     * 关闭动画
     *
     * @param view 关闭动画的view
     */
    private void animateClose(final LinearLayout view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                isAnimating = false;
            }
        });
        animator.start();
    }


    /**
     * 创建动画
     *
     * @param view  开启和关闭动画的view
     * @param start view的高度
     * @param end   view的高度
     * @return ValueAnimator对象
     */
    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
