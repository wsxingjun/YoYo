package www.wsxingjun.com.yoyolibsdk.widget.adbrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import com.orhanobut.logger.Logger;

import www.wsxingjun.com.yoyolibsdk.R;
import www.wsxingjun.com.yoyolibsdk.adutils.Utils;
import www.wsxingjun.com.yoyolibsdk.constant.SDKConstant;
import www.wsxingjun.com.yoyolibsdk.network.https.HttpsUtils;

/**
 * @function 负责视频的播放、暂停以及各类事件的触发
 */

public class CustomVideoView extends RelativeLayout implements
        View.OnClickListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {


    private static String tag = "信息：CustomVideoView";


    //变量
    private static final String TAG = "测：AdVideoView";
    private static final int TIME_MSG = 0X01;//事件的类型；
    private static final int TIME_INVAL = 1000;//发送msg的时间间隔，每隔1s就会对发送一个TIME_MSG,然后随机
    //    处理
    private static final int STATE_ERROR = -1;//
    private static final int STATE_IDLE = 0;//事件的类型；
    private static final int STATE_PLAYING = 1;//当前正在播放的标志；
    private static final int STATE_PAUSING = 2;//暂停标志
    private static final int LOAD_TOTAL_COUNT = 3;//视频失败后重新尝试加载的次数；

    //UI
    private ViewGroup mParentContainer;

    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private Button mMiniPlayBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;
    private Button mFullBtn;

    private AudioManager mAudioManager; //音量控制器
    private Surface mVideoSurface; //真正显示帧数据的类
    private ADFrameImageLoadListener mFrameLoadListener;
    private ADVideoPlayerListener mListener;


    //Data
    private String mFrameURI; //加载视频的地址；
    private boolean mIsMute; //是否静音
    private int mScreenWidth, mDestationHeight;  //宽度是屏幕的宽度，高度是；16:9计算；


    //状态的保护
    private boolean mIsRealPause;
    private boolean mIsComplete;
    private int mCurrentCount;
    private int mPlayerState = STATE_IDLE; //默认处于空闲的状态；

    //视频播放的核心类
    private MediaPlayer mMediaPlayer;
    private ADVideoPlayerListener listener; //事件监听的回调；
    private ScreenEventReceiver mScreenReceiver; //监听屏幕时候锁屏广播；

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //视频播放中向服务器发送当前播放的进度；
            //每隔1s发送一个msg，通知被调用者当前视频的播放进度；不要使用timer，容易造成内存泄露；
            switch (msg.what) {
                case TIME_MSG:
                    //                    if (mIsPlaying()){
                    //                        listener.onBufferUpadte
                    //                    }
                    break;
            }
        }
    };


    public CustomVideoView(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
        registerBroadcastReceiver();
    }

    //数据的初始化
    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PERCENT);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) inflater.inflate(R.layout.xadsdk_video_player, this);
        mVideoView = (TextureView) mPlayerView.findViewById(R.id.xadsdk_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);

        LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayerView.setLayoutParams(params);

        mMiniPlayBtn = (Button) mPlayerView.findViewById(R.id.xadsdk_small_play_btn);
        mFullBtn = (Button) mPlayerView.findViewById(R.id.xadsdk_to_full_view);
        mLoadingBar = (ImageView) mPlayerView.findViewById(R.id.loading_bar);
        mFrameView = (ImageView) mPlayerView.findViewById(R.id.framing_view);

        mMiniPlayBtn.setOnClickListener(this);
        mFullBtn.setOnClickListener(this);

    }


    //当屏幕上的view发生变化的时候，此方法会被回调；
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, @IntDef(value = {View.VISIBLE,
            View.INVISIBLE, View.GONE}) int visibility) {
        Log.d(TAG, "onVisibilityChanged: 屏幕发生了变化...");
        super.onVisibilityChanged(changedView, visibility);
    }

    //视频播放器遇到触摸事件就会消费掉，不会影响父控件；
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }


    private void registerBroadcastReceiver() {

    }


    //播放器的开始、暂停按键的响应；
    @Override
    public void onClick(View v) {

    }

    //播放器就绪的状态；
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        if (mMediaPlayer != null){
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if ( mListener != null){
                mListener.OnAdVideoLoadSuccess();
            }
            decideCanPlay();
        }

    }

    //播放器产生异常时回调此方法；
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    //视频播放完成后会回调此方法；
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    //缓存状态的通知；
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    //SurfaceTexture处于就绪的状态；
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    //SurfaceTexture状态的更新；
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    public interface ADVideoPlayerListener {
        public void OnAdVideoLoadSuccess();
        public void OnBufferUpdate();
        public void OnClickFullScreenBtn();
        public void OnClickVideo();
        public void OnClickBackBtn();
        public void OnClickPlay();
        public void OnAdVideoLoadFailed();
        public void OnAdVideoLoadComplete();
    }


    public interface ADFrameImageLoadListener {
        public void OnStartFrameLoad(String url, ImageLoaderListener listener);
    }


    public interface ImageLoaderListener {
        //如果图片不成功，则传递null
        void onLoadingComplete(Bitmap loadedImage);

    }

    public class ScreenEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    //加载视频的url；
    public void load() {
        if (this.mPlayerState != STATE_IDLE) {
            return;
        }

        showLoadingView();
        setCurrentPlayeState(STATE_IDLE);
        checkMediaPlayer();//完成播放器的创建工作；
        try {
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync();//异步加载视频；
        } catch (IOException e) {
            stop();
            e.printStackTrace();
        }


    }

    //加载视频存在一个小动画
    private void showLoadingView() {
        mFullBtn.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);
        AnimationDrawable anmi = (AnimationDrawable) mLoadingBar.getBackground();
        anmi.start();
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
        loadFramImage();
    }

    //暂停视频
    public void pause() {

    }

    //    恢复视频：就绪/暂停-->变为播放的状态；
    public void resume() {

    }

    //回到初始的状态，视频播放的进度条为0；重复播放视频，节省流量；
    public void playBack() {

    }

    /**
     * 停止状态；清空当前的mediaplayer并重新load；
     */
    public void stop() {
        Logger.d("停止方法被调用了");
        if (this.mMediaPlayer != null){
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setOnSeekCompleteListener(null);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        setCurrentPlayeState(STATE_IDLE);

        //重新加载load
        if (mCurrentCount  < LOAD_TOTAL_COUNT){
            mCurrentCount+=1;
            load();
        }else {
            //停止重试；
            showPauseView(false);
        }
    }

    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show?View.VISIBLE:View.GONE);
        mMiniPlayBtn.setVisibility(show?View.GONE:View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show){
            mFrameView.setVisibility(View.VISIBLE);
        }else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    //销毁自定义的view;
    public void Destory() {

    }

    //在home页面跳转到全屏播放的时候调用该方法；
    //跳到指定点播放视频
    public void SeekAndResume(int position) {

    }

    //跳到指定点暂停视频
    public void SeekAndPause() {

    }


    //对应事件发生的时候，调用对应的监听，通知外界当前的视频的状态
    public void setListener(ADVideoPlayerListener listener) {
        this.listener = listener;
    }

    //    每次都创建一个新的播放器；
    public synchronized void checkMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = createMediaPlayer();
        }
    }

    private MediaPlayer createMediaPlayer() {
        return mMediaPlayer;
    }

    //注册广播
    private void registerBroadCastReceiver() {
        if (mMediaPlayer != null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    private void unRegisterBroadCastReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }

    private void decideCanPlay() {
        if (Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
            resume();
        } else {
            pause();
        }
    }

    public void isShowFullBtn(boolean isShow) {

    }

    public void isPauseBtnClicked() {

    }

    public boolean isComplete() {
        return mIsComplete;
    }

    private void ShowPauseView(boolean show) {
        mFullBtn.setVisibility(shw ? View.VISIBLE : View.GONE);
        mMiniPlayBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show) {
            mFrameView.setVisibility(View.GONE);
            loadFramImage();
        } else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    /**
     * 异步加载定帧图像
     */
    private void loadFramImage() {
        if (mFrameLoadListener != null) {
            mFrameLoadListener.OnStartFrameLoad(mFrameURI, new ImageLoaderListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    if (loadedImage != null){
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mFrameView.setImageBitmap(loadedImage);
                    }else {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        mFrameView.setImageResource(R.drawable.xadsdk_img_error);
                    }
                }
            });
        }
    }

    private void entryResumeState() {
        setCurrentPlayState(STATE_PLAYING);
    }

    private void setCurrentPlayState(int statePlaying) {

    }

    private void setCurrentPlayeState(int state) {
        mPlayerState = state;
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (this.mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }

        return 0;
    }


}
