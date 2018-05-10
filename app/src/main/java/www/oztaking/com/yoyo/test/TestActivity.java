package www.oztaking.com.yoyo.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import www.oztaking.com.yoyo.R;
import www.wsxingjun.com.yoyolibsdk.widget.adbrowser.CustomVideoView;

/**
 * @function:
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RelativeLayout mContentLayout = (RelativeLayout) findViewById(R.id.activity_test);
        CustomVideoView mCustomVideoView = new CustomVideoView(this, mContentLayout);
        mCustomVideoView.setDataSource("http://fairee.vicp.net:83/2016rm/0116/baishi160116.mp4");
        mContentLayout.addView(mCustomVideoView);

    }
}
