package cn.daixiaodong.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.AssociationTopicActivity_;
import cn.daixiaodong.myapp.activity.PublishIdeaActivity_;
import cn.daixiaodong.myapp.activity.IdeaDetailActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.adapter.IdeaAdapter;
import cn.daixiaodong.myapp.config.Constants;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;
import cn.daixiaodong.myapp.utils.NetworkUtil;

/**
 * 所有的idea列表
 */
public class IdeaListFragment extends BaseFragment {

    private View mView;
    private RecyclerView mIdeaRecyclerView;
    private List<AVObject> mData;
    IdeaAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mLastVisibleItem;


    private Date mMinDate;

    private int mPager = 1;


    private int topicNum = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_idea_list, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData(true);
            }
        });
    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
    }

    private void loadData(final boolean isRefresh) {

        AVQuery<AVObject> query1 = new AVQuery<>("idea");
        query1.whereEqualTo("tag", 1);
        query1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {

                    if (isRefresh) {
                        mData.clear();
                    }
                    topicNum = list.size();
                    mData.addAll(0, list);
                    //mAdapter.notifyDataSetChanged();
                    loadNotTopicData(isRefresh);
                }
            }
        });


    }

    private void loadNotTopicData(final boolean isRefresh) {
        final AVQuery<AVObject> query = new AVQuery<>("idea");
        query.whereNotEqualTo("tag", 1);
        query.orderByDescending("createdAt");
        query.setLimit(1);
        query.include("user");


        if (!isRefresh) {
            query.whereLessThan("createdAt", mMinDate);
        }
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {


                        mMinDate = list.get(list.size() - 1).getCreatedAt();
                        if (isRefresh) {
                            mData.addAll(topicNum, list);
                        } else {
                            mData.addAll(list);
                            mPager++;
                        }
                        mAdapter.notifyDataSetChanged();
                        if (!NetworkUtil.isNetworkConnected(getActivity())) {
                            showToast(getActivity(), "网络错误");
                        }
                    } else {
                        showToast(getActivity(), "没有更多数据了");
                    }
                } else {
                    e.printStackTrace();
                    if (e.getCode() == AVException.CACHE_MISS) {

                    }
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mIdeaRecyclerView =
                (RecyclerView) mView.findViewById(R.id.id_rv_idea_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mIdeaRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        float scale = getResources().getDisplayMetrics().density;


        mAdapter = new IdeaAdapter(getActivity(), mData);
        mAdapter.setImageSize(width, scale);
        mIdeaRecyclerView.setHasFixedSize(false);
        mIdeaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mIdeaRecyclerView.setAdapter(mAdapter);
        mIdeaRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mAdapter.getItemCount()) {

                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        loadNotTopicData(false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.id_fab_floating_action_button);
        fab.attachToRecyclerView(mIdeaRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 事件统计
                // 判断是否登录

                AVAnalytics.onEvent(getActivity(), "user_create_idea");
                if (AVUser.getCurrentUser() == null) {
                    // 子Fragment无法取得传回的数据，父Fragment以及宿主Activity可以取得，此处通过父Fragment取得数据
                    // 参考：http://www.tuicool.com/articles/2eM32a
                    SignInActivity_.intent(getParentFragment()).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                } else {
                    PublishIdeaActivity_.intent(getActivity()).start();
                }
            }
        });
        mAdapter.setOnItemClickListener(new IdeaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IdeaAdapter.MyViewHolder viewHolder, int pos) {

                int tag = mData.get(pos).getInt("tag");

                if (tag == Constants.TOPIC) {
                    String tagName = mData.get(pos).getString("tagName");
                    if (Constants.ASSOCIATION_TOPIC.equals(tagName)) {
                        Intent intent = new Intent(getActivity(), AssociationTopicActivity_.class);
                        startActivity(intent);
                    }
                } else {
                    String objectId = mData.get(pos).getObjectId();
                    String title = mData.get(pos).getString("title");

                    IdeaDetailActivity_.intent(getActivity()).objectId(objectId).title(title).start();
                }

            }
        });
    }
}
