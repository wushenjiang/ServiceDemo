package com.androidlearing.servicedemo;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidlearing.servicedemo.interfaces.IPlayerControl;
import com.androidlearing.servicedemo.interfaces.IPlayerViewControl;
import com.androidlearing.servicedemo.service.PlayerService;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo
 * @ClassName: PlayerActivity
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 16:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 16:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PlayerActivity extends Activity {

    private static final String TAG = "PlayerActivity";
    private SeekBar mSeekBar;
    private Button mPlayOrPause;
    private Button mClose;
    private PlayerConnection mPlayerConnection;
    private IPlayerControl mIPlayerControl;
    private Boolean isUserTouchProgressBar = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        //设置相关的事件
        initEvent();
        //启动播放的服务
        initService();
        //绑定服务
        initBindService();
    }

    /**
     * 开启播放的服务
     */
    private void initService() {
        Log.d(TAG, "initService");
        startService(new Intent(this, PlayerService.class));
    }

    /**
     * 绑定服务
     */
    private void initBindService() {
        Log.d(TAG, "initBindService...");
        Intent intent = new Intent(this, PlayerService.class);
        if (mPlayerConnection == null) {
            mPlayerConnection = new PlayerConnection();
        }
        bindService(intent, mPlayerConnection, BIND_AUTO_CREATE);
    }

    private class PlayerConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIPlayerControl = (IPlayerControl) service;
            mIPlayerControl.registerViewController(mIPlayerViewControl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mIPlayerControl = null;
        }
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条发生改变
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //指针已经移上去了
                isUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                Log.d(TAG, "progress is -->" + progress);
                //停止拖动
                if (mIPlayerControl != null) {
                    mIPlayerControl.seekTo(progress);
                }
                isUserTouchProgressBar = false;
            }
        });
        mPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放或暂停
                if (mIPlayerControl != null) {
                    mIPlayerControl.playOrPause();
                }
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //停止按钮被点击
                if (mIPlayerControl != null) {
                    mIPlayerControl.stopPlay();
                }

            }
        });
    }


    /**
     * 初始化各控件
     */
    private void initView() {
        mSeekBar = this.findViewById(R.id.seek_bar);
        mPlayOrPause = this.findViewById(R.id.play_or_pause_btn);
        mClose = this.findViewById(R.id.close_btn);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mPlayerConnection != null) {
            //释放资源
            mIPlayerControl.unRegisterViewController();
            unbindService(mPlayerConnection);
            mPlayerConnection = null;
        }
    }

    private IPlayerViewControl mIPlayerViewControl = new IPlayerViewControl() {
        @Override
        public void onPlayStateChange(int state) {
            //我们要根据播放状态来修改UI
            switch (state) {
                case 1:
                    //播放中,修改按钮显示成暂停
                    mPlayOrPause.setText("暂停");
                    break;
                case 2:
                case 3:
                    mPlayOrPause.setText("播放");
                    break;
            }
        }

        @Override
        public void onSeekChange( final int seek) {
            //改变播放进度,条件:当用户的手触摸到进度条的时候,就不更新
            //这不是主线程,所有不可以用于更新UI
            //为什么更新进度不会崩溃呢？
            //在android里面有两个控件是可以用子线程进行更新的
            //progressBar和surfaceView
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isUserTouchProgressBar) {
                        mSeekBar.setProgress(seek);
                    }
                }
            });

        }
    };

    /**
     * 请求权限的方法
     */
    public void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission !=PackageManager.PERMISSION_GRANTED){
            //如果没有权限则向用户请求
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"获取权限结果-->"+requestCode);
    }
}
