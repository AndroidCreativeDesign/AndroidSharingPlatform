package life.z_turn.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import life.z_turn.app.R;

/**
 * Created by daixiaodong on 15/8/9.
 */
public class DialogUtil {


    public static AlertDialog createProgressDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_progress);
        builder.setCancelable(false);
        return builder.create();
    }

    public static void ShowAlertDialog(Context context, @StringRes int messageResId,@Nullable DialogInterface.OnClickListener positiveBtnClickListener,@Nullable DialogInterface.OnClickListener negativeBtnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageResId);
        builder.setNegativeButton("取消", negativeBtnClickListener);
        builder.setPositiveButton("确定", positiveBtnClickListener);
        builder.show();
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

}
