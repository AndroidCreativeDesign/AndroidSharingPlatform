package life.z_turn.app.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.NotificationDetailActivity_;
import life.z_turn.app.adapter.PushMessageAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;


public class NotificationFragment extends BaseFragment implements PushMessageAdapter.OnItemClickListener {


    private View mConvertView;
    private RecyclerView mRecyclerView;
    private List<AVObject> mData;
    private PushMessageAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mOffset;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_notification, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshData();
            }
        });
    }


    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }


    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new PushMessageAdapter(getActivity(), mData);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyItemDecoration(getResources().getDrawable(R.drawable.item_shape)));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        mAdapter.setOnItemClickListener(this);
    }


    private void refreshData() {
        AVQuery<AVObject> query1 = new AVQuery<>(Constants.TABLE_NOTIFICATION);
        query1.whereDoesNotExist(Constants.COLUMN_USER);

        AVQuery<AVObject> query2 = new AVQuery<>(Constants.TABLE_NOTIFICATION);
        query2.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());

        List<AVQuery<AVObject>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        AVQuery<AVObject> mainQuery = AVQuery.or(queries);
        mainQuery.setLimit(Constants.PAGE_SIZE);
        mainQuery.orderByDescending(Constants.COLUMN_CREATED_AT);
        mainQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_DISCOVER_ID);
                    } else {
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showToast(getActivity(), "无数据");
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }


    private void loadMoreData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_NOTIFICATION);
        query.setLimit(Constants.PAGE_SIZE);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.whereLessThan(Constants.COLUMN_DISCOVER_ID, mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_DISCOVER_ID);
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    @Override
    public void onItemClick(PushMessageAdapter.MyViewHolder viewHolder, int pos) {
        NotificationDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING, mData.get(pos).toString()).start();
    }
}
