package com.androidlearing.servicedemo.interfaces;

/**
 * @ProjectName: ServiceDemo
 * @Package: com.androidlearing.servicedemo.interfaces
 * @ClassName: IPlayerViewControl
 * @Description: java类作用描述
 * @Author: 武神酱丶
 * @CreateDate: 2020/4/18 16:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 16:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IPlayerViewControl {
    /**
     * 播放状态改变的通知
     */
    void onPlayStateChange(int state);

    /**
     * 播放进度的改变
     * @param seek
     */
    void onSeekChange(int seek);
}
