package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.os.Handler;

import com.avos.avoscloud.AVUser;

import org.androidannotations.annotations.EActivity;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

/**
 *  闪屏页
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AVUser.getCurrentUser() != null) {
                    MainActivity_.intent(SplashActivity.this).start();
                    finish();
                } else {
                    SignInActivity_.intent(SplashActivity.this).start();
                    finish();
                }
            }
        }, 3000);

    }
}
