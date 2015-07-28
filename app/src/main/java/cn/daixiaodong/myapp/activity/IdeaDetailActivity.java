package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

/**
 * idea详情页
 */
@EActivity(R.layout.activity_idea_detail)
public class IdeaDetailActivity extends BaseActivity {


    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    @ViewById(R.id.id_btn_join)
    Button mBtnJoin;

    @ViewById(R.id.id_btn_follow)
    Button mFollowBtn;


    @Extra
    String objectId;

    @Extra
    String title;


    private AVObject mIdea;


    @AfterExtras
    void initExtras() {

    }

    @AfterViews
    void init() {
        initToolbar();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);
        loadData();
    }


    @Click(R.id.id_btn_follow)
    void follow() {
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


    @Click(R.id.id_btn_join)
    void join() {


        AVUser user = AVUser.getCurrentUser();
        final AVObject object = new AVObject("user_join");
        object.put("user", user);
        object.put("idea", mIdea);
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
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_collect:
                showToast("收藏");
                AVUser user = AVUser.getCurrentUser();
                final AVObject collect = new AVObject("user_collect");
                collect.put("user", user);
                collect.put("idea", mIdea);
                collect.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showToast("收藏成功");
                            mIdea.increment("collectNum");
                            mIdea.saveInBackground();
                        }
                    }
                });
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

   /* @ViewById(R.id.id_btn_pay)
    Button mBtnPay;


    @Click(R.id.id_btn_pay)
    void pay() {
        new BmobPay(this).pay(0.02, "某商品", new PayListener() {
            @Override
            public void orderId(String s) {

            }

            @Override
            public void succeed() {

            }

            @Override
            public void fail(int i, String s) {

            }

            @Override
            public void unknow() {

            }
        });
    }*/


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
        AVQuery<AVObject> query = new AVQuery<AVObject>("idea");
        query.whereEqualTo("objectId", objectId);
        query.include("user");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 1) {
                        mIdea = list.get(0);
                        showToast("数据加载完毕");
                    }
                }
            }
        });
    }


}
