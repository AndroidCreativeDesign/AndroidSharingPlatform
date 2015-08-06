package cn.daixiaodong.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.config.Constants;


@EActivity(R.layout.activity_sign_up_first_step_new)
public class SignUpFirstStepNewActivity extends BaseActivity {


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.edit_mobile_phone_number)
    EditText mEditMobilePhone;

    @ViewById(R.id.edit_verify_code)
    EditText mEditSecurityCode;

    @ViewById(R.id.btn_get_verify_code)
    Button mBtnGetVerifyCode;


    private String mMobilePhoneNumber;
    private TimeCount mTimeCount;

    @AfterViews
    void initViews() {
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
        //  正则表达式判断
        Pattern pattern = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(mMobilePhoneNumber);
        if (!matcher.matches()) {
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
                                        }
                                    }
                                });
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });


    }


    @Click(R.id.btn_next_step)
    void nextStep() {

        // 提示
        /* ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();*/
        mMobilePhoneNumber = "18079609756";
        SignUpSecondStepNewActivity_.intent(SignUpFirstStepNewActivity.this).extra("mobilePhoneNumber", mMobilePhoneNumber).startForResult(Constants.REQUEST_EDIT_USER_INFO);

       /* String securityCode = mEditSecurityCode.getText().toString();

        AVOSCloud.verifyCodeInBackground(securityCode, mMobilePhoneNumber,
                new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showToast("验证通过");
                            SignUpSecondStepNewActivity_.intent(SignUpFirstStepNewActivity.this).start();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });*/

    }


    @Click(R.id.btn_sign_in_by_email)
    void signInByEmail() {
        Intent intent = new Intent(this, SignInByEmailActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("获取验证码");
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
        if(requestCode == Constants.REQUEST_EDIT_USER_INFO && resultCode == RESULT_OK){
            this.setResult(RESULT_OK);
            finish();
        }
    }
}
