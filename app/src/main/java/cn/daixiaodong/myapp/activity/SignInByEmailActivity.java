package cn.daixiaodong.myapp.activity;

import android.app.Fragment;
import android.os.Bundle;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.fragment.SignUpByEmailFragment;

public class SignInByEmailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_by_email);
        Fragment fragment = new SignUpByEmailFragment();
        getFragmentManager().beginTransaction().replace(R.id.flayout_container, fragment).commit();
    }
}
