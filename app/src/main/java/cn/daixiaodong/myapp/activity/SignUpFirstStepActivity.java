package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

/**
 * 用户通过手机号码注册界面
 */
@EActivity(R.layout.activity_sign_up_first_step)
public class SignUpFirstStepActivity extends BaseActivity {


    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;

    @ViewById(R.id.id_et_phone_number)
    EditText mViewPhoneNumber;

    @ViewById(R.id.id_et_sign_up_first_step_password)
    EditText mViewPassword;


    @ViewById(R.id.id_btn_sign_in_first_step_next_step)
    Button mViewNextStep;


    @Click(R.id.id_btn_sign_in_first_step_next_step)
    void signUp() {
        String phoneNumber = mViewPhoneNumber.getText().toString();
        String password = mViewPassword.getText().toString();
        if (!checkData(phoneNumber, password)) {
            // 校验不通过
            return;
        }
        signUpByPhoneNumber(phoneNumber, password);
    }

    /**
     * 通过手机号注册
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    private void signUpByPhoneNumber(String phoneNumber, String password) {

        AVUser user = new AVUser();
        user.setUsername(phoneNumber); // 暂时设置用户名为手机号码
        user.setPassword(password);
        user.setMobilePhoneNumber(phoneNumber);
        Log.i("tag", phoneNumber);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册通过，等待验证
                    SignUpSecondStepActivity_.intent(SignUpFirstStepActivity.this).start();
                } else {
                    e.printStackTrace();
                    showToast("该手机号已注册");
                }
            }
        });
    }


    /**
     * 校验用户输入
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    private boolean checkData(String phoneNumber, String password) {
        if (phoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return false;
        }
        if (phoneNumber.length() != 11) {
            showToast("请输入正确的手机号码");
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8 || password.length() > 15) {
            showToast("密码长度在8-15位");
            return false;
        }
        return true;
    }


    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitleTextColor(Color.WHITE);
        mViewToolbar.setTitle("注册");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
