package life.z_turn.app.fragment.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import life.z_turn.app.R;


public class BaseFragment extends Fragment {


    private AlertDialog mProgressDialog;

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(Context context,boolean showing) {
        if (showing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(R.layout.dialog_progress);
            mProgressDialog = builder.create();
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onPause() {
        super.onPause();
//        LogUtil.i("onPause", this.getClass().getSimpleName());
        /*AVAnalytics.onFragmentEnd("my-list-fragment");*/
    }

    public void onResume() {
        super.onResume();
//        LogUtil.i("onResume", this.getClass().getSimpleName());

       /* AVAnalytics.onFragmentStart("my-list-fragment");*/
    }

    protected boolean isSignIn() {
        if (AVUser.getCurrentUser() == null) {
            return false;
        }
        return true;
    }
}
