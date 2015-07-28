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
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.activity.UserProfileActivity_;
import cn.daixiaodong.myapp.adapter.UserFollowListAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;


/**
 *  用户关注的其他用户
 */
public class UserFollowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, UserFollowListAdapter.OnItemClickListener {

    private View mConvertView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private UserFollowListAdapter mAdapter;
    private List<AVObject> mData;
    // 登录提示 View
    private View mSignInTip;

    private int mOffset;

    private boolean isFirstRefresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView
                = inflater.inflate(R.layout.fragment_follow, container, false);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new UserFollowListAdapter(getActivity(), mData);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {

            AVUser user = AVUser.getCurrentUser();
            AVRelation<AVObject> relation = user.getRelation("follow");
            relation.getQuery().findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    mRefreshLayout.setRefreshing(false);
                    if (e == null) {
                        if (list.isEmpty()) {

                        } else {
                            mData.clear();
                            mData.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isFirstRefresh){
            if (isSignIn()) {
                isFirstRefresh = false;
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        loadData(true);
                        Log.i("onResume", "onResume");
                    }
                });
            } else {
                updateUI();
            }
        }

    }

    @Override
    public void onItemClick(UserFollowListAdapter.MyViewHolder viewHolder, int pos) {
        UserProfileActivity_.intent(this).start();
    }

    @Override
    public void onUnfollowBtnClick(UserFollowListAdapter.MyViewHolder viewHolder, int pos) {
        Log.i("按钮被点击了", "第" + pos + "个");
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
        Log.i("onActivityResult", "onActivityResult");

        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE) {
            if (resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
                updateUI();
                mRefreshLayout.setRefreshing(true);
                loadData(true);
                isFirstRefresh = false;
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
                        SignInActivity_.intent(UserFollowFragment.this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                    }

                });
            }
            mRecyclerView.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.GONE);
            mSignInTip.setVisibility(View.VISIBLE);
        }
    }
}
