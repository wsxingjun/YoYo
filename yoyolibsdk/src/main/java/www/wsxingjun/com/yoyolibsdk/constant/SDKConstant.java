package www.wsxingjun.com.yoyolibsdk.constant;

/**
 * @function
 */

public class SDKConstant {

    //自动播放的阈值
    public static int VIDEO_SCREEN_PERCENT = 50;

    public static int MILLION_UNIT = 1000; //毫秒的单位


    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;


    //自动播放条件
    public enum AutoPlaySetting {
        AUTO_PLAY_ONLY_WIFI,
        AUTO_PLAY_3G_4G_WIFI,
        AUTO_PLAY_NEVER
    }

    //用来记录可自动播放的条件
    private static AutoPlaySetting currentSetting =
            AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI; //默认都可以自动播放

    public static void setCurrentSetting(AutoPlaySetting setting) {
        currentSetting = setting;
    }

    public static AutoPlaySetting getCurrentSetting() {

        return currentSetting;
    }








}
