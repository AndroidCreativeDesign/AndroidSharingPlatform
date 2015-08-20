package life.z_turn.app.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.SharedPreferencesUtil;
import life.z_turn.app.utils.TextUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * 登录界面
 */
@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity {

    public final static int SIGN_IN_REQUEST_CODE = 1111;
    public final static int SIGN_IN_SUCCESS_RESULT_CODE = 1112;


    @ViewById(R.id.toolbar)
    Toolbar mViewToolbar;


    @ViewById(R.id.edit_account_number)
    EditText mEditAccountNumber;

    @ViewById(R.id.id_et_password)
    EditText mViewPassword;

    @ViewById(R.id.id_btn_sign_in)
    Button mViewSignIn;

    @ViewById(R.id.id_btn_sign_up_now)
    Button mViewSignUp;


    @ViewById(R.id.id_tv_forgot_password)
    TextView mViewForgotPassword;


    @Click(R.id.id_btn_sign_up_now)
    void signUp() {
        SignUpFirstStepNewActivity_.intent(this).startForResult(Constants.REQUEST_LOGIN_UP_BY_PHONE);
    }

    @AfterViews
    void init() {
        initToolbar();
    }

    @Click(R.id.id_btn_sign_in)
    void signIn() {

        String accountNumber = mEditAccountNumber.getText().toString();
        String password = mViewPassword.getText().toString();

        if (accountNumber.trim().isEmpty()) {
            showToast("请输入手机号或用户名");
            return;
        }
        if (password.trim().isEmpty()) {
            showToast(R.string.pls_enter_password);
            return;
        }
        switch (getAccountNumberType(accountNumber)) {
            case -1:
                showToast("请输入正确的手机号或邮箱账号");
                return;
            case 0:
                signInByPhoneNumber(accountNumber, password);
                break;
            case 1:
                signInByUsername(accountNumber, password);
                break;
        }
    }

    private void signInByUsername(String username, String password) {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){

                    saveInstallationId();

                    setResultAndFinish();
                }else{
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(SignInActivity.this, e);
                }
            }
        });
    }


    /**
     * 判断账号类型
     *
     * @param accountNumber 账号
     * @return 0:手机号码 1:用户名
     */
    private int getAccountNumberType(String accountNumber) {
        if (TextUtil.isMobilePhoneNum(accountNumber)) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * 跳转到重置密码界面
     */
    @Click(R.id.id_tv_forgot_password)
    void forgotPassword() {
        PasswordResetActivity_.intent(this).start();
    }


    /**
     * 通过手机号码登录
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    private void signInByPhoneNumber(String phoneNumber, String password) {
        AVUser.loginByMobilePhoneNumberInBackground(phoneNumber, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                showProgressDialog(false);
                if (e == null) {
                    showToast(R.string.success_sign_in);

                    saveInstallationId();
                    saveUserInfo2File();

                    // 设置返回码、发送登录成功的广播
                    setResultAndFinish();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(SignInActivity.this, e);
                }
            }
        });
    }

    private void saveUserInfo2File() {
        SharedPreferencesUtil.saveUserInfo(this);
    }

    /**
     * 将InstallationId关联到登录用户
     */
    private void saveInstallationId() {
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        AVUser.getCurrentUser().put("installationId", installationId);
        AVUser.getCurrentUser().saveInBackground();

    }


    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle(R.string.sign_in);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN_UP_BY_PHONE && resultCode == RESULT_OK) {
            showToast(R.string.success_sign_in);
            saveInstallationId();
            setResultAndFinish();
        }
    }

    /**
     * 设置返回结果，并发送登录广播，销毁activity实例
     */
    private void setResultAndFinish() {
        SignInActivity.this.setResult(SIGN_IN_SUCCESS_RESULT_CODE, getIntent());
        Intent intent = new Intent(Constants.ACTION_USER_SIGN_IN);
        sendBroadcast(intent);
        finish();
    }
}
