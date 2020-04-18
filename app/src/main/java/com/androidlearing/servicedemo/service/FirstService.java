package com.androidlearing.servicedemo.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidlearing.servicedemo.interfaces.ICommunication;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo.service
 * @ClassName: FirstService
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/17 18:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/17 18:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FirstService extends Service {

    private static final String TAG = FirstService.class.getName();
    private class InnerBinder extends Binder implements ICommunication {
        @Override
        public void callServiceInnerMethod(){
            sayHello();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind...");
        return new InnerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate...");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand...");
        return super.onStartCommand(intent, flags, startId);
    }
    private void sayHello(){
        Toast.makeText(this,"Hello!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

}
