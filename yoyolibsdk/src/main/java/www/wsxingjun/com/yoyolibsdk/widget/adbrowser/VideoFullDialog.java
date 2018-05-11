package www.wsxingjun.com.yoyolibsdk.widget.adbrowser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import www.wsxingjun.com.yoyolibsdk.Activity.AdBrowserActivity;
import www.wsxingjun.com.yoyolibsdk.R;
import www.wsxingjun.com.yoyolibsdk.constant.SDKConstant;
import www.wsxingjun.com.yoyolibsdk.core.video.VideoAdSlot;
import www.wsxingjun.com.yoyolibsdk.module.monitor.AdValue;
import www.wsxingjun.com.yoyolibsdk.report.ReportManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * @function 全屏显示播放视频的界面：使用Dialog，节省资源；
 */

public class VideoFullDialog extends Dialog implements CustomVideoView.ADVideoPlayerListener{

    private Context mContext;
    /**
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;
    private ImageView mBackButtom; //Back Button

    /**
     * Data
     */

    private AdValue mXAdInstance;
    private FullToSmallListener mListener;//通信的接口回调；
    private int mPosition;//从小屏到全屏播放位置的记录；
    private boolean isFirst = true; //适配是解决的bug

    private VideoAdSlot.AdSDKSlotListener mSlotListener;


    public VideoFullDialog(@NonNull Context context,
                           CustomVideoView videoView,AdValue instance,
                            int position) {
        super(context, R.style.dialog_full_screen);
        mXAdInstance = instance;
        mVideoView = videoView;
        mPosition = position; //切换大屏时继续播放视频；
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = (RelativeLayout)findViewById(R.id.content_layout);
        mBackButtom = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
        mBackButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackBtn();  //全屏显示关闭按钮的点击相应；
            }
        });

        mVideoView.setListener(this);
        mVideoView.mute(false);
        mParentView.addView(mVideoView);
    }

    //全屏关闭事件
    private void clickBackBtn() {
        dismiss();
        if (mListener != null){
            mListener.getCurrentPlayPostion(mVideoView.getCurrentPosition());
        }
    }

    //back键按下的事件监听
    @Override
    public void onBackPressed() {
        clickBackBtn();
        super.onBackPressed();
    }

    //dialog获取焦点
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus){
            //未获取焦点
            mPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }else{
            //表明dialog是首次获得焦点
            if (isFirst){
                //在华为手机无法直接恢复播放，需作此判断；
                mVideoView.seekAndResume(mPosition);
            }else {
                //获取焦点：回复视频的播放
                mVideoView.resume();
            }
        }
    }

    /**
     * dialog销毁时候调用；
     */
    @Override
    public void dismiss() {
        mParentView.removeView(mVideoView);//移除view，不在父控件中；
        super.dismiss();
    }



    @Override
    public void OnBufferUpdate(int time) {
        if (mXAdInstance != null){
            ReportManager.suReport(mXAdInstance.middleMonitor,true,time/ SDKConstant.MILLION_UNIT);
        }
    }



    @Override
    public void OnClickVideo() {
        String desationUrl = mXAdInstance.clickUrl;
        if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
            mSlotListener.onClickVideo(desationUrl);
            try {
                ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
                        / SDKConstant.MILLION_UNIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //走默认样式
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                Intent intent = new Intent(mContext, AdBrowserActivity.class);
                intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
                mContext.startActivity(intent);
                try {
                    ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void OnClickBackBtn() {

    }

    @Override
    public void OnClickPlay() {

    }

    @Override
    public void OnClickFullScreenBtn() {

        OnClickVideo();


    }

    @Override
    public void OnAdVideoLoadSuccess() {
        if (mVideoView != null){
            mVideoView.resume();
        }

    }

    @Override
    public void OnAdVideoLoadFailed() {

    }

    @Override
    public void OnAdVideoLoadComplete() {
        try {
            int position = mVideoView.getDuration()/SDKConstant.MILLION_UNIT;
            ReportManager.suReport(mXAdInstance.endMonitor,true,position);
        }catch (Exception e){
            e.printStackTrace();
        }
        dismiss();
        if (mListener != null){
            mListener.playComplete(); //通知业务层，播放结束；
        }

    }

    /**
     * 与业务层VideoSlot进行通信
     */
    public interface FullToSmallListener{
        void getCurrentPlayPostion(int position); //全屏播放中点击关闭按钮或者back键回调；
        void playComplete(); //全屏播放结束时回调；
    }

    public void setListener(FullToSmallListener listener) {
        this.mListener = listener;
    }

}
