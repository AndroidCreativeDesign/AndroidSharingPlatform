package life.z_turn.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.activity.UserProfileActivity_;
import life.z_turn.app.adapter.UserFollowListAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;


/**
 * 用户关注的其他用户
 */
public class UserFollowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, UserFollowListAdapter.OnItemClickListener {


    private View mConvertView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private UserFollowListAdapter mAdapter;
    private List<AVObject> mData;
    // 登录提示 View
    private View mSignInTip;
    private boolean isFirstRefresh = true;
    private CircularProgressView mViewProgressLoad;
    private TextView mTextNoMoreData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_follow, container, false);
        return mConvertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {

        setUpRecyclerView();
        setRefreshLayout();

    }

    private void setRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_collect_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new UserFollowListAdapter(getActivity(), mData);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.addItemDecoration(new MyItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.item_shape)));
        mRecyclerView.setAdapter(mAdapter);

    /*    // 添加FooterView
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        mViewProgressLoad = (CircularProgressView) view.findViewById(R.id.progress_load);
        mTextNoMoreData = (TextView) view.findViewById(R.id.text_no_more_data);
        view.setVisibility(View.GONE);
        mAdapter.addFooterView(view);*/

    }

    @Override
    public void onRefresh() {
        loadData();
    }


    private void loadData() {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<AVObject> relation = user.getRelation(Constants.COLUMN_FOLLOW);
        relation.getQuery().findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {

                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
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
                        loadData();
                    }
                });
            } else {
                updateUI();
            }
        }

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVUser followUser = (AVUser) mData.get(pos);
        UserProfileActivity_.intent(getActivity()).extra(Constants.EXTRA_KEY_USER_STRING_OBJECT, followUser.toString()).start();
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

        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE
                && resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            updateUI();
            mRefreshLayout.setRefreshing(true);
            loadData();
            isFirstRefresh = false;
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
