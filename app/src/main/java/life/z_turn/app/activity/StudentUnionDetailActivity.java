package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.StudentUnionDepartmentAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;

@EActivity
public class StudentUnionDetailActivity extends BaseActivity implements StudentUnionDepartmentAdapter.OnItemClickListener {


    private List<AVObject> mDataSet;

    private StudentUnionDepartmentAdapter mAdapter;


    @Extra("studentUnionId")
    String mStudentUnionId;
    @Extra(Constants.EXTRA_KEY_COLLEGE)
    String mCollegeName;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @ViewById(R.id.progress_load)
    CircularProgressView mProgressLoad;


    @AfterViews
    void init() {
        setUpToolbar();
        setUpRecyclerView();
        showProgressLoad(true);
        loadData();
    }

    private void showProgressLoad(boolean showing) {
        if (showing) {
            mProgressLoad.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressLoad.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    private void loadData() {
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_STUDENT_UNION);
        innerQuery.whereEqualTo(Constants.COLUMN_OBJECT_ID, mStudentUnionId);
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_STUDENT_UNION_DEPARTMENT);
//        query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.whereMatchesQuery(Constants.COLUMN_STUDENT_UNION, innerQuery);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                showProgressLoad(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mDataSet.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showToast("无数据");
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(StudentUnionDetailActivity.this, e);
                }
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(mCollegeName);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    private void setUpRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new MyItemDecoration(getResources().getDrawable(R.drawable.item_shape)));
        mDataSet = new ArrayList<>();
        mAdapter = new StudentUnionDepartmentAdapter(this, mDataSet);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_union_detail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_union_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
        AVObject object = mDataSet.get(position);
        String departmentStringObject = object.toString();
        String departmentId = object.getObjectId();
        DepartmentDetailActivity_.intent(this)
                .extra(DepartmentDetailActivity.EXTRA_KEY_DEPARTMENT_ID, departmentId)
                .extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT, departmentStringObject)
//                .extra(Constants.EXTRA_KEY_COLLEGE,mCollegeName)
                .extra(Constants.EXTRA_KEY_DEPARTMENT_NAME, object.getString("name"))
                .start();
    }
}
