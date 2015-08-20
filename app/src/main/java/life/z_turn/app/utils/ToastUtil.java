package life.z_turn.app.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.avos.avoscloud.AVException;

import life.z_turn.app.R;


public class ToastUtil {
    public static void showErrorMessageToastByException(Context context, Exception e) {
        if (e instanceof AVException) {
            switch (((AVException) e).getCode()) {
                case 0:
                    showToast(context,R.string.network_error);
                    break;
                case AVException.USERNAME_PASSWORD_MISMATCH:
                    showToast(context, R.string.username_password_mismatch);
                    break;
                case AVException.USER_DOESNOT_EXIST:
                    showToast(context, R.string.user_does_not_exist);
                    break;
                case AVException.OBJECT_NOT_FOUND:
                    showToast(context, R.string.object_not_found);
                    break;
                case AVException.EMAIL_NOT_FOUND:
                    showToast(context, R.string.email_not_found);
                    break;
                case AVException.USER_WITH_MOBILEPHONE_NOT_FOUND:
                    showToast(context,R.string.user_with_mobilephone_not_found);
                    break;
                case 601:
                    showToast(context, R.string.get_verify_code_busy);
                    break;

                case 603:
                    showToast(context, R.string.invalid_verify_code);
                    break;
                default:
                    showToast(context, R.string.unknown_error);
                    break;
            }
        }
    }


    public static void showToast(Context context, @StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
