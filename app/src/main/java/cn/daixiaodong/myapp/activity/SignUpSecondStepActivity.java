package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.activity.common.ActivityCollector;


/**
 * 验证验证码，完善注册信息页面
 */
@EActivity(R.layout.activity_sign_up_second_step)
public class SignUpSecondStepActivity extends BaseActivity {


    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;

    @ViewById(R.id.id_et_sign_up_second_step_verify_code)
    EditText mViewVerifyCode;


    @ViewById(R.id.id_btn_sign_up_second_step_get_verify_code)
    Button mViewGetVerifyCode;


    @Click(R.id.id_btn_sign_up_second_step_get_verify_code)
    void getVerifyCode() {
        //如果你的账号需要重新发送短信请参考下面的代码
        AVUser.requestMobilePhoneVerifyInBackground("18370661127", new RequestMobileCodeCallback() {

            @Override
            public void done(AVException e) {
                //发送了验证码以后做点什么呢
                if (e == null) {
                    showToast("发送成功");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @ViewById(R.id.id_btn_sign_up_second_step_done)
    Button mViewDone;

    @Click(R.id.id_btn_sign_up_second_step_done)
    void done() {
        String verifyCode = mViewVerifyCode.getText().toString();
        if (verifyCode.isEmpty()) {
            showToast("请输入验证码");
            return;
        }
        AVUser.verifyMobilePhoneInBackground(verifyCode, new AVMobilePhoneVerifyCallback() {

            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("验证成功");
                    MainActivity_.intent(SignUpSecondStepActivity.this).start();
                    ActivityCollector.finishActivity(SignUpFirstStepActivity_.class.getSimpleName());
                    finish();
                } else {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitleTextColor(Color.WHITE);
        mViewToolbar.setTitle("完善信息");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
