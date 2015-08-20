package life.z_turn.app.activity.common;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

import life.z_turn.app.R;

/**
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity {

    protected AlertDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtil.i("BaseActivity", this.getClass().getSimpleName() + "add");
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

    public void showToast(@StringRes int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();

    }

    public void showProgressDialog(boolean showing) {
        if (showing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog_progress);
            builder.setCancelable(false);
            mProgressDialog = builder.create();
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

    }

    public boolean isSignIn() {
        if (AVUser.getCurrentUser() != null) {
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LogUtil.i("BaseActivity", this.getClass().getSimpleName() + "remove");
        ActivityCollector.removeActivity(this.getClass().getSimpleName(), this);

        ActivityCollector.removeActivity(this);
    }
}
