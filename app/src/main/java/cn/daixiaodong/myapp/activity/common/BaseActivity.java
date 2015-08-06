package cn.daixiaodong.myapp.activity.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

/**
 *  Activity 基类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseActivity", this.getClass().getSimpleName() + "add");
        ActivityCollector.addActivity(this);
        ActivityCollector.addActivity(this.getClass().getSimpleName(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    public boolean isSignIn(){
        if(AVUser.getCurrentUser()!=null){
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("BaseActivity", this.getClass().getSimpleName() + "remove");
        ActivityCollector.removeActivity(this.getClass().getSimpleName(), this);

        ActivityCollector.removeActivity(this);
    }
}
