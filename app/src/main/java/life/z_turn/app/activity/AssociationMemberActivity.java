package life.z_turn.app.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.AssociationMemberAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.LogUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;


@EActivity(R.layout.activity_association_member)
public class AssociationMemberActivity extends BaseActivity implements AssociationMemberAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private List<AVObject> mDataSet;
    private AssociationMemberAdapter mAdapter;
    private AVObject mAssociation;

    @Extra(Constants.EXTRA_KEY_OBJECT_STRING)
    String mObjectStr;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @AfterViews
    void initViews() {
        setUpToolbar();
        setSwipeRefreshLayout();
        setUpRecyclerView();

        try {
            mAssociation = AVObject.parseAVObject(mObjectStr);
            LogUtil.i("mAssociation", mAssociation.getObjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 加载数据
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });

    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("成员");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyItemDecoration(ContextCompat.getDrawable(this, R.drawable.item_shape)));
        mDataSet = new ArrayList<>();
        mAdapter = new AssociationMemberAdapter(this, mDataSet);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query.whereEqualTo(Constants.COLUMN_ASSOCIATION, mAssociation);
        query.include(Constants.COLUMN_USER);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        mDataSet.clear();
                        mDataSet.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        showToast("无数据");
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(AssociationMemberActivity.this, e);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_association_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        UserProfileActivity_.intent(this).extra(Constants.EXTRA_KEY_USER_STRING_OBJECT,mDataSet.get(pos).getAVUser(Constants.COLUMN_USER).toString()).start();
    }

    @Override
    public void onRefresh() {

    }
}
