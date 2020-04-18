package com.androidlearing.servicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.androidlearing.servicedemo.interfaces.IPlayerControl;
import com.androidlearing.servicedemo.interfaces.IPlayerViewControl;
import com.androidlearing.servicedemo.presenter.PlayerPresenter;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo.service
 * @ClassName: PlayerService
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 16:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 16:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PlayerService extends Service {

    private static final String TAG = "PlayerService";
    private PlayerPresenter mPlayerPresenter;

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate...");
        super.onCreate();
        if (mPlayerPresenter == null) {
            mPlayerPresenter = new PlayerPresenter();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind...");
        return mPlayerPresenter;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy...");
        super.onDestroy();
        mPlayerPresenter = null;
    }
}
