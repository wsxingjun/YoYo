package www.oztaking.com.yoyo.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.module.recommand.RecommandBodyValue;
import www.oztaking.com.yoyo.utils.ImageLoaderManager;
import www.oztaking.com.yoyo.utils.Utils;

/**
 * @function:
 */

public class CoureseAdapter extends BaseAdapter {
    /**
     * listView 中不同item的标志
     */

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

    public CoureseAdapter(Context mContext, ArrayList<RecommandBodyValue> mData) {
        this.mData = mData;
        this.mContext = mContext;
        mInflate = LayoutInflater.from(mContext);
        mImagerLoader = ImageLoaderManager.getInstance(mContext);
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
                case CARD_TYPE_ONE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_one_layout1, parent, false);
                    //初始化Viewholder中使用到的控件；
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);

                    mViewHolder.mProductLayout.removeAllViews();
                    //动态添加多个imageview
                    for (String url : value.url) {
                        mViewHolder.mProductLayout.addView(createImageView(url));
                    }
                    break;
                default:
                    break;
            }

            Log.d("wsxingjun", "+++++++++++++++++++++++++++=");
            convertView.setTag(mViewHolder);
            Log.d("wsxingjun", "*****************************");

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //开始绑定数据
        switch (type) {
            case CARD_TYPE_ONE:

                Log.d("wsxingjun", "*****************************");
                Log.d("wsxingjun", "CARD_TYPE_ONE绑定数据");

                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.title);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));

                mImagerLoader.displayImage(mViewHolder.mLogoView, value.logo);

                mViewHolder.mProductLayout.removeAllViews();
                //动态添加多个imageview
                for (String url : value.url) {
                    mViewHolder.mProductLayout.addView(createImageView(url));
                }
                break;
            default:
                break;
        }

        return convertView;
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

        //CardOne特有的属性
        private LinearLayout mProductLayout;

    }
}