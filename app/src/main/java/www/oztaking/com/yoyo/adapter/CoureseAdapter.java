package www.oztaking.com.yoyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.module.recommand.RecommandBodyValue;
import www.oztaking.com.yoyo.utils.ImageLoaderManager;
import www.oztaking.com.yoyo.utils.Utils;
import www.wsxingjun.com.yoyolibsdk.Activity.AdBrowserActivity;
import www.wsxingjun.com.yoyolibsdk.core.AdContextInterface;
import www.wsxingjun.com.yoyolibsdk.core.video.VideoAdContext;
import www.wsxingjun.com.yoyolibsdk.core.video.VideoAdSlot;

/**
 * @function:
 */

public class CoureseAdapter extends BaseAdapter {
    /**
     * listView 中不同item的标志
     *
     */
    private static final String TAG = "wsxingjun";
    private int mLongth;


    private VideoAdContext mAdsdkContext;

    private static final int CARD_COUNT = 4;
    private static final int VIDOE_TYPE = 0x00; //视频类型的item
    private static final int CARD_TYPE_ONE = 0x01; //
    private static final int CARD_TYPE_TWO = 0x02;
    private static final int CARD_TYPE_THREE = 0x03;


    private LayoutInflater mInflate;
    private Context mContext;
    //加载的数据
    private ArrayList<RecommandBodyValue> mData;
    private ViewHolder mViewHolder;

    private ImageLoaderManager mImagerLoader;
    private View mVideoContentLayout;

    public CoureseAdapter(Context context, ArrayList<RecommandBodyValue> data) {
        mData = data;
        mContext = context;
        mInflate = LayoutInflater.from(context);
        mImagerLoader = ImageLoaderManager.getInstance(context);
        mLongth = data.size();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int pos) {
        return mData.get(pos);
    }

    //listView中存在几种类型的item;
    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    /**
     * 通知Adapter 使用哪种类型的item去加载数据
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        return value.type;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        Log.d("wsxingjun", "getView+++++++++++");
        int type = getItemViewType(pos);
        final RecommandBodyValue value = (RecommandBodyValue) getItem(pos);

        if (convertView == null) {
            switch (type) {
                case CARD_TYPE_ONE: //多图item开发；
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_one_layout, parent, false);
                    //初始化Viewholder中使用到的控件；
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mPhotoLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);

                    mViewHolder.mPhotoLayout.removeAllViews();
                    //动态添加多个imageview
                    for (String url : value.url) {
                        mViewHolder.mPhotoLayout.addView(createImageView(url));
                    }
                    convertView.setTag(mViewHolder);

                    break;

                case CARD_TYPE_TWO:  //单图item开发
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_two_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);

                    convertView.setTag(mViewHolder);
                    break;

                case CARD_TYPE_THREE://pager多图显示
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_three_layout, parent, false);
                    mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.vp_view_pager);


                    //增加数据
                    ArrayList<RecommandBodyValue> recommandList = Utils.handleData(value);
                    mViewHolder.mViewPager.setPageMargin(Utils.dip2px(mContext, 12));
                    mViewHolder.mViewPager.setAdapter(new HotSalePagerAdapter(mContext, recommandList));
                    mViewHolder.mViewPager.setCurrentItem(recommandList.size() * 100);

                    convertView.setTag(mViewHolder);
                    break;
                case VIDOE_TYPE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_video_layout, parent, false);
                    mViewHolder.mVieoContentLayout = (RelativeLayout)convertView.findViewById(R.id.video_ad_layout);
                    //为对应布局创建播放器
                    mAdsdkContext = new VideoAdContext(mViewHolder.mVieoContentLayout,new Gson().toJson(value), null);
//                    mAdsdkContext.setAdResultListener(new AdContextInterface() {
//                        @Override
//                        public void onAdSuccess() {
//
//                        }
//
//                        @Override
//                        public void onAdFailed() {
//
//                        }
//
//                        @Override
//                        public void onClickVideo(String url) {
//                            Intent intent = new Intent(mContext, AdBrowserActivity.class);
//                            intent.putExtra(AdBrowserActivity.KEY_URL, url);
//                            mContext.startActivity(intent);
//                        }
//                    });

                    break;

                default:
                    break;
            }

            Log.d("wsxingjun", "+++++++++++++++++++++++++++=");
//            mView.setTag(mViewHolder);
            Toast.makeText(mContext, "convertView++", Toast.LENGTH_SHORT).show();
            Log.d("wsxingjun", "*****************************");

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //开始绑定数据
        switch (type) {
            case CARD_TYPE_ONE: //多图item开发；

                Log.d("wsxingjun", "CARD_TYPE_ONE绑定数据");

                mImagerLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.title);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));

                Log.d("wsxingjun", "多图加载图片LOGO开始");
                mViewHolder.mPhotoLayout.removeAllViews();
                //动态添加多个imageview
                for (String url : value.url) {
                    mViewHolder.mPhotoLayout.addView(createImageView(url));
                }
                break;

            case CARD_TYPE_TWO: //单图item开发；

                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));

                //为log加载图片
                mImagerLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mImagerLoader.displayImage(mViewHolder.mPhotoView, value.url.get(0));

            case CARD_TYPE_THREE:
                break;

            default:
                break;
        }

        return convertView;
    }

    //自动播放方法
    public void updateAdInScrollView(){
        if (mAdsdkContext != null){
            mAdsdkContext.updateAdInScrollView();
        }
    }

    private View createImageView(String url) {
        ImageView photoView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(mContext, 100), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = Utils.dip2px(mContext, 5);
        photoView.setLayoutParams(params);
        mImagerLoader.displayImage(photoView, url);
        return photoView;
    }

    private static class ViewHolder {
        //缓存所有的card共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;

        // Video Card 外的所有的属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;

        //CardOne特有的属性  //多图item；
        private LinearLayout mPhotoLayout;

        //CardTwo特有的属性 //单图item
        private ImageView mPhotoView;

        //CardThree 特有的属性 //viewPager
        private ViewPager mViewPager;
        //Video 特有的属性
        private RelativeLayout mVieoContentLayout;

    }

}
