package cn.daixiaodong.myapp.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 * 用户个人资料 界面
 */
@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivity {


    @ViewById(R.id.btn_user_join)
    Button mBtnUserJoin;
    @ViewById(R.id.btn_user_publish)
    Button mBtnUserPublish;

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mToolbar;

    @Click(R.id.btn_user_join)
    void viewUserJoin() {
        UserJoinActivity_.intent(this).extra("userId", "55ba35d3e4b0c170a520b83f").start();
    }


    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle("个人资料");
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

        switch (item.getItemId()){
            case R.id.action_edit:

                showToast("编辑");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
