package life.z_turn.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.AssociationDetailActivity_;
import life.z_turn.app.activity.AssociationMemberActivity_;
import life.z_turn.app.activity.DepartmentDetailActivity;
import life.z_turn.app.activity.DepartmentDetailActivity_;
import life.z_turn.app.activity.IdeaDetailActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.adapter.UserJoinListAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * 用户参与的idea列表
 */

public class UserJoinListFragment extends BaseFragment implements UserJoinListAdapter.OnItemClickListener {


    private View mConvertView;
    private RecyclerView mJoinRecyclerView;
    private List<AVObject> mData;
    private UserJoinListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mSignInPromptView;
    private boolean isFirstLoad = true;
    private int mOffset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_join_list, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpSwipeRefreshLayout();
        setUpRecyclerView();

    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mConvertView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query.setLimit(Constants.PAGE_SIZE);
        query.whereEqualTo(Constants.COLUMN_USER, user);
        query.include(Constants.COLUMN_IDEA);
        query.include(Constants.COLUMN_ASSOCIATION);
        query.include("idea.user");
        query.include(Constants.COLUMN_DEPARTMENT);
        query.include("department.studentUnion");
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.clear();
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt("joinId");
                    } else {
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void loadMoreData() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query.setLimit(Constants.PAGE_SIZE);
        query.whereEqualTo(Constants.COLUMN_USER, user);
        query.include(Constants.COLUMN_IDEA);
        query.include("idea.user");
        query.include(Constants.COLUMN_ASSOCIATION);
        query.include(Constants.COLUMN_DEPARTMENT);
        query.include("department.studentUnion");
        query.orderByDescending(Constants.COLUMN_CREATED_AT);
        query.whereLessThan(Constants.COLUMN_JOIN_ID, mOffset);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mOffset = list.get(list.size() - 1).getInt(Constants.COLUMN_JOIN_ID);
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mJoinRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_join_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mJoinRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new UserJoinListAdapter(getActivity(), mData);
        mJoinRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        mJoinRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && linearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        loadMoreData();

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSignIn()) {
            if (isFirstLoad) {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }
                });
                isFirstLoad = false;
            }
            if (mSignInPromptView != null) {
                mSignInPromptView.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mJoinRecyclerView.setVisibility(View.VISIBLE);


            }
        } else {
            if (mSignInPromptView == null) {
                ViewStub viewStub = (ViewStub) mConvertView.findViewById(R.id.id_vs_login_in_prompt);
                mSignInPromptView = viewStub.inflate();
                TextView tipsText = (TextView) mSignInPromptView.findViewById(R.id.tv_message);
                tipsText.setText("登录才能看到自己已经参与的哦");
                mSignInPromptView.findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        SignInActivity_.intent(UserJoinListFragment.this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                    }

                });
            }
            mJoinRecyclerView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSignInPromptView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int pos) {

        AVObject object = mData.get(pos);
        int type = object.getInt(Constants.COLUMN_TYPE);
        switch (type) {
            case Constants.TYPE_ASSOCIATION:
                AVObject association = object.getAVObject(Constants.COLUMN_ASSOCIATION);
                String stringObject = association.toString();
                String associationName = association.getString(Constants.COLUMN_ASSOCIATION_NAME);
                AssociationDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT, stringObject).mAssociationName(associationName).mAssociationId(association.getObjectId()).start();
                break;
            case Constants.TYPE_STUDENT_UNION_DEPARTMENT:
                AVObject department = object.getAVObject(Constants.COLUMN_DEPARTMENT);
                String departmentStringObject = department.toString();
                String departmentId = department.getObjectId();
                DepartmentDetailActivity_.intent(this)
                        .extra(DepartmentDetailActivity.EXTRA_KEY_DEPARTMENT_ID, departmentId)
                        .extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT, departmentStringObject)
//                .extra(Constants.EXTRA_KEY_COLLEGE,mCollegeName)
                        .extra(Constants.EXTRA_KEY_DEPARTMENT_NAME, department.getString("name"))
                        .start();
                break;
            default:
                AVObject idea = object.getAVObject(Constants.COLUMN_IDEA);
                String objectStr = idea.toString();
                //String title = mData.get(pos).getString(Constants.COLUMN_TITLE);
                IdeaDetailActivity_.intent(this).extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT, objectStr).start();
                break;
        }

    }

    @Override
    public void onItemBtnMemberClick(int pos) {
        AssociationMemberActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING,mData.get(pos).getAVObject(Constants.COLUMN_ASSOCIATION).toString()).start();
    }

    @Override
    public void onItemBtnRecentActivitiesClick(int pos) {

    }
}
