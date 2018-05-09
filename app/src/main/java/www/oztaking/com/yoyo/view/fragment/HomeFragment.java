package www.oztaking.com.yoyo.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.adapter.CoureseAdapter;
import www.oztaking.com.yoyo.network.RequestCenter;
import www.oztaking.com.yoyo.module.recommand.BaseRecommandModule;
import www.oztaking.com.yoyo.view.home.HomeHeaderLayout;
import www.oztaking.com.yoyo.zxing.app.CaptureActivity;
import www.wsxingjun.com.yoyolibsdk.network.listener.DisposeDataListener;

/**
 *
 */
public class HomeFragment extends BaseFragment implements
          View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = "HomeFragment";

    private static final int REQUEST_QRCODE = 0x01;

    private Context mContext;
    //UI
    private View mContentView;
    private TextView mQuickMark;
    private TextView mCategory;
    private TextView mSearchView;
    private ImageView mImgViewLoading;
    private ListView mListview;
    private DisposeDataListener listener;

    private BaseRecommandModule mRecommandData;
    private CoureseAdapter mAdapter;
//    private CourseAdapteraa mAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestReCommandData();
    }

    /**
     * 发送首页列表请求
     */
    private void requestReCommandData() {
//        listener = new DisposeDataListener() {
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObject) {
                //完成真正的功能逻辑
                Log.e(TAG, responseObject.toString()+"++++++++++++++++++");
                mRecommandData = (BaseRecommandModule) responseObject;
                Toast.makeText(getActivity(),"+++++++++++++++++",Toast.LENGTH_SHORT).show();
                Log.d(TAG,mRecommandData.toString()+"++++++++++++++++++");

                showSuccessView();
            }
            @Override
            public void onFailer(Object reasonObj) {
                //提示用户网络有问题
                Log.e(TAG, reasonObj.toString());
            }
        });
//        RequestCenter.requestRecommandData(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mContext = getContext();
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout,container,false);
        initView();
        return mContentView;
    }



    private void initView() {
        mQuickMark = (TextView) mContentView.findViewById(R.id.tv_quickMark);
        mCategory = (TextView) mContentView.findViewById(R.id.tv_category);
        mSearchView = (TextView) mContentView.findViewById(R.id.tv_search_view);
        mImgViewLoading = (ImageView) mContentView.findViewById(R.id.imgV_loading);
        mListview = (ListView) mContentView.findViewById(R.id.list_view);


        mQuickMark.setOnClickListener(this);
        mCategory.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
        mListview.setOnItemClickListener(this);

        //启动动画
        AnimationDrawable anim = (AnimationDrawable) mImgViewLoading.getDrawable();
        anim.start();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_quickMark:
                doOpenCamera();
                Toast.makeText(mContext,"扫描二维码按钮被点击了",Toast.LENGTH_SHORT).show();
            break;
        }

    }

    private void doOpenCamera() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent,REQUEST_QRCODE);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    /**
     *数据请求成功之后执行的方法
     */
    private void showSuccessView() {
        if(mRecommandData.data.list != null && mRecommandData.data.list.size() > 0){
            mImgViewLoading.setVisibility(View.GONE);
            mListview.setVisibility(View.VISIBLE);

            //为listView添加头
            mListview.addHeaderView(
                    new HomeHeaderLayout(mContext,mRecommandData.data.head));
            //创建适配器adapter
            mAdapter = new CoureseAdapter(mContext,mRecommandData.data.list);
            mListview.setAdapter(mAdapter);

            mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });


        }else {
            showErrorView();
        }
    }

    private void showErrorView() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK){
                    String code = data.getStringExtra("SCAN_RESULT");
//                    if (code.contains("http") || code.contains("https")){
//                        Intent intent = new Intent(mContext, AdBrowserActivity.class);
//                        intent.putExtra(AdBr)
//                    }

                    Toast.makeText(mContext,"二维码扫描结果："+code,Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }
}
