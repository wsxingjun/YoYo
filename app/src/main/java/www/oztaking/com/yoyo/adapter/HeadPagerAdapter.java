package www.oztaking.com.yoyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import www.oztaking.com.yoyo.activity.base.CourseDetailActivity;
import www.oztaking.com.yoyo.utils.ImageLoaderManager;

/**
 * @function
 */

public class HeadPagerAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<String> mDataList = null;
    private boolean mIsMatch;
    private ImageLoaderManager mImageLoader;

    public HeadPagerAdapter(Context context, ArrayList<String> list, boolean b) {
        this.mContext = context;
        this.mDataList = list;
        this.mIsMatch = b;
        mImageLoader = ImageLoaderManager.getInstance(context);
    }

    @Override
    public int getCount() {
        mDataList.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView photoView;
        if (mIsMatch){
            photoView = new ImageView(mContext);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,CourseDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }else {
            photoView = new ImageView(mContext);
        }
        mImageLoader.displayImage(photoView,mDataList.get(position));
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }
}
