package www.wsxingjun.com.yoyolibsdk.widget.adbrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import www.wsxingjun.com.yoyolibsdk.R;
import www.wsxingjun.com.yoyolibsdk.constant.SDKConstant;

/**
 * @function 负责视频的播放、暂停以及各类事件的触发
 */

public class CustomVideoView extends RelativeLayout implements
        View.OnClickListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {

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

    private AudioManager mAudioManager; //音量控制器
    private Surface mVideoSurface; //真正显示帧数据的类


    //Data
    private String mUrl; //加载视频的地址；
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
    private Button mFullBtn;

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
//        mFrameView = mPlayerView.findViewById(R.id.big_framing_view);
        mFrameView = (ImageView) mPlayerView.findViewById(R.id.framing_view);


        mLoadingBar = (ImageView) mPlayerView.findViewById(R.id.loading_bar);


        mFullBtn = (Button) mPlayerView.findViewById(R.id.xadsdk_big_play_btn);


    }

    private void registerBroadcastReceiver() {

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

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

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    public interface ADVideoPlayerListener {

    }


    public interface ADFrameImageLoadListener {

    }

    public interface ImageLoaderListener {

    }

    public class ScreenEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
