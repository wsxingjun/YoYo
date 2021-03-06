package www.oztaking.com.yoyo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import www.oztaking.com.yoyo.R;

/**
 * @function 初始化UniverseImageLoader，并用来加载网络图片；
 */

public class ImageLoaderManager {

    private static final int THREAD_COUNT = 4; //UIL最多加载的线程数；
    private static final int PROPRITY = 2; //图片加载的优先级；
    private static final int DISK_CACHE_SIZE = 50 * 1024; //UIL可以缓存的图片的大小；
    private static final int READ_TIME_OUT = 30 * 1000; //读取的超时时间；
    private static final int CONNECTION_TIME_OUT = 5 * 1000; //连接的超时时间；

    private static ImageLoader mImageLoader = null;
    private static ImageLoaderManager mInstance = null;

    public static ImageLoaderManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }

        return mInstance;
    }

    public ImageLoaderManager(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PROPRITY)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDefaultOptions())
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
    }
//
//    public void displayImage(ImageView imageView, String path,
//                             DisplayImageOptions options,
//                             ImageLoadingListener listener) {
//        if (mImageLoader != null) {
//            displayImage(imageView, path,options,listener);
//        }
//    }

//    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
//            mImageLoader.displayImage(path,imageView,listener);
//    }
//
//    public void displayImage(ImageView imageView, String path) {
//        displayImage(imageView, path, null);
//    }



    //load the image
    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
        if (mImageLoader != null) {
            mImageLoader.displayImage(path, imageView, listener);
        }
    }

    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }


    //默认图片加载的设置
    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_user_avatar)
                .showImageOnFail(R.drawable.default_user_avatar)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }
}














