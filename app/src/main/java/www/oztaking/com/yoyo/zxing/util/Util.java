package www.oztaking.com.yoyo.zxing.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;


import static www.wsxingjun.com.yoyolibsdk.constant.SDKConstant.AutoPlaySetting;


public class Util {

	public static Activity currentActivity = null;

	/**
	 * 获得屏幕宽度
	 * 
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowWidthPix() {
		int ver = Build.VERSION.SDK_INT;
		Display display = currentActivity.getWindowManager()
				.getDefaultDisplay();
		int width = 0;
		if (ver < 13) {
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			width = dm.widthPixels;
		} else {
			Point point = new Point();
			display.getSize(point);
			width = point.x;
		}
		return width;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowHeightPix() {
		int ver = Build.VERSION.SDK_INT;
		Display display = currentActivity.getWindowManager()
				.getDefaultDisplay();
		int height = 0;
		if (ver < 13) {
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			height = dm.heightPixels;
		} else {
			Point point = new Point();
			display.getSize(point);
			height = point.y;
		}
		return height;
	}

	public static String getIMEI(Context context)
	{
		try
		{
			TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imeiCode= tm.getDeviceId();
			return imeiCode;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}


	public static int getVisiblePercent(View pView) {
		if (pView != null && pView.isShown()) {
			DisplayMetrics displayMetrics = pView.getContext().getResources().getDisplayMetrics();
			int displayWidth = displayMetrics.widthPixels;
			Rect rect = new Rect();
			pView.getGlobalVisibleRect(rect);
			if ((rect.top > 0) && (rect.left < displayWidth)) {
				double areaVisible = rect.width() * rect.height();
				double areaTotal = pView.getWidth() * pView.getHeight();
				return (int) ((areaVisible / areaTotal) * 100);  //计算面积的百分比
			} else {
				return -1;
			}
		}
		return -1;
	}


	//decide can autoplay the ad
	public static boolean canAutoPlay(Context context, AutoPlaySetting setting) {
		boolean result = true;
		switch (setting) {
			case AUTO_PLAY_3G_4G_WIFI:
				result = true;
				break;
			case AUTO_PLAY_ONLY_WIFI:
				if (isWifiConnected(context)) {
					result = true;
				} else {
					result = false;
				}
				break;
			case AUTO_PLAY_NEVER:
				result = false;
				break;
		}
		return result;
	}

	//is wifi connected
	public static boolean isWifiConnected(Context context) {
		if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
				!= PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
}
