package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.adapter.OtherUserJoinListAdapter;
import cn.daixiaodong.myapp.config.Constants;


@EActivity
public class UserJoinActivity extends BaseActivity implements OtherUserJoinListAdapter.OnItemClickListener {

    private RecyclerView mJoinRecyclerView;
    private List<AVObject> mData;
    private OtherUserJoinListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mOffset;


    private AVUser mUser;
    @Extra("userId")
     String mObjectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_join);
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                findUser();
                refreshData();
            }
        });
    }

    private void findUser() {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("objectId", mObjectId);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        mUser = list.get(0);
                        refreshData();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query.setLimit(1);
        query.whereEqualTo("user", mUser);
        query.include("idea");
        query.include("association");
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
                    } else {
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
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query.setLimit(1);
        query.whereEqualTo("user", user);
        query.include("idea");
        query.include("idea.user");
        query.include("association");
        query.orderByDescending("createdAt");
        query.whereLessThan("joinId", mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        Log.i("list size", list.size() + "");
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
                (RecyclerView) findViewById(R.id.id_rv_join_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mJoinRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new OtherUserJoinListAdapter(this, mData);
        mJoinRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

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
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        int type = object.getInt("type");
        switch (type) {
            case Constants.TYPE_ASSOCIATION:
                AVObject association = object.getAVObject(Constants.TABLE_ASSOCIATION);
                AssociationDetailActivity_.intent(this).mAssociationId(association.getObjectId()).mAssociationName(association.getString("name")).start();
                break;
            default:
                AVObject idea = object.getAVObject("idea");
                String title = idea.getString("title");
                IdeaDetailActivity_.intent(this).objectId(idea.getObjectId()).title(title).start();

        }

    }
}
