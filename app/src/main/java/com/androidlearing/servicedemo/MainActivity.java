package com.androidlearing.servicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.androidlearing.servicedemo.interfaces.ICommunication;
import com.androidlearing.servicedemo.service.FirstService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean mIsServiceBinded;
    private ICommunication mICommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate...");
    }

    /**
     * 开启服务
     *
     * @param v
     */
    public void startServiceClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, FirstService.class);
        startService(intent);
    }

    /**
     * 停止服务
     *
     * @param v
     */
    public void stopServiceClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, FirstService.class);
        stopService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy...");
    }

    public void callServiceMethod(View v) {
        Log.d(TAG, "call service inner method");
        mICommunication.callServiceInnerMethod();
    }

    /**
     * 绑定服务
     *
     * @param v
     */
    public void bindServiceClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, FirstService.class);
        mIsServiceBinded =  bindService(intent,mConnection,BIND_AUTO_CREATE);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected...");
            mICommunication =(ICommunication) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected...");
            mICommunication = null;
        }
    };
    /**
     * 解绑服务
     *
     * @param v
     */
    public void unbindServiceClick(View v) {
        if (mConnection != null) {
            unbindService(mConnection);
        }

    }
}
