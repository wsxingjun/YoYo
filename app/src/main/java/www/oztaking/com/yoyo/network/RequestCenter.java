package www.oztaking.com.yoyo.network;

import www.oztaking.com.yoyo.module.recommand.BaseRecommandModule;
import www.wsxingjun.com.yoyolibsdk.network.CommonOkHttpClient;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataHandle;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataListener;
import www.wsxingjun.com.yoyolibsdk.network.request.CommonRequest;
import www.wsxingjun.com.yoyolibsdk.network.request.RequestParams;
import www.wsxingjun.com.yoyolibsdk.network.response.HttpConstant;

/**
 * @function: 存放应用中所有的网络请求；
 */

public class RequestCenter {
    //post请求的封装--但是本看来这是get请求；
    private static void postRequest(String url, RequestParams params, DisposeDataListener listener,
                                   Class<?> clazz) {
        //应用层封装的get请求；
        CommonOkHttpClient.get(
                CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

        /**
         * 发送我们的首页的请求
         */

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstant.HOME_RECOMMAND,null,listener,
                BaseRecommandModule.class);
    }

}

