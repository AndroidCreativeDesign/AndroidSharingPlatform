package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.adapter.IdeaAdapter;


/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements IdeaAdapter.OnItemClickListener {

    private ArrayList<AVObject> mData;

    private IdeaAdapter mAdapter;
    private AVQuery<AVObject> mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        SearchView searchView = (SearchView) findViewById(R.id.id_search_view);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("搜索被点击");
            }
        });


        setUpRecyclerView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                showResult(newText);

                return true;
            }
        });

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_rv_search_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new IdeaAdapter(this, mData);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResult(final String newText) {
        if (newText.isEmpty()) {
            return;
        }
        if (mQuery != null) {
            mQuery.cancel();

        }
        mQuery = new AVQuery<>("idea");
        Log.i("newText", newText);
        mQuery.whereContains("title", newText.trim());
        mQuery.setLimit(10);
        mQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    mData.clear();
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    Log.i("resultSize", list.size() + "");
                }
            }
        });

    }

    @Override
    public void onItemClick(IdeaAdapter.MyViewHolder viewHolder, int pos) {
        String objectId = mData.get(pos).getObjectId();
        String title = mData.get(pos).getString("title");
        IdeaDetailActivity_.intent(this).objectId(objectId).title(title).start();
    }
}
