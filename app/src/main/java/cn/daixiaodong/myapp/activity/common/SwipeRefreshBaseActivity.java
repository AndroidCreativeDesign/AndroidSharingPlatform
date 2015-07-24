package cn.daixiaodong.myapp.activity.common;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by daixiaodong on 15/7/14.
 */
public abstract class SwipeRefreshBaseActivity extends ToolbarActivity {


    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setupSwipeRefreshLayout();
    }

    private void setupSwipeRefreshLayout() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

 /*       mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_3,
                R.color.refresh_progress_2,
                R.color.refresh_progress_1
        );*/

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataRefresh();
            }
        });
    }

    protected void requestDataRefresh() {
    }

    protected void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected void initView() {
      //  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh_layout);
    }
}
