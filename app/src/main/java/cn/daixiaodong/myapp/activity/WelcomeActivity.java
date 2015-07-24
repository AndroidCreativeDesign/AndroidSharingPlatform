package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.os.Handler;

import com.avos.avoscloud.AVUser;

import org.androidannotations.annotations.EActivity;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

/**
 * 欢迎页
 */
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AVUser.getCurrentUser() != null) {
                    MainActivity_.intent(WelcomeActivity.this).start();
                    finish();
                } else {
                    SignInActivity_.intent(WelcomeActivity.this).start();
                    finish();
                }
            }
        }, 3000);

    }
}
