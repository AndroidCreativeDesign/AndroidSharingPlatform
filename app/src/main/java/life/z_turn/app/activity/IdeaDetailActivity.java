package life.z_turn.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

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

    @Extra(Constants.EXTRA_KEY_IDEA_STRING_OBJECT)
    String mIdeaObjectString;


    private int mOperationCode;

    private AVObject mIdea;

    private AVUser mUser;


    @ViewById(R.id.img_idea)
    ImageView mImgIdea;


    @ViewById(R.id.text_username)
    TextView mTextUsername;

    @ViewById(R.id.img_profile_photo)
    CircleImageView mImgProfilePhoto;

    @ViewById(R.id.scroll_view_container)
    NestedScrollView mScrollContainer;


    @ViewById(R.id.text_title)
    TextView mTextTitle;

    @ViewById(R.id.text_introduce)
    TextView mTextIntroduce;

    @ViewById(R.id.text_start_date)
    TextView mTextStartDate;


    @ViewById(R.id.text_end_date)
    TextView mTextEndDate;

    @ViewById(R.id.text_address)
    TextView mTextAddress;


    private boolean isCollected;

    @AfterExtras
    void initExtras() {

    }


    MenuItem mItemCollect;

    @AfterViews
    void init() {
        try {
            mIdea = AVObject.parseAVObject(mIdeaObjectString);
            updatePublisherInfo();
            updateCollectMenuItem();

        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(this, e);
        }

        initToolbar();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mIdea.getString("title"));
    }

    private void updatePublisherInfo() {
        AVUser user = mIdea.getAVUser("user");
        mTextUsername.setText(user.getUsername());
        Picasso.with(this).load(user.getString("profilePhotoUrl")).into(mImgProfilePhoto);
        mTextTitle.setText(mIdea.getString(Constants.COLUMN_TITLE));
//        mTextIntroduce.setText(mIdea.getString(Constants.COLUMN_INTRODUCE));
        mTextStartDate.setText(mIdea.getDate("startDate").toString());
        mTextEndDate.setText(mIdea.getDate("endDate").toString());
        mTextAddress.setText(mIdea.getString("address"));
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


    @Click(R.id.rlayout_publisher)
    void toUserDetail() {
        UserProfileActivity_.intent(IdeaDetailActivity.this).extra(Constants.EXTRA_KEY_USER_STRING_OBJECT, mIdea.getAVUser("user").toString()).start();

    }

    @Click(R.id.btn_comment)
    void comment() {
        CommentActivity_.intent(this).extra("ideaId", objectId).start();
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


                if (isSignIn() && !isCollected) {
                    collect();
                    showToast("收藏");
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
                    if (mItemCollect != null) {
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


    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        if (getSupportActionBar() != null) {
            // getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //  getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    private void updateCollectMenuItem() {
        if (isSignIn()) {
            AVQuery<AVObject> q = new AVQuery<>(Constants.TABLE_USER_COLLECT);
            q.whereEqualTo("user", AVUser.getCurrentUser());
            q.whereEqualTo("idea", mIdea);
            q.countInBackground(new CountCallback() {
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
