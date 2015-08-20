package life.z_turn.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.melnykov.fab.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.AssociationTopicActivity_;
import life.z_turn.app.activity.EditIdeaDetailActivity_;
import life.z_turn.app.activity.IdeaDetailActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.activity.StudentUnionTopicActivity_;
import life.z_turn.app.adapter.IdeaAdapter;
import life.z_turn.app.adapter.TopicViewPagerAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.DisplayUtil;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.LogUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * 所有的idea列表
 */
public class IdeaListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IdeaAdapter.OnItemClickListener, TopicViewPagerAdapter.OnItemClickListener {


    private View mConvertView;
    private RecyclerView mIdeaRecyclerView;
    private List<AVObject> mData;
    private IdeaAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyHandler mHandler;

    private int mOffset;
    private boolean isFirstIn = true;

    private List<ImageView> mDotImageView;
    private ImageView mCurrentDot;
    private ViewPager mViewPager;
    private ArrayList<AVObject> mTopicDataSet;

    private int mCurrentItem;
    private LinearLayout mLLayoutDot;
    private TopicViewPagerAdapter mTopicAdapter;
    private CircularProgressView mViewProgressLoad;
    private TextView mTextNoMoreData;
    private boolean isLoadingMoreData;
    private boolean isEnableLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_idea_list, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new MyHandler(new WeakReference<>(this));
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
        setUpFloatingActionButton();
        setUpViewPager();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshData();
                loadTopicData();
            }
        });
    }

    private void loadTopicData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_TOPIC);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mTopicDataSet.clear();
                    mTopicDataSet.addAll(list);
                    setTopicData();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void setTopicData() {

        mDotImageView = new ArrayList<>();
        mLLayoutDot.removeAllViews();
        for (int i = 0; i < mTopicDataSet.size(); i++) {
            ImageView dotView = new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = DisplayUtil.dip2px(getActivity(), 4);
            dotView.setBackgroundResource(R.drawable.dot_normal);
            mLLayoutDot.addView(dotView, lp);
            mDotImageView.add(dotView);
        }
        // setCurrentDot(0);
        mTopicAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessageDelayed(MyHandler.MSG_UPDATE_IMAGE, MyHandler.MSG_DELAY);


    }


    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void refreshData() {
        isEnableLoad = true;
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_IDEA);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.setLimit(Constants.PAGE_SIZE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_IDEA_ID);
                    mData.clear();
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void loadMoreData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_IDEA);
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.whereLessThan(Constants.COLUMN_IDEA_ID, mOffset);
        query.setLimit(Constants.PAGE_SIZE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (list != null && !list.isEmpty()) {
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_IDEA_ID);
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        isLoadingMoreData = false;
                    } else {
                        mViewProgressLoad.setVisibility(View.GONE);
                        mTextNoMoreData.setVisibility(View.VISIBLE);
                        isEnableLoad = false;
                        isLoadingMoreData = false;
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }


    private void setUpRecyclerView() {


        mIdeaRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_idea_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mIdeaRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new IdeaAdapter(getActivity(), mData);
        mIdeaRecyclerView.setHasFixedSize(true);
        mIdeaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mIdeaRecyclerView.setAdapter(mAdapter);
        mIdeaRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

//                        mSwipeRefreshLayout.setRefreshing(true);
                    if (!isLoadingMoreData && isEnableLoad && mData.size()  >= Constants.PAGE_SIZE) {
                        loadMoreData();
                        isLoadingMoreData = true;
                        mViewProgressLoad.setVisibility(View.VISIBLE);
                        mTextNoMoreData.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });


        mAdapter.setOnItemClickListener(this);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mIdeaRecyclerView, false);
        mViewProgressLoad = (CircularProgressView) view.findViewById(R.id.progress_load);
        mTextNoMoreData = (TextView) view.findViewById(R.id.text_no_more_data);
        mAdapter.addFooterView(view);
    }


    private void setUpFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) mConvertView.findViewById(R.id.id_fab_floating_action_button);
        fab.attachToRecyclerView(mIdeaRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 事件统计
                // 判断是否登录

                AVAnalytics.onEvent(getActivity(), Constants.EVENT_USER_CLICK_CREATE_IDEA_BUTTON);
                if (AVUser.getCurrentUser() == null) {
                    // 子Fragment无法取得传回的数据，父Fragment以及宿主Activity可以取得，此处通过父Fragment取得数据
                    // 参考：http://www.tuicool.com/articles/2eM32a
                    SignInActivity_.intent(getParentFragment()).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                } else {
//                    PublishIdeaActivity_.intent(getActivity()).start();
//                    UpdateActivity_.intent(getParentFragment()).start();
                    EditIdeaDetailActivity_.intent(getActivity()).start();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshData();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*case R.id.action_create:
                AVAnalytics.onEvent(getActivity(), "create_dream");
                if (!checkUserStatus()) {
                    SignInActivity_.intent(getActivity()).extra("log_in_toward", 0).start();
                } else {
                    PublishIdeaActivity_.intent(getActivity()).start();
                }
                break;*/
        /*    case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;*/
        }
        return true;
    }


    private void setUpViewPager() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view_pager, null);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mLLayoutDot = (LinearLayout) view.findViewById(R.id.llayout_dot);
        mCurrentDot = (ImageView) view.findViewById(R.id.img_dot);
        int displayWidthPixels = DisplayUtil.getDisplayWidthPixels(getActivity());
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (displayWidthPixels) * 9 / 16;
        mViewPager.setLayoutParams(layoutParams);
        mTopicDataSet = new ArrayList<>();
        mTopicAdapter = new TopicViewPagerAdapter(getActivity(), mTopicDataSet);
        mTopicAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(mTopicAdapter);
        mAdapter.addHeaderView(view);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mCurrentDot.getLayoutParams();
                lp.leftMargin = (int) ((position + positionOffset) * DisplayUtil.dip2px(getActivity(), 14));
                mCurrentDot.setLayoutParams(lp);
               /* LogUtil.i("positionOffsetPixels", positionOffsetPixels + "");
                LogUtil.i("positionOffset", positionOffset + "");
                LogUtil.i("position", position + "");*/
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mHandler.sendEmptyMessage(MyHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        mHandler.sendEmptyMessageDelayed(MyHandler.MSG_UPDATE_IMAGE, MyHandler.MSG_DELAY);
                        break;
                }

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        AVObject object = mTopicDataSet.get(position);
        switch (object.getInt(Constants.COLUMN_TYPE)) {
            case Constants.TYPE_TOPIC:
                String typeName = object.getString(Constants.COLUMN_TYPE_NAME);
                if (Constants.ASSOCIATION_TOPIC.equals(typeName)) {
                    Intent intent = new Intent(getActivity(), AssociationTopicActivity_.class);
                    startActivity(intent);
                } else if (Constants.STUDENT_UNION_TOPIC.equals(typeName)) {
                    StudentUnionTopicActivity_.intent(this).start();
                }
                break;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.i("setUserVisibleHint", isVisibleToUser + "");
        if (isVisibleToUser) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(MyHandler.MSG_UPDATE_IMAGE, MyHandler.MSG_DELAY);
            }
        } else {
            if (mHandler != null)
                mHandler.sendEmptyMessage(MyHandler.MSG_KEEP_SILENT);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MyHandler.MSG_UPDATE_IMAGE, MyHandler.MSG_DELAY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MyHandler.MSG_KEEP_SILENT);
        }
    }

    public void setCurrentItem() {
        if (mCurrentItem != mTopicDataSet.size() - 1) {
            mViewPager.setCurrentItem(mCurrentItem + 1);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }


    private static class MyHandler extends Handler {

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;
        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<IdeaListFragment> weakReference;

        protected MyHandler(WeakReference<IdeaListFragment> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("LOG_TAG", "receive message " + msg.what);
            IdeaListFragment fragment = weakReference.get();
            if (fragment == null) {
                //Activity已经回收，无需再处理UI了
                return;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (fragment.mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                fragment.mHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    fragment.setCurrentItem();
                    //准备下次播放
                    fragment.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    fragment.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。

                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {
        AVObject object = mData.get(pos);
        String objectStr = object.toString();
        //String title = mData.get(pos).getString(Constants.COLUMN_TITLE);
        IdeaDetailActivity_.intent(getActivity()).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, objectStr).start();
    }

}
