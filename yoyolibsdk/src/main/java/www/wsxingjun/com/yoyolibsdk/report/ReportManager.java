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
import www.wsxingjun.com.yoyolibsdk.network.response.HttpConstant.Params;
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

            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    if (isFull) {
                        try {
                            params.put("fu", "l");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        params.put("ve", String.valueOf(playTime));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }

        }
    }

    /**
     * send the video pause monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void pauseVideoReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    try {
                        params.put("ve", String.valueOf(playTime));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * 发送广告是否正常解析及展示监测
     */
    public static void sendAdMonitor(boolean isPad, String sid, String ie, String appVersion, Params step, String result) {
        RequestParams params = new RequestParams();

        try {
        params.put(Params.lvs.getKey(), Params.lvs.getValue());
        params.put(Params.st.getKey(), Params.st.getValue());
        params.put(Params.os.getKey(), Params.os.getValue());
        params.put(Params.p.getKey(), Params.p.getValue());
        params.put(Params.appid.getKey(), Params.appid.getValue());
        if (isPad) {
            params.put(Params.bt_pad.getKey(), Params.bt_pad.getValue());
        } else {
            params.put(Params.bt_phone.getKey(), Params.bt_phone.getValue());
        }

            params.put(step.getKey(),
                    step.getValue());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        params.put(HttpConstant.STEP_CD, result);
//        params.put(HttpConstant.SID, sid);
//        params.put(HttpConstant.IE, ie);
//        params.put(HttpConstant.AVS, appVersion);

        CommonOkHttpClient.get(CommonRequest.createGetRequest(HttpConstant.ATM_MONITOR, params), handle);
    }





}
