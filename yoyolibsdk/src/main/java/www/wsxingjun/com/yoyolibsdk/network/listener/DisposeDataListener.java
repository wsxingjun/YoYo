package www.wsxingjun.com.yoyolibsdk.network.listener;

/**
 * @function 自定义事件的监听
 */

public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     * @param responseObject
     */
    public void onSuccess(Object responseObject);

    /**
     * 请求失败回调事件处理
     * @param reasonObj
     */
    public void onFailer(Object reasonObj);

}
