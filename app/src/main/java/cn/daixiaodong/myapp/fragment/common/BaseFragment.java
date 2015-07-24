package cn.daixiaodong.myapp.fragment.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;


public class BaseFragment extends Fragment {


    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
        Log.i("onPause", this.getClass().getSimpleName());
        /*AVAnalytics.onFragmentEnd("my-list-fragment");*/
    }

    public void onResume() {
        super.onResume();
        Log.i("onResume", this.getClass().getSimpleName());

       /* AVAnalytics.onFragmentStart("my-list-fragment");*/
    }

    protected boolean isSignIn() {
        if (AVUser.getCurrentUser() == null) {
            return false;
        }
        return true;
    }
}
