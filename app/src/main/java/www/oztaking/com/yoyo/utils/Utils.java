package www.oztaking.com.yoyo.utils;

import android.content.Context;

import java.util.ArrayList;

import www.oztaking.com.yoyo.module.recommand.RecommandBodyValue;

/**
 * @function
 */

public class Utils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public static ArrayList<RecommandBodyValue> handleData(RecommandBodyValue value){

        ArrayList<RecommandBodyValue> values = new ArrayList<>();
        String[] titles = value.title.split("@");
        String[] infos = value.info.split("@");
        String[] prices = value.price.split("@");
        String[] texts = value.text.split("@");
        ArrayList<String> urls = value.url;

        int start = 0;
        int length = titles.length;
        for (int i = 0; i < length; i++){
            RecommandBodyValue tempValye = new RecommandBodyValue();
            tempValye.title = titles[i];
            tempValye.info = infos[i];
            tempValye.price = prices[i];
            tempValye.text = texts[i];
            tempValye.url = extractData(urls,start,3);
            start += 3;

            values.add(tempValye);
        }
        return values;
    }

    private static ArrayList<String> extractData(ArrayList<String> source,
                                                 int start, int interVal) {
        ArrayList<String> tempUrls = new ArrayList<>();
        int sum = start + interVal;
        for (int i = start; i < sum; i++){
            tempUrls.add(source.get(i));
        }
        return tempUrls;
    }
}


















