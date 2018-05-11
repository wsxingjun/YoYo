package www.wsxingjun.com.yoyolibsdk.core.video;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import www.oztaking.com.yoyo.zxing.util.Util;
import www.wsxingjun.com.yoyolibsdk.Activity.AdBrowserActivity;
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
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;

    /**
     * Data
     */

    private AdValue mVideoInfo;
    private ADSDKSlotListener mSlotListener;
    private boolean canPause = false; // 是否可自动暂停标志位
    private int lastArea = 0;  //防止要滑入滑出时播放器的状态的改变

    public VideoAdSlot(AdValue adInstance, ADSDKSlotListener slotListener) {
        mVideoInfo = adInstance;
        this.mSlotListener = slotListener;
        mParentView = slotListener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView();
    }

    private void initVideoView() {
        mVideoView = new CustomVideoView(mContext,mParentView);
        if (mVideoInfo != null){
            mVideoView.setDataSource(mVideoInfo.resource);
            mVideoView.setListener(this);
        }
        mParentView.addView(mVideoView);

    }




    @Override
    public void OnBufferUpdate(int time) {
        ReportManager.suReport(mVideoInfo.middleMonitor, false,time / SDKConstant.MILLION_UNIT);
    }


    //全屏按钮的调用
    @Override
    public void OnClickFullScreenBtn() {
        mParentView.removeView(mVideoView);//将播放器从view中移除；
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mVideoInfo, mVideoView.getCurrentPosition());
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


    }

    @Override
    public void OnClickVideo() {
        String desationUrl = mVideoInfo.clickUrl;
        //跳转都web页面
        if (!TextUtils.isEmpty(desationUrl)){
            Intent intent = new Intent(mContext, AdBrowserActivity.class);
            intent.putExtra(AdBrowserActivity.KEY_URL,mVideoInfo.clickUrl);
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
        ReportManager.suReport( mVideoInfo.endMonitor,false,getDuration());

    }

    public interface ADSDKSlotListener{
        public ViewGroup getAdParent();
        public void onAdVideoLoadSuccess();
        public void onAdVideoLoadFailed();

    }


    //获取播放器当前的秒数；
    private int getPosition(){
        return mVideoView.getCurrentPosition() /SDKConstant.MILLION_UNIT;
    }
    //获取播放器总时长；
    private int getDuration(){
        return mVideoView.getDuration() /SDKConstant.MILLION_UNIT;
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


    private void pauseVideo(){
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
    public void updateVideoInScrollView(){
        int currentArea = Util.getVisiblePercent(mParentView);

        //如果没有出现在屏幕上，不做任何的处理；
        if (currentArea <= 0){
            return;
        }

        if (Math.abs(currentArea - lastArea) >= 100){
            return;
        }

        if (currentArea <= SDKConstant.VIDEO_SCREEN_PERCENT){
            if (canPause){
                pauseVideo();
                canPause = false; //用户的手抖，这里设置只暂停一次；
            }
        }
        //滑动没有超过屏幕的50%
        if (currentArea < SDKConstant.VIDEO_SCREEN_PERCENT){
            if (canPause){
                pauseVideo();
                canPause = false;
            }

            lastArea = 0;
            mVideoView.setIsComplete(false);
            mVideoView.setIsRealPause(false);
            return;
        }

        //当视频进入真正的暂停状态时
        if (isRealPause() || isComplete()){
            pauseVideo();
            canPause   = false;
        }

        if (Util.canAutoPlay(mContext,SDKConstant.getCurrentSetting())){
            resumeVideo();
        }else {
            pauseVideo();
            mVideoView.setIsRealPause(true);
        }
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
