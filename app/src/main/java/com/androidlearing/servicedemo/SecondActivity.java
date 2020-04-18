package com.androidlearing.servicedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import com.androidlearing.servicedemo.interfaces.ICommunication;
import com.androidlearing.servicedemo.service.SecondService;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo
 * @ClassName: SecondActivity
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 13:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 13:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

/**
 * 服务的开启方式有两种：
 * 1、startService-->stopService
 * 这种启动方式的优点：服务可以长期存在于后台运行
 * 缺点:不能够进行通讯
 * 生命周期：
 * 最基本的生命周期:
 * onCreate()-->onStartCommand()-->onDestroy();
 * 如果服务已经启动,就不再创建(onCreate)了,除非执行了onDestroy();
 * 2、 bindService 绑定服务，如果没有启动,自动启动
 * -->UnbindService 解绑服务
 * 优点：可以进行通讯。
 * 缺点：不可以长期处于后台运行。如果不解绑将发生泄漏leak
 * 如果解绑了服务将停止运行.
 * 生命周期：
 * onCreate()-->onBind()-->onUnBind()-->onDestroy();
 * 混合开启服务的生命周期
 * 1.开启服务,绑定服务.如果不取消绑定那么无法停止服务
 * 2.开启服务后,多次绑定-解绑服务,服务不会被停止，只能通过stopService()来停止
 * 推荐的混合开启服务方式：
 * 1 开启服务-->为了确保服务尅长期于后台运行
 * 2、绑定服务-->为了可以进行通讯
 * 3、调用服务内部的方法，如控制音乐播放器的播放/暂停/停止/快进
 * 4、退出Activity要记得解绑服务-->释放资源
 * 5、如果不使用服务了要让服务停止,那么就调用stopService()
 */
public class SecondActivity extends Activity {

    private boolean mIsBind;
    private ICommunication mCommunication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    /**
     * 开启服务
     * @param v
     */
    public void startServiceClick(View v){
        //开启服务
        Intent intent = new Intent(this, SecondService.class);
        startService(intent);
    }

    /**
     * 绑定服务
     * @param v
     */
    public void bindServiceClick(View v){
        //绑定服务
        Intent intent = new Intent(this,SecondService.class);
        //如果服务已启动,则不会启动.若没有启动则启动服务
        mIsBind = bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务绑定了,会返回binder
            mCommunication = (ICommunication) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务解绑了
            mCommunication = null;
        }
    };

    /**
     * 解绑服务
     * @param v
     */
    public void unbindServiceClick(View v){
        if (mIsBind&& mConnection!=null) {
            unbindService(mConnection);
            //
        }
    }

    /**
     * 调用服务内部方法
     * @param v
     */
    public void callServiceMethod(View v){
        //调用服务内部方法
        if (mConnection != null) {
            mCommunication.callServiceInnerMethod();
        }

    }

    /**
     * 停止服务
     * @param v
     */
    public void stopServiceClick(View v){
        //停止服务
        Intent intent =new Intent(this,SecondService.class);
        stopService(intent);

    }
}
