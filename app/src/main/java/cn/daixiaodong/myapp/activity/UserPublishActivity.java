package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.adapter.OtherUserPublishListAdapter;
import cn.daixiaodong.myapp.config.Constants;


@EActivity(R.layout.activity_user_publish)
public class UserPublishActivity extends AppCompatActivity implements OtherUserPublishListAdapter.OnItemClickListener {


    @Extra("userId")
    String mObjectId;

    @Extra("username")
    String mUsername;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    private AVUser mUser;


    private RecyclerView mPublishRecyclerView;
    private List<AVObject> mData;
    private OtherUserPublishListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mOffset;

    @AfterViews
    void initViews() {
        initToolbar();
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                findUser();
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("王二麻");
        mToolbar.setSubtitle("TA发起的");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void findUser() {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("objectId", "55ba35d3e4b0c170a520b83f");
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
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
                (RecyclerView) findViewById(R.id.recycler_user_publish);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPublishRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new OtherUserPublishListAdapter(this, mData);
        mPublishRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        mPublishRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    if (!mSwipeRefreshLayout.isRefreshing()) {

                        if (mAdapter.getItemCount() < Constants.PAGE_SIZE) {
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
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        String objectId = object.getObjectId();
        String title = object.getString("title");
        IdeaDetailActivity_.intent(this).objectId(objectId).title(title).start();
    }


}
