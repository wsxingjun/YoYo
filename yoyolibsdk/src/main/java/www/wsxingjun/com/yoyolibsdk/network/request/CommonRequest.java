package www.wsxingjun.com.yoyolibsdk.network.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * @function:
 */

public class CommonRequest {

    //post请求封装

    public static Request createPostRequest(String url, RequestParams params) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到okhtttp的FormBody中
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //通过请求类的build方法获取到真正的请求体参数
        FormBody mFormBody = mFormBodyBuild.build();

        return new Request
                .Builder()
                .url(url)
                .post(mFormBody)
                .build();
    }

    //get请求封装

    /**
     * @param url
     * @param params
     * @return
     * @function:通过传入的参数返回一个Get类型的请求；
     */
    public static Request createGetRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        return new Request
                .Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .build();
    }


    public static Request createMonitorRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if (params != null && params.hasParams()) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get()
                .build();
    }
}
