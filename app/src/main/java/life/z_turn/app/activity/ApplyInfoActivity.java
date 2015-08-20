package life.z_turn.app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

@EActivity(R.layout.activity_apply_info)
public class ApplyInfoActivity extends BaseActivity {

    private AVObject mDepartment;
    private String mName;
    private String mStudentId;
    private String mMajor;
    private String mMobilePhoneNumber;

    @Extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT)
    String mDepartmentStringObject;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.llayout_parent)
    LinearLayout mLLayoutParent;

    @ViewById(R.id.edit_name)
    EditText mEditName;
    @ViewById(R.id.edit_student_id)
    EditText mEditStudentId;
    @ViewById(R.id.edit_college)
    EditText mEditCollege;
    @ViewById(R.id.edit_major)
    EditText mEditMajor;
    @ViewById(R.id.edit_mobile_phone_number)
    EditText mEditMobilePhoneNumber;

    @AfterViews
    void init() {
        initToolbar();
        try {
            mDepartment = AVObject.parseAVObject(mDepartmentStringObject);
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(this, e);
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("个人资料");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apply_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            new AlertDialog.Builder(this).setMessage("如果退出，本界面编辑的信息都会丢失，确定退出").setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setPositiveButton("不退出", null).show();
            return true;
        }
        if (id == R.id.action_submit) {
            submitApplyInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    void submitApplyInfo() {
        mName = mEditName.getText().toString();
        mStudentId = mEditStudentId.getText().toString();
        mMajor = mEditMajor.getText().toString();
        mMobilePhoneNumber = mEditMobilePhoneNumber.getText().toString();
        showProgressDialog(true);
        AVObject joinObject = new AVObject(Constants.TABLE_USER_JOIN);
        joinObject.put(Constants.COLUMN_DEPARTMENT, mDepartment);
        joinObject.put(Constants.COLUMN_USER, AVUser.getCurrentUser());
        joinObject.put(Constants.COLUMN_TYPE, Constants.TYPE_STUDENT_UNION_DEPARTMENT);
        joinObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                showProgressDialog(false);
                if (e == null) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(ApplyInfoActivity.this, e);
                }
            }
        });
    }

}
