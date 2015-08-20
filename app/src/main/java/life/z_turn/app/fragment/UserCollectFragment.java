package life.z_turn.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.IdeaDetailActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.adapter.IdeaNewAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

import static android.support.v7.widget.RecyclerView.OnScrollListener;


/**
 * 用户收藏的idea
 */
public class UserCollectFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IdeaNewAdapter.OnItemClickListener {


    private View mConvertView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private IdeaNewAdapter mAdapter;
    private List<AVObject> mData;

    // 登录提示 View
    private View mSignInTip;
    private int mOffset;
    private boolean isFirstRefresh = true;
    private CircularProgressView mViewProgressLoad;
    private TextView mTextNoMoreData;

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
        setUpRecyclerView();
        setRefreshLayout();
    }

    private void setRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_collect_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new IdeaNewAdapter(getActivity(), mData);
        mAdapter.setOnItemClickListener(this);
        // 添加FooterView
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        mViewProgressLoad = (CircularProgressView) view.findViewById(R.id.progress_load);
        mTextNoMoreData = (TextView) view.findViewById(R.id.text_no_more_data);
        view.setVisibility(View.GONE);
        mAdapter.addFooterView(view);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {
                    if (mViewProgressLoad.getVisibility() == View.VISIBLE) {
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
        query.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());
        query.include(Constants.COLUMN_IDEA);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.limit(Constants.PAGE_SIZE);
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
                        if (list.size() == Constants.PAGE_SIZE) {
                            mViewProgressLoad.setVisibility(View.VISIBLE);
                        } else {
                            mViewProgressLoad.setVisibility(View.GONE);
                        }
                    } else {
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                        showToast(getActivity(), "无数据");
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }


    private void loadMoreData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_COLLECT);
        query.setLimit(Constants.PAGE_SIZE);
        query.whereEqualTo(Constants.COLUMN_USER, user);
        query.include(Constants.COLUMN_IDEA);
        query.include("idea.user");
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.whereLessThan(Constants.COLUMN_COLLECT_ID, mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_COLLECT_ID);
                        if (list.size() == Constants.PAGE_SIZE) {
                            mViewProgressLoad.setVisibility(View.VISIBLE);
                        } else {
                            mViewProgressLoad.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
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
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE
                && resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            updateUI();
            mRefreshLayout.setRefreshing(true);
            refreshData();
            isFirstRefresh = false;
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
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        String objectStr = object.toString();
        //String title = mData.get(pos).getString(Constants.COLUMN_TITLE);
        IdeaDetailActivity_.intent(getActivity()).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, objectStr).start();
    }
}