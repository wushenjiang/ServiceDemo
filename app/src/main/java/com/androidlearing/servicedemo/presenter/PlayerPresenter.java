package com.androidlearing.servicedemo.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProvider;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.androidlearing.servicedemo.PlayerActivity;
import com.androidlearing.servicedemo.interfaces.IPlayerControl;
import com.androidlearing.servicedemo.interfaces.IPlayerViewControl;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo.presenter
 * @ClassName: PlayerPresenter
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 17:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 17:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PlayerPresenter extends Binder implements IPlayerControl {
    private static final String TAG = "PlayerPresenter";
    private IPlayerViewControl mViewController;
    private int mCurrentState = PLAY_STATE_PAUSE;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private SeekTimeTask mTimeTask;

    @Override
    public void registerViewController(IPlayerViewControl viewController) {
            this.mViewController = viewController;
    }

    @Override
    public void unRegisterViewController() {
        mViewController = null;
    }

    @Override
    public void playOrPause() {
        Log.d(TAG,"playOrPause...");
        if (mCurrentState == PLAY_STATE_PAUSE) {
            //创建播放器
            initPlayer();
            //设置数据源
            try {
                mMediaPlayer.setDataSource("mnt/sdcard/song.mp3");
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mCurrentState = PLAYER_STATE_PLAY;
                startTimer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(mCurrentState == PLAYER_STATE_PLAY){
            //如果当前状态是播放的,那么我们就暂停
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                mCurrentState = PLAY_STATE_PAUSE;
                stopTimer();
            }
        }else if(mCurrentState == PLAY_STATE_PAUSE){
            //如果当前状态是暂停,那么我们就继续播放
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                mCurrentState = PLAYER_STATE_PLAY;
            }
        }
        if (mViewController != null) {
            mViewController.onPlayStateChange(mCurrentState);
        }

    }

    private void initPlayer() {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Override
    public void stopPlay() {
        Log.d(TAG,"stopPlay...");
        if (mMediaPlayer != null&& mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mCurrentState = 3;
            //更新播放状态
            if(mViewController!=null){
                mViewController.onPlayStateChange(mCurrentState);
            }
            mMediaPlayer.release();
            stopTimer();
        }
    }

    @Override
    public void seekTo(int seek) {
        Log.d(TAG,"seekTo..."+seek);
        //0~100之间
        //需要做一个转换,得到的seek实际是一个百分比
        if(mMediaPlayer!= null){
            int tarSeek  = (int) (seek *1.0f/100 * mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(tarSeek);
        }
    }

    /**
     * 开启一个timerTask
     */
    private void startTimer(){

        if (mTimer == null) {
            mTimer = new Timer();
        }
        if(mTimeTask == null){
            mTimeTask = new SeekTimeTask();
        }
        mTimer.schedule(mTimeTask,0,500);
    }
    private void stopTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimeTask != null){
            mTimeTask.cancel();
            mTimer = null;
        }

    }
    private class SeekTimeTask extends TimerTask{

        @Override
        public void run() {
            //获取当前的播放进度
            if (mMediaPlayer != null &&mViewController!=null) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                Log.d(TAG,"current play position...."+mMediaPlayer.getCurrentPosition());
                int curPosition = (int) (currentPosition *1.0f / mMediaPlayer.getDuration()*100);
                mViewController.onSeekChange(curPosition);
            }

        }
    }
}
