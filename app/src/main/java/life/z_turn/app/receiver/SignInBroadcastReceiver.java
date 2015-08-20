package life.z_turn.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by daixiaodong on 15/7/25.
 */
public class SignInBroadcastReceiver extends BroadcastReceiver {

    private OnSignInStatusChangeListener mListener;

    public interface OnSignInStatusChangeListener {
        void onSignInStatusChangeListener(boolean isSignIn);
    }

    public void setOnSignInStatusChangeListener(OnSignInStatusChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isSignIn = intent.getBooleanExtra("isSignIn", true);
        if (mListener != null) {
            mListener.onSignInStatusChangeListener(isSignIn);
        }
    }
}
