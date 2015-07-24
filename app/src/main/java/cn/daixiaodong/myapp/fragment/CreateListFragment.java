package cn.daixiaodong.myapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.DreamDetailActivity_;
import cn.daixiaodong.myapp.adapter.PublishAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 * 用户创建的Dream列表
 */
@EFragment
public class CreateListFragment extends BaseFragment {


    private View mView;


    private RecyclerView mPublishRecyclerView;
    private List<AVObject> mData;
    private PublishAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("CreateListFragment", "onCreateView");
        mView = inflater.inflate(R.layout.fragment_create_list, container, false);
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("CreateListFragment", "onActivityCreated");

        setUpRecyclerView();
        setUpSwipeRefreshLayout();


    }


    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }


    private void loadData() {
        AVQuery<AVObject> dreams = new AVQuery<>("dream");
        dreams.whereEqualTo("user", AVUser.getCurrentUser());
        dreams.orderByDescending("createdAt");
        // dreams.include("user");
        dreams.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    mData.addAll(list);
                    mAdapter.notifyItemInserted(0);
                  /*  for (AVObject object : list) {
                        Log.i("title", object.getString("title"));
                        AVUser user = object.getAVUser("user");
                        Log.i("username", user.getUsername());
                    }*/
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setUpRecyclerView() {
        mPublishRecyclerView =
                (RecyclerView) mView.findViewById(R.id.id_rv_publish_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mPublishRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new PublishAdapter(getActivity(), mData);
        mPublishRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new PublishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PublishAdapter.MyViewHolder viewHolder, int pos) {
                AVObject object = mData.get(pos);
                String objectId = object.getObjectId();
                String title = object.getString("title");
                DreamDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("CreateListFragment", "onPause");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("CreateListFragment", "onResume");

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CreateListFragment", "onCreate");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("CreateListFragment", "onActivityResult");

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("CreateListFragment", "onAttach");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CreateListFragment", "onDestroy");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("CreateListFragment", "onDestroyView");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("CreateListFragment", "onDetach");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("CreateListFragment", "onHiddenChanged");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("CreateListFragment", "onConfigurationChanged");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("CreateListFragment", "onSaveInstanceState");

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("CreateListFragment", "onViewStateRestored");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("CreateListFragment", "onStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("CreateListFragment", "onStop");

    }

}
