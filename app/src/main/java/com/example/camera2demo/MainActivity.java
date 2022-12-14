package com.example.camera2demo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camera2demo.PermissionsUtils.IPermissionsResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private AutoFitTextureView mTextureview;
    private Button mTakePictureBtn;//拍照
    private Button mVideoRecodeBtn;//开始录像
    private LinearLayout mVerticalLinear;
    private Button mTakePictureBtn2;//拍照 横,竖屏状态分别设置了一个拍照,录像的按钮
    private Button mVideoRecodeBtn2;//开始录像
    private LinearLayout mHorizontalLinear;
    private TextView mVHScreenBtn;
    private CameraController mCameraController;
    private boolean mIsRecordingVideo; //开始停止录像
    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/AAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_main);

        //Activity对象
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 创建监听权限的接口对象
     */
    private IPermissionsResult permissionsResult = new IPermissionsResult() {
        @Override
        public void passPermissons() {
            //授权后的操作
            //获取相机管理类的实例
            mCameraController = CameraController.getInstance(MainActivity.this);
            mCameraController.setFolderPath(BASE_PATH);

            initView();
            //判断当前横竖屏状态
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mVHScreenBtn.setText("竖屏");
            } else {
                mVHScreenBtn.setText("横屏");

            }
        }

        @Override
        public void forbitPermissons() {
            //未授权，请手动授权

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode,
                permissions, grantResults);

    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mTextureview = (AutoFitTextureView) findViewById(R.id.textureview);
        mTakePictureBtn = (Button) findViewById(R.id.take_picture_btn);
        mTakePictureBtn.setOnClickListener(this);
        mVideoRecodeBtn = (Button) findViewById(R.id.video_recode_btn);
        mVideoRecodeBtn.setOnClickListener(this);
        mVerticalLinear = (LinearLayout) findViewById(R.id.vertical_linear);
        mTakePictureBtn2 = (Button) findViewById(R.id.take_picture_btn2);
        mTakePictureBtn2.setOnClickListener(this);
        mVideoRecodeBtn2 = (Button) findViewById(R.id.video_recode_btn2);
        mVideoRecodeBtn2.setOnClickListener(this);
        mHorizontalLinear = (LinearLayout) findViewById(R.id.horizontal_linear);
        mVHScreenBtn = (TextView) findViewById(R.id.v_h_screen_btn);
        mVHScreenBtn.setOnClickListener(this);

        //判断当前屏幕方向
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //竖屏时
            mHorizontalLinear.setVisibility(View.VISIBLE);
            mVerticalLinear.setVisibility(View.GONE);
        } else {
            //横屏时
            mVerticalLinear.setVisibility(View.VISIBLE);
            mHorizontalLinear.setVisibility(View.GONE);
        }
        mCameraController.initCamera(mTextureview);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.take_picture_btn:
            case R.id.take_picture_btn2:
                mCameraController.takePicture();
                Toast.makeText(this, "相片已保存", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_recode_btn:
            case R.id.video_recode_btn2:
                if (mIsRecordingVideo) {
                    mIsRecordingVideo = false;//!mIsRecordingVideo
                    mCameraController.stopRecordingVideo();
                    mVideoRecodeBtn.setText("开始录像");
                    mVideoRecodeBtn2.setText("开始录像");
                    Toast.makeText(this, "录像结束", Toast.LENGTH_SHORT).show();
                } else {
                    mVideoRecodeBtn.setText("停止录像");
                    mVideoRecodeBtn2.setText("停止录像");
                    mIsRecordingVideo = true;//!mIsRecordingVideo
                    mCameraController.startRecordingVideo();
                    Toast.makeText(this, "录像开始", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.v_h_screen_btn:
                //判断当前屏幕方向
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    //切换竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //切换横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            default:
                break;
        }
    }


}