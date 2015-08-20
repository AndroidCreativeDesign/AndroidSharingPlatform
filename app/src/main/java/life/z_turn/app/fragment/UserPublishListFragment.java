package life.z_turn.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.EditCrowdfundingActivity_;
import life.z_turn.app.activity.EditDefaultActivity_;
import life.z_turn.app.activity.EditRecruitmentActivity_;
import life.z_turn.app.activity.IdeaDetailActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.adapter.UserPublishListAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * 用户创建的idea列表
 */
@EFragment
public class UserPublishListFragment extends BaseFragment implements UserPublishListAdapter.OnItemClickListener {


    private View mConvertView;
    private RecyclerView mPublishRecyclerView;
    private List<AVObject> mData;
    private UserPublishListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mSignInPromptView;
    private boolean isFirstLoad = true;

    private int mOffset;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_publish_list, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
    }


    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_IDEA);
        query.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.setLimit(Constants.PAGE_SIZE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = mData.get(mData.size() - 1).getInt(Constants.COLUMN_IDEA_ID);
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }


    private void setUpRecyclerView() {
        mPublishRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_publish_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mPublishRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new UserPublishListAdapter(getActivity(), mData);
        mPublishRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        mPublishRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMoreData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_IDEA);
        query.setLimit(Constants.PAGE_SIZE);
        query.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.whereLessThan(Constants.COLUMN_IDEA_ID, mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_IDEA_ID);
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
        if (isSignIn()) {
            if (isFirstLoad) {

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
                mPublishRecyclerView.setVisibility(View.VISIBLE);
            }

        } else {
            if (mSignInPromptView == null) {
                ViewStub viewStub = (ViewStub) mConvertView.findViewById(R.id.id_vs_login_in_prompt);
                mSignInPromptView = viewStub.inflate();
                mSignInPromptView.findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        SignInActivity_.intent(UserPublishListFragment.this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                    }

                });
            }
            mPublishRecyclerView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSignInPromptView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        IdeaDetailActivity_.intent(getActivity()).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, object.toString()).start();
    }

    @Override
    public void onEditBtnClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        int type = object.getInt(Constants.COLUMN_TYPE);
        switch (type) {
            case Constants.TYPE_DEFAULT:
                EditDefaultActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING, object.toString()).start();
                break;
            case Constants.TYPE_CROWDFUNDING:
                EditCrowdfundingActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING, object.toString()).start();
                break;
            case Constants.TYPE_RECRUITMENT:
                EditRecruitmentActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING, object.toString()).start();
                break;
        }

    }

    @Override
    public void onCheckBtnClick(RecyclerView.ViewHolder viewHolder, int pos) {

    }
}
