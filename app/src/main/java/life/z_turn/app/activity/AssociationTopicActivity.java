package life.z_turn.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.AssociationListAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.LogUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;

/**
 * 协会专题
 */
@EActivity(R.layout.activity_association_topic)
public class AssociationTopicActivity extends BaseActivity implements AssociationListAdapter.OnItemClickListener {


    private List<AVObject> mData;
    private AssociationListAdapter mAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.id_rv_recycler_view)
    RecyclerView mRecyclerView;


    @ViewById(R.id.progress_load)
    CircularProgressView mProgressLoad;
    private EditText mEditSearch;
    private AlertDialog mSearchDialog;
    private int mSelectedAssociationType = -1;


    @AfterViews
    void init() {
        initToolbar();
        setUpRecyclerView();
        loadData();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle("社团专题");
    }

    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_ASSOCIATION);
        query.orderByDescending(Constants.COLUMN_UPDATED_AT);
//        query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mProgressLoad.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                if (e == null) {
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();

                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(AssociationTopicActivity.this, e);
                }
            }
        });
    }

    private void setUpRecyclerView() {
      /*  mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyItemDecoration(ContextCompat.getDrawable(this, R.drawable.item_shape)));
        mAdapter = new AssociationListAdapter(this, mData);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                showSearchDialog();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_filter:
                showFilterDialog();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("搜索");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_search, null);
        mEditSearch = (EditText) view.findViewById(R.id.edit_search);
        mEditSearch.setMaxLines(1);
        mEditSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getQueryStr();
                    return true;
                }
                return false;

            }
        });

        builder.setView(view);
        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", null);
        mSearchDialog = builder.create();
        mSearchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface d) {
                mSearchDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getQueryStr();
                    }
                });
            }
        });
        mSearchDialog.show();

    }

    private void getQueryStr() {
        String s = mEditSearch.getText().toString();
        if (!s.trim().isEmpty()) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0);
            mSearchDialog.dismiss();
            searchAssociation(s);
        }
    }

    private void searchAssociation(String s) {
        List<AVObject> queryResultSet = new ArrayList<>();
    /*    int count = mData.size();
        for (int i = 0; i < count; i++) {
            AVObject o = mData.get(i);
            if (!o.getString("name").contains(s)) {
                //queryResultSet.add(o);
                mData.remove(o);
                count--;
                mAdapter.notifyItemRemoved(i);
            }
        }*/
        for (AVObject o : mData) {
            if (o.getString("name").contains(s)) {
                queryResultSet.add(o);
            }
        }
        mAdapter.isQuery = true;
        mAdapter.addQueryResultSet(queryResultSet);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);

    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.association_type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                searchAssociationByType(which);
            }
        });
        builder.show();
    }

    private void searchAssociationByType(int which) {
        List<AVObject> queryResultSet = new ArrayList<>();
        for (AVObject o : mData) {
            if (o.getInt("type") == which) {
                LogUtil.i("type", which + "");
                queryResultSet.add(o);
            }
        }
        mAdapter.isQuery = true;
        mAdapter.addQueryResultSet(queryResultSet);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        String stringObject = object.toString();
        String associationName = object.getString(Constants.COLUMN_ASSOCIATION_NAME);
        AssociationDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT, stringObject).mAssociationName(associationName).mAssociationId(object.getObjectId()).start();
    }

    @Override
    public void onShowAllBtnClick() {
        mAdapter.isQuery = false;
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }

   /* @Override
    public void onItemJoinClick(AssociationListAdapter.MyViewHolder viewHolder, int pos) {




//        RegistrationInformationActivity_.intent(AssociationTopicActivity.this).mAssociationId(mData.get(pos).getObjectId()).start();

        AVQuery<AVObject> query = new AVQuery<>("user_join_association");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.whereEqualTo("association", mData.get(pos));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        showToast("你已经加入了该协会，请勿重复报名");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssociationTopicActivity.this);
                        builder.setMessage("你已经加入了该协会，请勿重复报名");
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    } else {
                        RegistrationInformationActivity_.intent(AssociationTopicActivity.this).start();
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(AssociationTopicActivity.this, e);
                }
            }
        });
    }*/
}
