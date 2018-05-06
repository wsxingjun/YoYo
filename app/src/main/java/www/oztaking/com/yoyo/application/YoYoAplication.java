package www.oztaking.com.yoyo.application;

import android.app.Application;


/**
 * @function:
 * ①整个程序的入口，
 * ②为整个app提供上下文环境；
 * ③ 初始化的工作；
 */


public class YoYoAplication extends Application {

    private static YoYoAplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static YoYoAplication getInstance(){
        return mApplication;
    }
}
