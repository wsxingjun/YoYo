package www.wsxingjun.com.yoyolibsdk.report;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import www.wsxingjun.com.yoyolibsdk.adutils.Utils;
import www.wsxingjun.com.yoyolibsdk.module.monitor.Monitor;
import www.wsxingjun.com.yoyolibsdk.network.CommonOkHttpClient;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataHandle;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataListener;
import www.wsxingjun.com.yoyolibsdk.network.request.CommonRequest;
import www.wsxingjun.com.yoyolibsdk.network.request.RequestParams;
import www.wsxingjun.com.yoyolibsdk.network.response.HttpConstant;

/**
 * @function:
 */

public class ReportManager {

    /**
     * 默认的事件回调处理
     */
    private static DisposeDataHandle handle = new DisposeDataHandle(
            new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                }

                @Override
                public void onFailer(Object reasonObj) {
                }
            });


    public static void suReport(ArrayList<Monitor> monitors, boolean isFull, long playTime) {
        if (monitors != null && monitors.size() > 0) {

            for (Monitor monitor:monitors){
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)){
                    if (isFull){
                        try {
                            params.put("fu","l");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        params.put("ve",String.valueOf(playTime));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url,params),handle);
            }


        }

    }
}
