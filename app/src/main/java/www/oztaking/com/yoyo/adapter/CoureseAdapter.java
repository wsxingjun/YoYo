package www.oztaking.com.yoyo.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
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

/**
 * @function:
 */

public class CoureseAdapter extends BaseAdapter {
    /**
     * listView 中不同item的标志
     */

    private static final int CARD_COUNT = 4;
    private static final int VIDEO_TYPE = 0x00;
    private static final int CARD_SINGLE_PIC = 0x01;
    private static final int CARD_MULTI_PIC = 0x02;
    private static final int CARD_VIEW_PAGER = 0x03;

    //加载的数据
    private ArrayList<RecommandBodyValue> mData;
    private ViewHolder viewHolder;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflater;

    private ImageLoaderManager mImagerLoader;

    private Context mContext;

    public CoureseAdapter(Context mContext,ArrayList<RecommandBodyValue> mData) {
        this.mData = mData;
        this.mContext = mContext;
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
    public View getView(int pos, View convertview, ViewGroup viewGroup) {
        int type = getItemViewType(pos);
        final RecommandBodyValue value = (RecommandBodyValue) getItem(pos);
        if (convertview == null) {
            switch (type) {
                case CARD_SINGLE_PIC:

                    mViewHolder = new ViewHolder();
                    mInflater.inflate(R.layout.item_product_card_one_layout, viewGroup, false);

                    //初始化Viewholder中使用到的控件；
                    mViewHolder.mLogoView = (CircleImageView) convertview.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertview.findViewById(R.id.item_title_textview);
                    mViewHolder.mInfoView = (TextView) convertview.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertview.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertview.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertview.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertview.findViewById(R.id.item_zan_view);
                    break;
                default:
                    break;
            }

            convertview.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertview.getTag();
        }

        //开始绑定数据
        switch (type) {
            case CARD_SINGLE_PIC:
                mViewHolder.mTitleView.setText(value.title);
//                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.String.tian_qian)));
                mViewHolder.mFooterView.setText(value.title);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
//                mViewHolder.mZanView.setText(mContext.getString(R.String.dian_zan).concat(value.zan));

                mImagerLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mImagerLoader.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;
            default:
                break;
        }


        return convertview;
    }

    private static class ViewHolder {
        //缓存所有的card共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;

        //        videoCard特有的属性
        private RelativeLayout mVideoContentLayout;
        private ImageView mShareView;

        //        Video Card 外的所有的属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;

        //CardOne特有的属性
        private LinearLayout mProductLayout;
        //CardTwo特有的属性
        private ImageView mProductView;
        //CardThree特有的属性
        private ViewPager mViewPager;


    }
}
