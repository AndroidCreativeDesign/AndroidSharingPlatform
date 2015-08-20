package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.OtherUserJoinListAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.LogUtil;
import life.z_turn.app.utils.ToastUtil;


@EActivity
public class UserJoinActivity extends BaseActivity implements OtherUserJoinListAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private List<AVObject> mData;
    private OtherUserJoinListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mOffset;


    @Extra(Constants.EXTRA_KEY_USER_STRING_OBJECT)
    String mUserObjectString;


    @Extra(Constants.EXTRA_KEY_OBJECT_ID)
    String mObjectId;

    @Extra("username")
    String username;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @AfterViews
    void initViews() {
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("参与");
//        mToolbar.setSubtitle("TA参与的");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


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
                refreshData();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        AVQuery<AVUser> innerQuery = AVUser.getQuery();
        innerQuery.whereEqualTo("objectId", mObjectId);
        LogUtil.i("objectId", mObjectId);
        AVQuery<AVObject> query = AVQuery.getQuery(Constants.TABLE_USER_JOIN);
        query.whereMatchesQuery(Constants.COLUMN_USER, innerQuery);
        query.include(Constants.COLUMN_IDEA);
        query.include(Constants.COLUMN_ASSOCIATION);
        query.include("idea.user");
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.findInBackground(new FindCallback<AVObject>() {
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
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserJoinActivity.this, e);
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
        mRecyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new OtherUserJoinListAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    if (!mSwipeRefreshLayout.isRefreshing()) {

                        if (mAdapter.getItemCount() < 10) {
                            return;
                        }
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
        int type = object.getInt(Constants.COLUMN_TYPE);
        switch (type) {
            case Constants.TYPE_ASSOCIATION:
                AVObject association = object.getAVObject(Constants.COLUMN_ASSOCIATION);
                String stringObject = association.toString();
                String associationName = association.getString(Constants.COLUMN_ASSOCIATION_NAME);
                AssociationDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT, stringObject).mAssociationName(associationName).mAssociationId(association.getObjectId()).start();
                break;
            case Constants.TYPE_STUDENT_UNION_DEPARTMENT:
                AVObject department = object.getAVObject(Constants.COLUMN_DEPARTMENT);
                String departmentStringObject = department.toString();
                String departmentId = department.getObjectId();
                DepartmentDetailActivity_.intent(this)
                        .extra(DepartmentDetailActivity.EXTRA_KEY_DEPARTMENT_ID, departmentId)
                        .extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT, departmentStringObject)
//                .extra(Constants.EXTRA_KEY_COLLEGE,mCollegeName)
                        .extra(Constants.EXTRA_KEY_DEPARTMENT_NAME, department.getString("name"))
                        .start();
                break;
            default:
                AVObject idea = object.getAVObject(Constants.COLUMN_IDEA);
                String objectStr = idea.toString();
                //String title = mData.get(pos).getString(Constants.COLUMN_TITLE);
                IdeaDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, objectStr).start();
                break;

        }

    }
}
