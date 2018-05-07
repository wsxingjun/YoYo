package www.oztaking.com.yoyo.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import www.oztaking.com.yoyo.R;

/**
 *
 */
public class PondFragment extends BaseFragment implements
          View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Context mContext;
    //UI
    private View mContentView;

    public PondFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mContext = getContext();
        mContentView = inflater.inflate(R.layout.fragment_pond_layout,container,false);
        return mContentView;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
