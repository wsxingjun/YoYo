package www.wsxingjun.com.yoyolibsdk.core.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;


import www.wsxingjun.com.yoyolibsdk.Activity.AdBrowserActivity;
import www.wsxingjun.com.yoyolibsdk.adutils.Utils;
import www.wsxingjun.com.yoyolibsdk.constant.SDKConstant;
import www.wsxingjun.com.yoyolibsdk.module.monitor.AdValue;
import www.wsxingjun.com.yoyolibsdk.report.ReportManager;
import www.wsxingjun.com.yoyolibsdk.widget.adbrowser.CustomVideoView;
import www.wsxingjun.com.yoyolibsdk.widget.adbrowser.VideoFullDialog;

/**
 * @function:
 */

public class VideoAdSlot implements CustomVideoView.ADVideoPlayerListener{

    private Context mContext;
    /**
     * UI
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;

    /**
     * Data
     */

    private AdValue mXAdInstance;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false; // 是否可自动暂停标志位
    private int lastArea = 0;  //防止要滑入滑出时播放器的状态的改变

    public VideoAdSlot(AdValue adInstance,
                       AdSDKSlotListener slotLitener,
                       CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mXAdInstance = adInstance;
        mSlotListener = slotLitener;
        mParentView = slotLitener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);
    }

    private void initVideoView(CustomVideoView.ADFrameImageLoadListener frameImageLoadListener) {
        mVideoView = new CustomVideoView(mContext,mParentView);
        if (mXAdInstance != null){
            mVideoView.setDataSource(mXAdInstance.resource);
            mVideoView.setListener(this);
        }
        mParentView.addView(mVideoView);

    }

    private boolean isPlaying(){
        if (mVideoView != null){
            return mVideoView.isPlaying();
        }
        return false;
    }

    private boolean isRealPause(){
        if (mVideoView != null){
            return mVideoView.isRealPause();
        }

        return false;
    }

    private boolean isComplete(){
        if (mVideoView != null){
            return mVideoView.isComplete();
        }

        return false;
    }


    private void pauseVideo(boolean isAuto){
        if (mVideoView != null){
            mVideoView.SeekAndPause(0);
        }
    }

    private void resumeVideo(){
        if (mVideoView != null){
            mVideoView.resume();
        }
    }

    //实现划入播放，滑出暂停的功能
    public void updateAdInScrollView(){
        int currentArea = Utils.getVisiblePercent(mParentView);

        //如果没有出现在屏幕上，不做任何的处理；
        if (currentArea <= 0){
            return;
        }

        if (Math.abs(currentArea - lastArea) >= 100){
            return;
        }
        //滑动没有超过屏幕的50%
        if (currentArea <= SDKConstant.VIDEO_SCREEN_PERCENT){
            if (canPause){
                pauseVideo(true);
                canPause = false; //用户的手抖，这里设置只暂停一次；
            }

            lastArea = 0;
            mVideoView.setIsComplete(false); // 滑动出50%后标记为从头开始播
            mVideoView.setIsRealPause(false); //以前叫setPauseButtonClick()
            return;
        }

        //当视频进入真正的暂停状态时
        if (isRealPause() || isComplete()){
            pauseVideo(false);
            canPause   = false;
            return;
        }

        if (Utils.canAutoPlay(mContext,SDKConstant.getCurrentSetting())
                || isPlaying()){
            lastArea = currentArea;
            resumeVideo();
            canPause = true;
            mVideoView.setIsRealPause(false);
        }else {
            pauseVideo(false);
            mVideoView.setIsRealPause(true);
        }
    }

    public void destroy() {
        mVideoView.destroy();
        mVideoView = null;
        mContext = null;
        mXAdInstance = null;
    }

    //全屏按钮的调用
    @Override
    public void OnClickFullScreenBtn() {

        //获取videoview在当前界面的属性
        Bundle bundle = Utils.getViewProperty(mParentView);
        mParentView.removeView(mVideoView);//将播放器从view中移除；
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mXAdInstance, mVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPostion(int position) {
                backToSmallMode(position);//在全屏幕播放的时候点击了回调；
            }

            @Override
            public void playComplete() {
                bigPlayCommplete(); //全屏播放完成后的事件的回调
            }


        });
        dialog.setViewBundle(bundle); //为Dialog设置播放器数据Bundle对象
        dialog.setSlotListener(mSlotListener);
        dialog.show();

    }


    //返回小屏幕的时候
    private void backToSmallMode(int position) {
        if (mVideoView.getParent() == null){
            mParentView.addView(mVideoView);
        }
        mVideoView.isShowFullBtn(true);//显示全屏按钮；
        mVideoView.mute(false);
        mVideoView.setListener(this);//重新设置监听为我们的业务逻辑层；
        mVideoView.SeekAndResume(position);//使播放器跳到指定位置并播放；
        canPause = true; //可自动播放；
    }

    //全屏播放结束时的事件回调；
    private void bigPlayCommplete() {
        if (mVideoView.getParent() == null){
            mParentView.addView(mVideoView);
        }
        mVideoView.setTranslationY(0); //防止动画导致偏离父容器
        mVideoView.isShowFullBtn(true);
        mVideoView.mute(true);
        mVideoView.setListener(this);
        mVideoView.SeekAndPause(0);
        canPause = false;
    }







    @Override
    public void OnClickVideo() {
        String desationUrl = mXAdInstance.clickUrl;
        //跳转都web页面
        if (!TextUtils.isEmpty(desationUrl)){
            Intent intent = new Intent(mContext, AdBrowserActivity.class);
            intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
            mContext.startActivity(intent);
        }

    }

    @Override
    public void OnClickBackBtn() {

    }

    @Override
    public void OnClickPlay() {

    }

    //视频加载成功
    @Override
    public void OnAdVideoLoadSuccess() {
        if (mSlotListener != null){
            mSlotListener.onAdVideoLoadSuccess();
        }

    }
    //视频加载失败
    @Override
    public void OnAdVideoLoadFailed() {
        if (mSlotListener != null){
            mSlotListener.onAdVideoLoadFailed();
        }
        // 加载失败回到初始的状态；
        canPause = false;

    }

    //视频播放结束
    @Override
    public void OnAdVideoLoadComplete() {
//        ReportManager.suReport( mXAdInstance.endMonitor,false,getDuration());

        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadComplete();
        }
        mVideoView.setIsRealPause(true);
    }

    @Override
    public void OnBufferUpdate(int time) {
//        ReportManager.suReport(mXAdInstance.middleMonitor, false,time / SDKConstant.MILLION_UNIT);
    }




    //获取播放器当前的秒数；
    private int getPosition(){
        return mVideoView.getCurrentPosition() /SDKConstant.MILLION_UNIT;
    }
    //获取播放器总时长；
    private int getDuration(){
        return mVideoView.getDuration() /SDKConstant.MILLION_UNIT;
    }





    //传递消息到appcontext层
    public interface AdSDKSlotListener {

        public ViewGroup getAdParent();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoLoadComplete();

        public void onClickVideo(String url);
    }
}
