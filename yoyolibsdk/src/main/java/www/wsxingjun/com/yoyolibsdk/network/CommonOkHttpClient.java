package www.wsxingjun.com.yoyolibsdk.network;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import www.wsxingjun.com.yoyolibsdk.network.https.HttpsUtils;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataHandle;
import www.wsxingjun.com.yoyolibsdk.network.response.CommonJsonCallback;


/**
 * @function 请求的发送、请求参数的配置、https支持；
 */

public class CommonOkHttpClient {
    private static final int TIME_OUT = 30; //超时请求的时间；
    private static OkHttpClient mOkHttpClient;

    //配置封装的CommonOkHttpClient的参数
    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true); //允许重定向，增加请求转发的灵活性；

        //增加对HTTPS的支持；
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        /**
         * trust all the https point
         */
        okHttpBuilder.sslSocketFactory(
                HttpsUtils.initSSLSocketFactory(),
                HttpsUtils.initTrustManager());
        //生成client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    //发送具体的http/https请求；
    public static Call sendRequest(Request request, Callback commonCallback){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);

        return call;
    }

    public static Call get(Request request, DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(Request request,DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }





//    private void test(){
//        CommonOkHttpClient.sendRequest(CommonRequest.createGetRequest("http://wwww.baidu.com",
//                null), new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        })
//    }

}
