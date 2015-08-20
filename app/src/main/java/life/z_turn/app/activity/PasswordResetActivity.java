package life.z_turn.app.activity;

import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.TextUtil;
import life.z_turn.app.utils.ToastUtil;


/**
 * 密码重置界面
 */
@EActivity(R.layout.activity_password_reset)
public class PasswordResetActivity extends BaseActivity {

    private TimeCount mTimeCount;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.edit_account_number)
    EditText mViewPhoneNumber;

    @ViewById(R.id.edit_password)
    EditText mViewNewPassword;

    @ViewById(R.id.edit_verify_code)
    EditText mEditVerifyCode;

    @ViewById(R.id.btn_reset)
    Button mViewPasswordReset;

    @ViewById(R.id.btn_get_verify_code)
    Button mBtnGetVerifyCode;


    @AfterViews
    void init() {
        initToolbar();
        mTimeCount = new TimeCount(60000, 1000);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("重置密码");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Click(R.id.btn_reset_password_by_email)
    void resetPasswordByEmail() {
        ResetPasswordByEmailActivity_.intent(this).start();
    }

    @Click(R.id.btn_reset)
    void reset() {

        String newPassword = mViewNewPassword.getText().toString();
        String verifyCode = mEditVerifyCode.getText().toString();

        AVUser.resetPasswordBySmsCodeInBackground(verifyCode, newPassword, new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("密码重置成功,返回登录");
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    @Click(R.id.btn_get_verify_code)
    void getVerifyCode() {
        String mobilePhoneNumber = mViewPhoneNumber.getText().toString();
        if (mobilePhoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return;
        }
        if (!TextUtil.isMobilePhoneNum(mobilePhoneNumber)) {
            showToast("请输入正确的手机号码");
            return;
        }

        AVUser.requestPasswordResetBySmsCodeInBackground(mobilePhoneNumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("验证码正在发送中");
                    mTimeCount.start();

                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(PasswordResetActivity.this, e);
                }
            }
        });
    }


    /**
     * 校验用户输入
     *
     * @param phoneNumber 手机号
     * @param newPassword 密码
     * @param verifyCode  验证码
     */
    private boolean checkData(String phoneNumber, String newPassword, String verifyCode) {
        if (phoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return false;
        }
        if (phoneNumber.length() != 11) {
            showToast("请输入正确的手机号码");
            return false;
        }
        if (verifyCode.isEmpty()) {
            showToast("请输入验证码");
            return false;
        }
        if (newPassword.isEmpty()) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.length() < 8 || newPassword.length() > 15) {
            showToast("密码长度在8-15位");
            return false;
        }
        return true;
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            mBtnGetVerifyCode.setText("获取激活码");
            mBtnGetVerifyCode.setClickable(true);
        }

        public void onTick(long millisUntilFinished) {
            mBtnGetVerifyCode.setClickable(false);
            mBtnGetVerifyCode.setText(millisUntilFinished / 1000 + "秒后");
        }
    }

}
