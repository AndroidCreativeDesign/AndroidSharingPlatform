package cn.daixiaodong.myapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 * 用户个人资料 界面
 */
@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivity {

    private  String mUserId;
    private AVUser mUser;


    @ViewById(R.id.btn_user_join)
    Button mBtnUserJoin;
    @ViewById(R.id.btn_user_publish)
    Button mBtnUserPublish;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @Click(R.id.btn_user_join)
    void viewUserJoin() {
        UserJoinActivity_.intent(this).extra("userId", "55ba35d3e4b0c170a520b83f").start();
    }

    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("个人资料");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String scheme = intent.getScheme();
        Uri uri = intent.getData();


        if(uri != null){
            mUserId = uri.getQueryParameter("userId");

        }else{
            mUserId  = intent.getStringExtra("userId");
        }
        Log.i("userId",mUserId);

        loadData();


    }

    private void loadData() {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("objectId",mUserId);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e == null){
                    if(list.size() == 1){
                        mUser = list.get(0);
                        showToast("数据加载完毕");
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_edit:

                showToast("编辑");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
