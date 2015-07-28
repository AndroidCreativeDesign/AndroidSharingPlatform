package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import cn.daixiaodong.myapp.adapter.UserJoinListAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 * 用户参与的idea列表
 */

public class UserJoinListFragment extends BaseFragment {
    private View mView;


    private RecyclerView mJoinRecyclerView;
    private List<AVObject> mData;
    private UserJoinListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mSignInPromptView;
    private boolean isFirstLoad = true;


    private int mOffset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_join_list, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpSwipeRefreshLayout();
        setUpRecyclerView();

    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>("user_join");
        query.setLimit(1);
        query.whereEqualTo("user", user);
        query.include("idea");
        query.include("idea.user");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("joinId");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadMoreData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>("user_join");
        query.setLimit(1);
        query.whereEqualTo("user", user);
        query.include("idea");
        query.include("idea.user");
        query.orderByDescending("createdAt");
        query.whereLessThan("joinId", mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        Log.i("list size",list.size()+"");
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("joinId");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mJoinRecyclerView =
                (RecyclerView) mView.findViewById(R.id.id_rv_join_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mJoinRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new UserJoinListAdapter(getActivity(), mData);
        mJoinRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new UserJoinListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserJoinListAdapter.MyViewHolder viewHolder, int pos) {
                AVObject object = mData.get(pos).getAVObject("idea");
                String objectId = object.getObjectId();
                String title = object.getString("title");
                IdeaDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
            }
        });
        mJoinRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSignIn()) {

            if (isFirstLoad) {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }
                });
                isFirstLoad = false;
            }
            if (mSignInPromptView != null) {
                mSignInPromptView.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mJoinRecyclerView.setVisibility(View.VISIBLE);


            }
        } else {
            if (mSignInPromptView == null) {
                ViewStub viewStub = (ViewStub) mView.findViewById(R.id.id_vs_login_in_prompt);
                mSignInPromptView = viewStub.inflate();
                mSignInPromptView.findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        SignInActivity_.intent(UserJoinListFragment.this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                    }

                });
            }
            mJoinRecyclerView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSignInPromptView.setVisibility(View.VISIBLE);
        }

    }

}
