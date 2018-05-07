package www.wsxingjun.com.yoyolibsdk.network.response;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import www.wsxingjun.com.yoyolibsdk.adutils.ResponseEntityToModule;
import www.wsxingjun.com.yoyolibsdk.network.exception.OkHttpException;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataHandle;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataListener;

/**
 * @function 专门处理对json数据的回调的响应；
 */

public class CommonJsonCallback implements Callback {
    //与服务器返回的字段的一个对应关系

    // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final String RESULT_CODE = "ecode";// 只说明是服务器有返回；
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    /**
     * 自定义的异常类型
     */
    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR = -2;
    protected final int OTHER_ERROR = -3;

    /**
     * 将其他的线程的数据转发到UI线程；
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        //初始化handler
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException ioexception) {
        /**
         * 还在非UI线程，需要转发；
         * 将失败的信息返回到应用层，应用程进行处理；
         */

        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailer(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    //处理服务器返回的响应的数据
    private void handleResponse(Object resonseObj) {
        //保证代码的健壮性
        if (resonseObj == null && resonseObj.toString().trim().equals("")) {
            mListener.onFailer(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }


        try {
            JSONObject result = new JSONObject(resonseObj.toString());
            if (result.has(RESULT_CODE)) {
                //从json对象中取出响应码，如果是0，则是正确的响应；
                if (mClass == null) {
                    mListener.onSuccess(resonseObj);//返回给应用层自己的处理；
                } else {
                    //json转化为实体对象
                    Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                    //正确的数据返回实体对象；
                    if (obj != null) {
                        mListener.onSuccess(obj);
                    } else {
                        //返回的是不合法的jons；
                        mListener.onFailer(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                    }
                }
            } else {
                //将服务器的异常回调到应用层去处理；
                mListener.onFailer(new OkHttpException(OTHER_ERROR,
                        result.get(RESULT_CODE)));
            }
        } catch (JSONException e) {
            mListener.onFailer(new OkHttpException(OTHER_ERROR, e.getMessage()));
        }
    }
}



















