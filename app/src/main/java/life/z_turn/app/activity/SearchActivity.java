package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.IdeaNewAdapter;
import life.z_turn.app.config.Constants;


/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements IdeaNewAdapter.OnItemClickListener {

    private ArrayList<AVObject> mData;
    private IdeaNewAdapter mAdapter;
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
        mAdapter = new IdeaNewAdapter(this, mData);
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
                }
            }
        });

    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        String objectStr = object.toString();
        //String title = mData.get(pos).getString(Constants.COLUMN_TITLE);
        IdeaDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, objectStr).start();
    }
}
