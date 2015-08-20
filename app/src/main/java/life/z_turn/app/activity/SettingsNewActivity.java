package life.z_turn.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;


@EActivity(R.layout.activity_settings_new)
public class SettingsNewActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @ViewById(R.id.divider_1)
    View mViewDivider1;

    @ViewById(R.id.text_account)
    TextView mTextAccountSettings;

    @ViewById(R.id.text_app_version)
    TextView mTextAppVersion;

    @ViewById(R.id.chk_message_push)
    CheckBox mChkMessagePush;

    @ViewById(R.id.btn_sign_out)
    Button mBtnSignOut;


    @Click(R.id.rlayout_message_push)
    void onPushMessageLayoutClick() {
        mChkMessagePush.setChecked(!mChkMessagePush.isChecked());
    }


    @Click(R.id.text_account)
    void onAccountSettingsClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.account_settints, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ChangePasswordActivity_.intent(SettingsNewActivity.this).start();
                        break;
                    case 1:
                        AccountBindingActivity_.intent(SettingsNewActivity.this).start();
                        break;


                }
            }
        });
        builder.show();
    }

    @Click(R.id.text_about_us)
    void onAboutUsClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Z-turn Team 介绍");
        builder.setNegativeButton("确定", null);
        builder.show();

    }

    @Click(R.id.rlayout_update)
    void onUpdateClick() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(R.layout.dialog_progress);
//        builder.show();
        UmengUpdateAgent.forceUpdate(this);


    }


    @CheckedChange(R.id.chk_message_push)
    void checkedChangeOnPushMessage(CompoundButton compoundButton, boolean isChecked) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();//获取编辑器
        editor.putBoolean(Constants.KEY_IS_RECEIVE_PUSH_MESSAGE, isChecked);
        editor.apply();//提交修改
    }

    @Click(R.id.btn_sign_out)
    void onSignOutClick() {
        AVUser.logOut();
        mBtnSignOut.setVisibility(View.GONE);
        mTextAccountSettings.setVisibility(View.GONE);
        mViewDivider1.setVisibility(View.GONE);
        Intent intent = new Intent(Constants.ACTION_USER_SIGN_OUT);
        sendBroadcast(intent);
    }


    @AfterViews
    void initViews() {


        setSupportActionBar(mToolbar);
        mToolbar.setTitle("设置");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String version;
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            version = "获取版本号失败";
        }
        mTextAppVersion.setText(version);
        if (isSignIn()) {
            mBtnSignOut.setVisibility(View.VISIBLE);
            mTextAccountSettings.setVisibility(View.VISIBLE);
            mViewDivider1.setVisibility(View.VISIBLE);

        } else {
            mBtnSignOut.setVisibility(View.GONE);
            mTextAccountSettings.setVisibility(View.GONE);
            mViewDivider1.setVisibility(View.GONE);
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isReceivePushMessage = sharedPref.getBoolean(Constants.KEY_IS_RECEIVE_PUSH_MESSAGE, true);
        mChkMessagePush.setChecked(isReceivePushMessage);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
