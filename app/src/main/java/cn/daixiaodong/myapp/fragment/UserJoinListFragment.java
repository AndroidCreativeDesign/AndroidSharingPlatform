package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.IdeaDetailActivity_;
import cn.daixiaodong.myapp.adapter.UserJoinListAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 * 用户参与的idea列表
 */

public class UserJoinListFragment extends BaseFragment {
    private View mView;


    private RecyclerView mJoinRecyclerView;
    private List<AVObject> mData;
    private UserJoinListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_join_list, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
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
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>("user_join");
        query.setSkip(0);
        query.setLimit(10);
        query.whereEqualTo("user", user);
        query.include("idea");
        query.include("idea.user");
        query.orderByDescending("createAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {

                    mData.addAll(list);
                    mAdapter.notifyItemInserted(0);
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mJoinRecyclerView =
                (RecyclerView) mView.findViewById(R.id.id_rv_join_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mJoinRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new UserJoinListAdapter(getActivity(), mData);
        mJoinRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new UserJoinListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserJoinListAdapter.MyViewHolder viewHolder, int pos) {
                AVObject object = mData.get(pos).getAVObject("idea");
                String objectId = object.getObjectId();
                String title = object.getString("title");
                IdeaDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
            }
        });
    }
}
