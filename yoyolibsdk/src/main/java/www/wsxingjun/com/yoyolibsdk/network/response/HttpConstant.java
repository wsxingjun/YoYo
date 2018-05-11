package www.wsxingjun.com.yoyolibsdk.network.response;

/**
 * @function:
 */

public class HttpConstant {


    public static final String ROOT_URL = "http://imooc.com/api";

    public static final String ATM_PRE = "val.atm.youku.com";

    //播放器&移动后台监控埋点日志地址
    public static final String ATM_MONITOR = "http://count.atm.youku.com/mlog";


    //广告数据返回成功
    public static final String AD_DATA_SUCCESS = "200";
    //广告数据解析失败
    public static final String AD_DATA_FAILED = "202";
    //广告加载成功
    public static final String AD_PLAY_SUCCESS = "300";
    //广告加载失败
    public static final String AD_PLAY_FAILED = "301";

    /**
     * 首页产品请求接口
     */
    public static String HOME_RECOMMAND = ROOT_URL + "/product/home_recommand.php";

    public enum Params {

        lvs("lvs", "4"), st("st", "12"), bt_phone("bt", "1"), bt_pad("bt", "0"),
        os("os", "1"), p("p", "2"), appid("appid", "xya"),
        ad_analize("sp", "2"),
        ad_load("sp", "3");

        private String value;
        private String key;

        private Params(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }

}
