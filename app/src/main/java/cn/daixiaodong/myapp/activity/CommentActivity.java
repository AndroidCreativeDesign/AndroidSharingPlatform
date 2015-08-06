package cn.daixiaodong.myapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.adapter.CommentAdapter;
import cn.daixiaodong.myapp.view.MyItemDecoration;


@EActivity(R.layout.activity_comment)
public class CommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, CommentAdapter.OnCommentItemClickListener {


    @Extra("ideaId")
    String mIdeaId;

    @ViewById(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @ViewById(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @ViewById(R.id.edit_content)
    EditText mEditContent;


    private List<AVObject> mData;
    private CommentAdapter mAdapter;

    private boolean isReply;

    private AVUser mReplyToUser;


    @AfterViews
    void init() {
        setUpRefreshLayout();
        setUpRecyclerView();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                refreshData();
            }
        });

    }

    private void refreshData() {
        AVQuery<AVObject> query = new AVQuery<>("user_comment");
        query.whereEqualTo("ideaId", mIdeaId);
        query.orderByDescending("createdAt");
        query.include("user");
        query.include("replyToUser");
        query.setLimit(15);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mData.clear();
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new CommentAdapter(this, mData);
        mAdapter.setOnCommentItemClickListener(this);
        mRecyclerView.addItemDecoration(new MyItemDecoration(getResources().getDrawable(R.drawable.item_shape)));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Click(R.id.ibtn_send)
    void send() {
        final String content = mEditContent.getText().toString();

        final AVObject commentModel = new AVObject("user_comment");
        if (mReplyToUser != null) {
            if (content.startsWith("回复 " + mReplyToUser.getUsername() + "：")) {
                commentModel.put("replyToUser", mReplyToUser);
                String replace = content.replace("回复 " + mReplyToUser.getUsername() + "：", "");
                commentModel.put("content", replace);
                isReply = true;
            } else {

                commentModel.put("content", content);
                isReply = false;
            }
        } else {
            commentModel.put("content", content);
            isReply = false;
        }
        commentModel.put("user", AVUser.getCurrentUser());
        commentModel.put("ideaId", mIdeaId);


        commentModel.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mData.add(commentModel);
                    mAdapter.notifyItemInserted(0);
                    showToast("评论成功");
                    mEditContent.setText("");
                    InputMethodManager inputMethodManager = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEditContent.getWindowToken(), 0);
                    mEditContent.clearFocus();

                    if (isReply) {
                        AVObject object = new AVObject("user_received_reply");
                        object.put("user", mReplyToUser);
                        object.put("content", content);
                        object.put("send", AVUser.getCurrentUser());
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    // 假设 THE_INSTALLATION_ID 是保存在用户表里的 installationId，
                                    // 可以在应用启动的时候获取并保存到用户表
                                    pushQuery.whereEqualTo("installationId", mReplyToUser.getString("installationId"));
                                    AVPush.sendMessageInBackground(content, pushQuery, new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                showToast("发送成功");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshData();

    }


    @Override
    public void onCommentItemClick(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onCommentItemProfilePhotoClick(RecyclerView.ViewHolder viewHolder, int position) {
        toUserProfile(position);
    }


    @Override
    public void onCommentItemUsernameClick(RecyclerView.ViewHolder viewHolder, int position) {
        toUserProfile(position);
    }

    @Override
    public void onCommentItemReplyClick(RecyclerView.ViewHolder viewHolder, int position) {
        AVObject object = mData.get(position);
        AVUser user = object.getAVUser("user");
        mReplyToUser = user;
        mEditContent.setText("回复 " + user.getUsername() + "：");
        Editable text = mEditContent.getText();
        if (text != null) {
            mReplyToUser = user;
            Selection.setSelection(text, text.length());
            mEditContent.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mEditContent, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void onCommentItemUpvoteClick(RecyclerView.ViewHolder viewHolder, int position) {
        AVObject comment = mData.get(position);
        comment.increment("upvotes");
        comment.saveInBackground();
    }

    @Override
    public void onCommentItemMoreClick(RecyclerView.ViewHolder viewHolder, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.comment_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AVObject object = mData.get(position);
                AVUser user = object.getAVUser("user");
                switch (which) {
                    case 0:
                        AVObject object1 = new AVObject("user_report");
                        if (AVUser.getCurrentUser() != null) {
                            object1.put("user", AVUser.getCurrentUser());
                        }
                        object1.put("reported", user);
                        object1.put("comment", object.getObjectId());
                        object1.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    showToast("举报成功");
                                }
                            }
                        });
                        break;
                }
            }
        });
        builder.show();
    }

    private void toUserProfile(int position) {
        AVObject object = mData.get(position);
        AVUser user = object.getAVUser("user");
        if (AVUser.getCurrentUser() != user) {
            UserProfileActivity_.intent(CommentActivity.this).extra("userId", user.getObjectId()).start();
        }
    }


}

