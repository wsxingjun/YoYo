package www.wsxingjun.com.yoyolibsdk.module.monitor;

import java.util.ArrayList;

import www.wsxingjun.com.yoyolibsdk.module.monitor.emevent.EMEvent;

/**
 * @function:
 */

public class AdValue {
    public String resourceID;
    public String adid;
    public String resource;  //视频播放的网址；
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
    public String type;
}
