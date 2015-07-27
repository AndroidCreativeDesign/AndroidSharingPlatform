package cn.daixiaodong.myapp.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 *  密码重置界面
 */
@EActivity(R.layout.activity_password_reset)
public class PasswordResetActivity extends BaseActivity {
    @ViewById(R.id.id_et_phone_number)
    EditText mViewPhoneNumber;

    @ViewById(R.id.id_et_new_password)
    EditText mViewNewPassword;

    @ViewById(R.id.id_et_verify_code)
    EditText mViewVerifyCode;

    @ViewById(R.id.id_btn_password_reset)
    Button mViewPasswordReset;

    @ViewById(R.id.id_btn_verify_code)
    Button mViewGetVerifyCode;

    @Click(R.id.id_btn_password_reset)
    void signUp() {

        String phoneNumber = mViewPhoneNumber.getText().toString();
        String newPassword = mViewNewPassword.getText().toString();
        String verifyCode = mViewVerifyCode.getText().toString();
        if (!checkData(phoneNumber, newPassword, verifyCode)) {
            return;
        }
        AVUser.resetPasswordBySmsCodeInBackground(verifyCode, newPassword, new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    showToast("密码重置成功,返回登录");
                    finish();
                }else{
                    e.printStackTrace();
                }
            }
        });
    }


    @Click(R.id.id_btn_verify_code)
    void getVerifyCode() {
        String phoneNumber = mViewPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return;
        }
        if (phoneNumber.length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }
        AVUser.requestPasswordResetBySmsCodeInBackground(phoneNumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    showToast("验证码正在发送中");
                }else{
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 校验用户输入
     *
     * @param phoneNumber 手机号
     * @param newPassword    密码
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

}
