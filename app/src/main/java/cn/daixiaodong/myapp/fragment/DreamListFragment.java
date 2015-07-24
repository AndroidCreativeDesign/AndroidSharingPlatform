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
import cn.daixiaodong.myapp.activity.CreateDreamActivity_;
import cn.daixiaodong.myapp.activity.DreamDetailActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.activity.TopicActivity_;
import cn.daixiaodong.myapp.adapter.DreamAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;
import cn.daixiaodong.myapp.utils.NetworkUtil;
import cn.daixiaodong.myapp.view.FixedRecyclerView;

/**
 * 所有的Dream列表
 */
public class DreamListFragment extends BaseFragment {

    private View mView;
    private FixedRecyclerView mDreamRecyclerView;
    private List<AVObject> mData;
    DreamAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mLastVisibleItem;

    private Date mLastDate;

    private Date mMinDate;
    private int mPager = 1;

    private boolean isFirstEnter = true;

    private int topicNum = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dream_list, container, false);
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
        /*if(NetworkUtil.isNetworkConnected(getActivity())){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadData();
                }
            });
        }else{
            final AVQuery<AVObject> query = new AVQuery<>("dream");
            query.orderByDescending("createAt");
            query.setLimit(10);
            query.setSkip(0);
            query.include("user");
            boolean b = query.hasCachedResult();
            if(b )
        }*/


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

        AVQuery<AVObject> query1 = new AVQuery<>("dream");
        query1.whereEqualTo("topic", 1);
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
        final AVQuery<AVObject> query = new AVQuery<>("dream");
        query.whereEqualTo("topic", 0);
        query.orderByDescending("createdAt");
        query.setLimit(1);
        query.include("user");
        /*if (isRefresh) {
            if (!isFirstEnter) {
                query.whereGreaterThan("createdAt", mLastDate);
            }
            query.setSkip(0);
        } else {
            query.whereLessThan("createdAt", mMinDate);
            query.setSkip(0);
        }*/

        if (!isRefresh) {
            query.whereLessThan("createdAt", mMinDate);
        }
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    //      isFirstEnter = false;
                    if (!list.isEmpty()) {
             /*           Log.i("list size", list.size() + "");
                        if (isRefresh) {
                            AVObject object0 = list.get(0);

                            mLastDate = object0.getCreatedAt();
                            Log.i("mLastDate", mLastDate.toString());
                        }*/

                        mMinDate = list.get(list.size() - 1).getCreatedAt();
//                        Log.i("mMinDate", mMinDate.toString());
                       /* for (AVObject object : list) {
                         //   AVUser user = object.getAVUser("user");

                            AVQuery<AVObject> query1 = new AVQuery<>("user_join");
                            query1.whereEqualTo("user", AVUser.getCurrentUser());

                            query1.whereEqualTo("dream", object);

                            query1.countInBackground(new CountCallback() {
                                @Override
                                public void done(int i, AVException e) {
                                    Log.i("count", i + "");
                                }
                            });
                        }*/
                        if (isRefresh) {
                            // mData.clear();
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
        mDreamRecyclerView =
                (FixedRecyclerView) mView.findViewById(R.id.id_rv_dream_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mDreamRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        float scale = getResources().getDisplayMetrics().density;


        mAdapter = new DreamAdapter(getActivity(), mData);
        mAdapter.setImageSize(width, scale);
        mDreamRecyclerView.setHasFixedSize(false);
        mDreamRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDreamRecyclerView.setAdapter(mAdapter);
        mDreamRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
                    //  mSwipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    // showToast(getActivity(), "刷新");
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
        fab.attachToRecyclerView(mDreamRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 事件统计
                // 判断是否登录

                AVAnalytics.onEvent(getActivity(), "create_dream");
                if (AVUser.getCurrentUser() == null) {
                    // 子Fragment无法取得传回的数据，父Fragment以及宿主Activity可以取得，此处通过父Fragment取得数据
                    // 参考：http://www.tuicool.com/articles/2eM32a
                    SignInActivity_.intent(getParentFragment()).startForResult(SignInActivity.LOGIN_REQUEST_CODE);
                } else {
                    CreateDreamActivity_.intent(getActivity()).start();
                }
            }
        });
        mAdapter.setOnItemClickListener(new DreamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DreamAdapter.MyViewHolder viewHolder, int pos) {

                int topic = mData.get(pos).getInt("topic");

                if (topic == 1) {
                    String topicName = mData.get(pos).getString("topicName");
                    if ("社团专题".equals(topicName)) {
                        Intent intent = new Intent(getActivity(), TopicActivity_.class);
                        startActivity(intent);
                    }
                } else if (topic == 0) {
                    String dreamId = mData.get(pos).getObjectId();
                    String dreamTitle = mData.get(pos).getString("title");

                    DreamDetailActivity_.intent(getActivity()).objectId(dreamId).title(dreamTitle).start();
                }

            }
        });
    }
}
