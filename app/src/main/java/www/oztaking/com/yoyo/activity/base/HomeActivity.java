package www.oztaking.com.yoyo.activity.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import www.oztaking.com.yoyo.R;
import www.oztaking.com.yoyo.view.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    private RelativeLayout mHomeLayout;
    private RelativeLayout mPondLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private ImageView mHomeView;
    private ImageView mPondView;
    private ImageView mMessageView;
    private ImageView mMineView;
    private HomeFragment mHomeFragment;

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
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch(view.getId()){
             case R.id.home_layout_view:

//                 //在显示homeFragment的时候，需要隐藏其他的fragment；
//                 hideFragment(mCommonFragmentOne, fragmentTransaction);
//                 hideFragment(mMessageFragment, fragmentTransaction);
//                 hideFragment(mMineFragment, fragmentTransaction);
                 //显示homeFragment，不为空则显示；
                 if (mHomeFragment == null) {
                     mHomeFragment = new HomeFragment();
                     fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                 } else {
                     mCurrent = mHomeFragment;
                     fragmentTransaction.show(mHomeFragment);
                 }
                  break;
            case R.id.pond_layout_view:
                break;
            case R.id.message_layout_view:
                break;
            case R.id.mine_layout_view:
                break;

            default:
                  break;
        }


    }

    private void hideFragment(Fragment fragment,FragmentTransaction fm){
        if (fragment != null){
            fm.hide(fragment);
        }
    }
}
