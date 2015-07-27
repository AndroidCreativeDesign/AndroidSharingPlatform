package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

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
 *  协会详情界面
 */
@EActivity
public class AssociationDetailActivity extends BaseActivity {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mToolbar;


    @ViewById(R.id.id_btn_join)
    Button mBtnJoin;

    @Extra
    String mAssociationId;

    @Extra
    String mAssociationName;


    private AVObject mAssociation;

    @AfterExtras
    void initData() {

    }

    @AfterViews
    void init() {
        setUpToolbar();
        mToolbar.setTitle(mAssociationName);
        loadData();
    }

    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>("association");
        query.whereEqualTo("objectId", mAssociationId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mAssociation = list.get(0);
                    showToast("数据加载完毕");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click(R.id.id_btn_join)
    void join() {
        AVQuery<AVObject> query = new AVQuery<>("user_association");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.whereEqualTo("association", mAssociation);

        // 这里可以用 count
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        showToast("你已经加入了该协会，请勿重复报名");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssociationDetailActivity.this);
                        builder.setMessage("你已经加入了该协会，请勿重复报名");
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    } else {
                        RegistrationInformationActivity_.intent(AssociationDetailActivity.this).start();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);

    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //mToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_association, menu);
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
