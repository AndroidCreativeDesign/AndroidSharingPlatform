package cn.daixiaodong.myapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.IdeaDetailActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.adapter.UserCollectListAdapter;
import cn.daixiaodong.myapp.config.Constants;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

import static android.support.v7.widget.RecyclerView.OnScrollListener;


/**
 * 用户收藏的idea
 */
public class UserCollectFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, UserCollectListAdapter.OnItemClickListener {


    private View mConvertView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private UserCollectListAdapter mAdapter;
    private List<AVObject> mData;

    // 登录提示 View
    private View mSignInTip;
    private int mOffset;
    private boolean isFirstRefresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_collect, container, false);
        return mConvertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_collect_recycler_view);
        setUpRecyclerView();
        setRefreshLayout();
    }

    private void setRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new UserCollectListAdapter(getActivity(), mData);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    if (!mRefreshLayout.isRefreshing()) {
                        mRefreshLayout.setRefreshing(true);
                        loadMoreData();

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    private void refreshData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_COLLECT);
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.include("idea");
        query.orderByDescending("createdAt");
        query.limit(1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("collectId");
                    }else{
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void loadMoreData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_COLLECT);
        query.setLimit(1);
        query.whereEqualTo("user", user);
        query.include("idea");
        query.include("idea.user");
        query.orderByDescending("createdAt");
        query.whereLessThan("collectId", mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("collectId");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        refreshData();
    }


    /**
     * 接收 SignInActivity 的返回数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE) {
            if (resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
                updateUI();
                mRefreshLayout.setRefreshing(true);
                refreshData();
                isFirstRefresh = false;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstRefresh) {
            if (isSignIn()) {
                isFirstRefresh = false;
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        refreshData();
                        Log.i("onResume", "onResume");
                    }
                });
            } else {
                updateUI();
            }
        }
    }

    /**
     * 当视图显示时，更新界面状态
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateUI();
        }
    }


    /**
     * 根据登录状态显示或隐藏  登录提示
     */
    public void updateUI() {
        if (isSignIn()) {
            if (mSignInTip != null) {
                mSignInTip.setVisibility(View.GONE);
                mRefreshLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mSignInTip == null) {
                ViewStub viewStub = (ViewStub) mConvertView.findViewById(R.id.id_vs_login_in_prompt);
                mSignInTip = viewStub.inflate();
                mSignInTip.findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        SignInActivity_.intent(UserCollectFragment.this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                    }

                });
            }
            mRecyclerView.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.GONE);
            mSignInTip.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos, int viewType) {
        AVObject object = mData.get(pos).getAVObject("idea");
        String objectId = object.getObjectId();
        String title = object.getString("title");
        IdeaDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
    }
}
