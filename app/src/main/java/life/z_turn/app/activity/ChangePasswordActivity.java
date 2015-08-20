package life.z_turn.app.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.utils.DialogUtil;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;


@EActivity(R.layout.activity_change_password)
public class ChangePasswordActivity extends AppCompatActivity {


    @ViewById(R.id.edit_old_password)
    EditText mEditOldPassword;

    @ViewById(R.id.edit_new_password)
    EditText mEditNewPassword;


    @Click(R.id.btn_done)
    void done() {

        String oldPassword = mEditOldPassword.getText().toString();
        String newPassword = mEditNewPassword.getText().toString();

        final AlertDialog progressDialog = DialogUtil.createProgressDialog(this);
        progressDialog.show();

        AVUser.getCurrentUser().updatePasswordInBackground(oldPassword, newPassword, new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                progressDialog.dismiss();
                if (e == null) {

                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(ChangePasswordActivity.this, e);
                }
            }
        });
    }


}
