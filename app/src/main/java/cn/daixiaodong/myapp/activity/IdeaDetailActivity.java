package cn.daixiaodong.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.config.Constants;

/**
 * idea详情页
 */
@EActivity(R.layout.activity_idea_detail)
public class IdeaDetailActivity extends BaseActivity {


    @ViewById(R.id.toolbar)
    Toolbar mViewToolbar;


    @ViewById(R.id.id_btn_join)
    Button mBtnJoin;
/*
    @ViewById(R.id.id_btn_follow)
    Button mFollowBtn;*/


    @Extra
    String objectId;

    @Extra
    String title;


    private int mOperationCode;

    private AVObject mIdea;

    private AVUser mUser;


    @ViewById(R.id.img_idea)
    ImageView mImgIdea;


    @ViewById(R.id.rlayout_progress_load)
    RelativeLayout mLayoutProgressLoad;

    @ViewById(R.id.scroll_view_container)
    NestedScrollView mScrollContainer;

    private boolean isCollected;

    @AfterExtras
    void initExtras() {

    }


    MenuItem mItemCollect;

    @AfterViews
    void init() {
        initToolbar();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);
        loadData();
    }


    /*@Click(R.id.id_btn_follow)
    void onFollow() {
        if (isSignIn()) {
            follow();
        } else {
            mOperationCode = 1;
            SignInActivity_.intent(this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
        }

    }
*/
    private void follow() {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<AVObject> relation = user.getRelation("follow");
        relation.add(mIdea.getAVUser("user"));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("关注成功");
                }
            }
        });
    }

    @Click(R.id.btn_comment)
    void comment(){
        CommentActivity_.intent(this).extra("ideaId",objectId).start();
    }



    @Click(R.id.id_btn_join)
    void onJoin() {
        if (isSignIn()) {
            join();
        } else {
            mOperationCode = 2;
            SignInActivity_.intent(this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
        }

    }

    private void join() {
        AVUser user = AVUser.getCurrentUser();
        final AVObject object = new AVObject(Constants.TABLE_USER_JOIN);
        object.put("user", user);
        object.put("idea", mIdea);
        object.put("type", mIdea.get("type"));
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("保存成功");
                    mIdea.increment("joinNum");
                    mIdea.saveInBackground();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_idea_detail, menu);

        mItemCollect = menu.findItem(R.id.action_collect);
        if (isSignIn()) {
            mItemCollect.setVisible(false);
        } else {
            mItemCollect.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_collect:
                showToast("收藏");

                if (isSignIn() && !isCollected) {
                    collect();
                } else if (isSignIn()) {
                    cancelCollect();
                } else {
                    mOperationCode = 0;
                    SignInActivity_.intent(this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
                }

                break;

            case R.id.action_share:
                showToast("分享");
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelCollect() {
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_USER_COLLECT);
        query.whereEqualTo("user", user);
        query.whereEqualTo("idea", mIdea);
        query.deleteAllInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("已取消收藏");
                    if (mItemCollect != null) {
                        mItemCollect.setIcon(R.drawable.ic_star_border_white_48dp);
                        isCollected = false;
                    }
                }
            }
        });
    }

    private void collect() {
        AVUser user = AVUser.getCurrentUser();
        final AVObject collect = new AVObject(Constants.TABLE_USER_COLLECT);
        collect.put("user", user);
        collect.put("idea", mIdea);
        collect.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("收藏成功");
                    mIdea.increment("collectNum");
                    mIdea.saveInBackground();
                    isCollected = true;
                    if(mItemCollect!=null){
                        mItemCollect.setIcon(R.drawable.ic_star_white_48dp);
                    }
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        if (getSupportActionBar() != null) {
            // getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //  getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void loadData() {
        final AVQuery<AVObject> query = new AVQuery<>("idea");
        query.whereEqualTo("objectId", objectId);
        query.include("user");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 1) {
                        mIdea = list.get(0);
                        showToast("数据加载完毕");
                        Picasso.with(IdeaDetailActivity.this).load(mIdea.getString("imgUrl")).into(mImgIdea);
                        mLayoutProgressLoad.setVisibility(View.GONE);
                        mScrollContainer.setVisibility(View.VISIBLE);
                        if (isSignIn()) {
                            AVQuery<AVObject> query1 = new AVQuery<>(Constants.TABLE_USER_COLLECT);
                            query1.whereEqualTo("user", AVUser.getCurrentUser());
                            query1.whereEqualTo("idea", mIdea);
                            query1.countInBackground(new CountCallback() {
                                @Override
                                public void done(int i, AVException e) {
                                    if (i > 0) {
                                        isCollected = true;
                                        if (mItemCollect != null) {
                                            mItemCollect.setVisible(true);
                                            mItemCollect.setIcon(R.drawable.ic_star_white_48dp);

                                        }
                                    } else {
                                        if (mItemCollect != null) {
                                            mItemCollect.setVisible(true);
                                        }
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE &&
                resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            mUser = AVUser.getCurrentUser();
            switch (mOperationCode) {
                case 0:
                    collect();
                    break;
                case 1:
                    follow();
                    break;
                case 2:
                    join();
                    break;
            }
        }
    }
}
