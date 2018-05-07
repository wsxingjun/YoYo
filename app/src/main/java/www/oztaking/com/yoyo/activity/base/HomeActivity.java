package www.oztaking.com.yoyo.activity.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.view.fragment.HomeFragment;
import www.oztaking.com.yoyo.view.fragment.MessageFragment;
import www.oztaking.com.yoyo.view.fragment.MineFragment;
import www.oztaking.com.yoyo.view.fragment.PondFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mHomeLayout;
    private RelativeLayout mPondLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private ImageView mHomeView;
    private ImageView mPondView;
    private ImageView mMessageView;
    private ImageView mMineView;

    private HomeFragment mHomeFragment;
    private PondFragment mPondFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;

    private Fragment mCurrent;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        changeStatusBarColor(R.color.color_fed952);
        setContentView(R.layout.activity_home_layout);

        initView();

        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout,mHomeFragment);
        fragmentTransaction.commit();

    }

//    private void changeStatusBarColor(int color) {
//
//    }

    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mPondLayout = (RelativeLayout) findViewById(R.id.pond_layout_view);
        mMessageLayout = (RelativeLayout) findViewById(R.id.message_layout_view);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);

        mHomeLayout.setOnClickListener(this);
        mPondLayout.setOnClickListener(this);
        mMessageLayout.setOnClickListener(this);
        mMineLayout.setOnClickListener(this);

        mHomeView = (ImageView) findViewById(R.id.home_image_view);
        mPondView = (ImageView) findViewById(R.id.fish_image_view);
        mMessageView = (ImageView) findViewById(R.id.message_image_view);
        mMineView = (ImageView) findViewById(R.id.mine_image_view);
//        mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
    }

    private void hideFragment(Fragment fragment, FragmentTransaction transaction) {
        if (fragment != null){
            transaction.hide(fragment);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch(view.getId()){
             case R.id.home_layout_view:
                 hideFragment(mPondFragment,fragmentTransaction);
                 hideFragment(mMessageFragment,fragmentTransaction);
                 hideFragment(mMineFragment,fragmentTransaction);

                 if (mHomeFragment == null){
                     mHomeFragment = new HomeFragment();
                     fragmentTransaction.add(R.id.content_layout,mHomeFragment);
                 }else {
                     mCurrent = mHomeFragment;
                     fragmentTransaction.show(mHomeFragment);
                 }

                  break;
            case R.id.pond_layout_view:
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mMessageFragment,fragmentTransaction);
                hideFragment(mMineFragment,fragmentTransaction);

                if (mPondFragment == null){
                    mPondFragment = new PondFragment();
                    fragmentTransaction.add(R.id.content_layout,mPondFragment);
                }else {
                    mCurrent = mPondFragment;
                    fragmentTransaction.show(mPondFragment);
                }
                break;
            case R.id.message_layout_view:
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mPondFragment,fragmentTransaction);
                hideFragment(mMineFragment,fragmentTransaction);

                if (mMessageFragment == null){
                    mMessageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.content_layout,mMessageFragment);
                }else {
                    mCurrent = mMessageFragment;
                    fragmentTransaction.show(mMessageFragment);
                }
                break;
            case R.id.mine_layout_view:
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mPondFragment,fragmentTransaction);
                hideFragment(mMessageFragment,fragmentTransaction);

                if (mMineFragment == null){
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout,mMineFragment);
                }else {
                    mCurrent = mMineFragment;
                    fragmentTransaction.show(mMineFragment);
                }
                break;
            default:
                  break;
        }
        fragmentTransaction.commit();
    }


}
