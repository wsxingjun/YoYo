package www.oztaking.com.yoyo.view.home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.module.recommand.RecommandHeadValue;
import www.oztaking.com.yoyo.utils.ImageLoaderManager;
import www.oztaking.com.yoyo.view.viewpagerindicator.CirclePageIndicator;

/**
 * @function
 */

public class HomeHeaderLayout extends RelativeLayout {

    private Context mContext;

    //data

    private RecommandHeadValue mHeaderValue;
    private ImageLoaderManager mImageLoader;
    private RelativeLayout mRootView;
    private CirclePageIndicator mIndicator;
    private ViewPager mViewPager;
    private HeadPagerAdapter mHeadPagerAdapter;

    public HomeHeaderLayout(Context context, RecommandHeadValue headerValue) {
        this(context,null,headerValue);
    }

    public HomeHeaderLayout(Context context, AttributeSet attrs, RecommandHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_home_head_layout, this);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) mRootView.findViewById(R.id.pager_indictor_view);

        mHeadPagerAdapter = new HeadPagerAdapter(mContext, mHeaderValue.ads, true);
        mViewPager.setAdapter(mHeadPagerAdapter);
        //绑定indicator与ViewPager
        mIndicator.setViewPager(mViewPager);



    }
}
