package www.oztaking.com.yoyo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.module.recommand.RecommandBodyValue;
import www.oztaking.com.yoyo.utils.ImageLoaderManager;

/**
 * @function
 */

public class HotSalePagerAdapter  extends PagerAdapter{

    private Context mContext;
    private ArrayList<RecommandBodyValue> mData;
    private LayoutInflater mInflate;
    private ImageLoaderManager mImageLoader;
    private ImageView mImgeViewOne;
    private ImageView mImgeViewTwo;
    private ImageView mImgeViewThree;
    private RelativeLayout mRootView;
    private TextView mTitleView;
    private TextView mInfoView;
    private TextView mGonggaoView;
    private LinearLayout mImageLayout;
    private TextView mSaleNumView;

    public HotSalePagerAdapter(Context mContext, ArrayList<RecommandBodyValue> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }


    @Override
    public int getCount() {
        //设置为无限循环；
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 设置图片无限循环 使用取余数的方法
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final RecommandBodyValue value = mData.get(position % mData.size());
        //初始化布局
        mRootView = (RelativeLayout) mInflate.inflate(R.layout.item_hot_product_pager_layout, null);
        mTitleView = (TextView) mRootView.findViewById(R.id.title_view);
        mInfoView = (TextView) mRootView.findViewById(R.id.info_view);
        mGonggaoView = (TextView) mRootView.findViewById(R.id.gonggao_view);
        mSaleNumView = (TextView) mRootView.findViewById(R.id.sale_num_view);


        mTitleView.setText(value.title);
        mInfoView.setText(value.info);
        mGonggaoView.setText(value.price);
        mSaleNumView.setText(value.text);

        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = (ImageView) mRootView.findViewById(R.id.image_one);
        imageViews[1] = (ImageView) mRootView.findViewById(R.id.image_one);
        imageViews[2] = (ImageView) mRootView.findViewById(R.id.image_one);

        int length = imageViews.length;
        for (int i = 0; i < length; i++){
            mImageLoader.displayImage(imageViews[i],value.url.get(i));
        }

//        rootview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CourseDetailActivity.class);
//                intent.putExtra(CourseDetailActivity.COURSE_ID,value.adid);
//                mContext.startActivity(intent);
//            }
//        });

        container.addView(mRootView,0);
        return mRootView;
    }
}
