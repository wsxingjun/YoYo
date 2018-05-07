package www.wsxingjun.com.yoyolibsdk.network.listener;

/**
 * @function
 * 对返回的json数据进行了解析，返回到class；
 * 对字节码和响应回调listener进行了封装；
 */

public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;

    public DisposeDataHandle(DisposeDataListener mListener) {
        this.mListener = mListener;
    }

    public DisposeDataHandle(DisposeDataListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }


}
