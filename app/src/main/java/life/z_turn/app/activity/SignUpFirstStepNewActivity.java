package life.z_turn.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.TextUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.NoUnderlineSpan;


@EActivity(R.layout.activity_sign_up_first_step_new)
public class SignUpFirstStepNewActivity extends BaseActivity {


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.edit_account_number)
    EditText mEditMobilePhone;

    @ViewById(R.id.edit_verify_code)
    EditText mEditSecurityCode;

    @ViewById(R.id.btn_get_verify_code)
    Button mBtnGetVerifyCode;

    @ViewById(R.id.chk_agree)
    CheckBox mChkAgree;

    @ViewById(R.id.text_term_of_service)
    TextView mTextTermOfService;


    private String mMobilePhoneNumber;
    private TimeCount mTimeCount;

    @AfterViews
    void initViews() {
        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();

        initToolbar();
        mTimeCount = new TimeCount(60000, 1000);
    }


    @Click(R.id.btn_get_verify_code)
    void getSecurityCode() {
        mMobilePhoneNumber = mEditMobilePhone.getText().toString();


        if (mMobilePhoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return;
        }

        if (!TextUtil.isMobilePhoneNum(mMobilePhoneNumber)) {
            showToast("请输入正确的手机号码");
            return;
        }


        //  查询该号码是否已注册
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("mobilePhoneNumber", mMobilePhoneNumber);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    if (i > 0) {
                        showToast("该手机号已注册");
                    } else {
                        AVOSCloud.requestSMSCodeInBackground(mMobilePhoneNumber, null, "短信验证", 5,
                                new RequestMobileCodeCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            //发送成功
                                            mTimeCount.start();
                                            showToast("短信已发送");
                                        } else {
                                            ExceptionUtil.printStackTrace(e);
                                            ToastUtil.showErrorMessageToastByException(SignUpFirstStepNewActivity.this, e);
                                        }
                                    }


                                });
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(SignUpFirstStepNewActivity.this, e);
                }
            }
        });


    }


    @Click(R.id.btn_next_step)
    void nextStep() {
        mMobilePhoneNumber = mEditMobilePhone.getText().toString();
        if (mMobilePhoneNumber.trim().isEmpty()) {
            showToast(R.string.pls_enter_mobile_phone_number);
            return;
        }
        if (!TextUtil.isMobilePhoneNum(mMobilePhoneNumber)) {
            showToast("请输入正确的手机号码");
            return;
        }

        String securityCode = mEditSecurityCode.getText().toString();
        if (securityCode.trim().isEmpty()) {
            showToast("请输入验证码");
            return;
        }

        if (!mChkAgree.isChecked()) {
            showToast(R.string.pls_read_trem_of_service);
            return;
        }


        showProgressDialog(true);

        AVOSCloud.verifyCodeInBackground(securityCode, mMobilePhoneNumber,
                new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        showProgressDialog(false);
                        if (e == null) {
                            SignUpSecondStepNewActivity_.intent(SignUpFirstStepNewActivity.this).startForResult(Constants.REQUEST_EDIT_USER_INFO);
                        } else {
                            ExceptionUtil.printStackTrace(e);
                            ToastUtil.showErrorMessageToastByException(SignUpFirstStepNewActivity.this, e);
                        }
                    }
                });

    }


    @Click(R.id.btn_sign_in_by_email)
    void signInByEmail() {
        Intent intent = new Intent(this, SignUpByEmailActivity.class);
        startActivityForResult(intent, Constants.REQUEST_SIGN_UP_BY_EMAIL);
    }


    @Click(R.id.text_term_of_service)
    void readTermOfService(){
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.API_USE_AGREEMENT));
//        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(it);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.sign_up_by_mobile_phone_number);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_first_step_new, menu);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_EDIT_USER_INFO && resultCode == RESULT_OK) {
            this.setResult(RESULT_OK);
            finish();
        }
        if (requestCode == Constants.REQUEST_SIGN_UP_BY_EMAIL && resultCode == RESULT_OK) {
            this.setResult(RESULT_OK);
            finish();
        }
    }
}
