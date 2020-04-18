package com.androidlearing.servicedemo.interfaces;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo.interfaces
 * @ClassName: IPlayerControl
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 16:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 16:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IPlayerControl {
    //播放状态
    //播放
   int PLAYER_STATE_PLAY  = 1;
    int PLAY_STATE_PAUSE  = 2;
    int PLAYER_STATE_RESUME = 3;
    /**
     * 把UI的控制接口设置给逻辑层
     * @param viewController
     */
    void registerViewController(IPlayerViewControl viewController);

    /**
     * 取消接口通知的注册
     */

    void unRegisterViewController();
    /**
     * 播放音乐
     */
    void playOrPause();



    /**
     * 停止播放
     */
   void stopPlay();

    /**
     * 设置播放进度
     * @param seek  播放进度
     */
   void seekTo(int seek);
}
