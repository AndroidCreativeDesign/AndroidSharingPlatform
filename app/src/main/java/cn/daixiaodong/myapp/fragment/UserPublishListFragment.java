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

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.IdeaDetailActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.adapter.UserPublishListAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 * 用户创建的idea列表
 */
@EFragment
public class UserPublishListFragment extends BaseFragment {


    private View mView;


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
        Log.i("UserPublishListFragment", "onCreateView");
        mView = inflater.inflate(R.layout.fragment_publish_list, container, false);
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("UserPublishListFragment", "onActivityCreated");

        setUpRecyclerView();
        setUpSwipeRefreshLayout();


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
        AVQuery<AVObject> query = new AVQuery<>("idea");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.setLimit(1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (e == null) {
                    Log.i("list.size()", list.size() + "");
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = mData.get(mData.size() - 1).getInt("ideaId");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setUpRecyclerView() {
        mPublishRecyclerView =
                (RecyclerView) mView.findViewById(R.id.id_rv_publish_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mPublishRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new UserPublishListAdapter(getActivity(), mData);
        mPublishRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new UserPublishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserPublishListAdapter.MyViewHolder viewHolder, int pos) {
                AVObject object = mData.get(pos);
                String objectId = object.getObjectId();
                String title = object.getString("title");
                IdeaDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
            }
        });

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
        AVQuery<AVObject> query = new AVQuery<>("idea");
        query.setLimit(1);
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.whereLessThan("ideaId", mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {

                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("ideaId");
                    }
                }else {
                    e.printStackTrace();
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
                ViewStub viewStub = (ViewStub) mView.findViewById(R.id.id_vs_login_in_prompt);
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
}
