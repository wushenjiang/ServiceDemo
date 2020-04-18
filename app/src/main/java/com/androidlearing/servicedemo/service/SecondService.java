package com.androidlearing.servicedemo.service;

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
 * @ClassName: SecondService
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 14:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 14:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SecondService extends Service {
    private static final String TAG = "SecondService";
    private class InnerBinder  extends Binder implements ICommunication {


        @Override
        public void callServiceInnerMethod() {
            serviceInnerMethod();
        }
    }
    @Nullable
    @Override
    /**
     * 服务绑定
     */
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind...");
        InnerBinder innerBinder = new InnerBinder();
        return innerBinder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate...");
        super.onCreate();
    }

    /**
     * 服务启动弄
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand...");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 解绑
     * @param intent
     * @return
     */

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind...");
        return super.onUnbind(intent);
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy...");
        super.onDestroy();
    }
    private void serviceInnerMethod(){
        Log.d(TAG,"serviceInnerMethod...");
        Toast.makeText(this,"服务内部方法调用了",Toast.LENGTH_SHORT).show();
    }
}
