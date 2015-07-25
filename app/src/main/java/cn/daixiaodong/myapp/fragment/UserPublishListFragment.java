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
import cn.daixiaodong.myapp.activity.IdeaDetailActivity_;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("UserPublishListFragment", "onCreateView");
        mView = inflater.inflate(R.layout.fragment_create_list, container, false);
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
                loadData();
            }
        });
    }


    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>("idea");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
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
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("UserPublishListFragment", "onPause");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("UserPublishListFragment", "onResume");

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("UserPublishListFragment", "onCreate");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("UserPublishListFragment", "onActivityResult");

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("UserPublishListFragment", "onAttach");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("UserPublishListFragment", "onDestroy");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("UserPublishListFragment", "onDestroyView");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("UserPublishListFragment", "onDetach");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("UserPublishListFragment", "onHiddenChanged");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("UserPublishListFragment", "onConfigurationChanged");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("UserPublishListFragment", "onSaveInstanceState");

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("UserPublishListFragment", "onViewStateRestored");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("UserPublishListFragment", "onStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("UserPublishListFragment", "onStop");

    }

}
