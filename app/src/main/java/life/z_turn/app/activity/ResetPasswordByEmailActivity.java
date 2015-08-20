package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;


@EActivity
public class ResetPasswordByEmailActivity extends BaseActivity {


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @ViewById(R.id.edit_email)
    EditText mEditEmail;

    @ViewById(R.id.btn_reset)
    Button mBtnReset;

    @AfterViews
    void init(){
        initToolbar();
    }

    @Click(R.id.btn_reset)
    void reset() {
        String email = mEditEmail.getText().toString();
        if (email.trim().isEmpty()) {
            showToast(R.string.pls_enter_email);
            return;
        }
        AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    showToast("请登录您的邮箱，进行密码重置");
                }else{
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(ResetPasswordByEmailActivity.this,e);
                }
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("重置密码");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_by_email);
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

        return super.onOptionsItemSelected(item);
    }
}
