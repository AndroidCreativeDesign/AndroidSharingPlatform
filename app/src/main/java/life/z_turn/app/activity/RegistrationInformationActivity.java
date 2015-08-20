package life.z_turn.app.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * 协会报名信息填写界面
 */
@EActivity
public class RegistrationInformationActivity extends BaseActivity implements PayListener {

    public static final int REQUEST_JOIN = 7834;
    private static final int RESULT_PAY_FAIL = 1045;

    @ViewById(R.id.id_et_name)
    EditText mEditName;
    @ViewById(R.id.id_et_student_id)
    EditText mEditStudentId;
    @ViewById(R.id.edit_account_number)
    EditText mEditMobilePhoneNum;
    @ViewById(R.id.id_et_class)
    EditText mEditMajor;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @ViewById(R.id.group_pay_type)
    RadioGroup mGroupPayType;

    @Extra("associationId")
    String mAssociationId;

    @Extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT)
    String mAssociationStringObject;

    private int mPayType;

    private AVObject mAssociation;

    private AVObject mJoin;


    private String mName;
    private String mCollege;
    private String mStudentId;
    private String mMajor;
    private String mMobilePhoneNum;
    private String mDues;

    @AfterExtras
    void initData() {


    }


    @AfterViews
    void initViews() {
//        setUpToolbar();
        mGroupPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_alipay:
                        mPayType = 0;
                        break;
                    case R.id.rbtn_micropay:
                        mPayType = 1;
                        break;
                    default:
                        mPayType = 2;
                        break;
                }
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle("加入");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_information);
        setUpToolbar();
        try {
            mAssociation = AVObject.parseAVObject(mAssociationStringObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_next:

                submitJoinInfo();


                break;
            case android.R.id.home:
                finish();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void submitJoinInfo() {

        mName = mEditName.getText().toString();
        mStudentId = mEditStudentId.getText().toString();
        mMobilePhoneNum = mEditMobilePhoneNum.getText().toString();
        mMajor = mEditMajor.getText().toString();

        if (mJoin == null) {
            mJoin = new AVObject(Constants.TABLE_USER_JOIN);
        }
        saveJoinInfo();
    }

    private void saveJoinInfo() {
        mJoin.put("name", mEditName.getText().toString());
        mJoin.put("studentId", mEditStudentId.getText().toString());
        mJoin.put("phoneNum", mEditMobilePhoneNum.getText().toString());
        mJoin.put("class", mEditMajor.getText().toString());
        mJoin.put("user", AVUser.getCurrentUser());
        mJoin.put("association", mAssociation);
        mJoin.put("payType", mPayType);
        mJoin.put("type", Constants.TYPE_ASSOCIATION);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("保存成功");
                    showPayInfo();
                    //PayActivity_.intent(RegistrationInformationActivity.this).extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT, mAssociationStringObject).start();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(RegistrationInformationActivity.this, e);
                }
            }
        });
    }

    private void showPayInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认信息");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_info, null);
        TextView textName = (TextView) view.findViewById(R.id.text_name);
        TextView textCollege = (TextView) view.findViewById(R.id.text_college);
        TextView textMajor = (TextView) view.findViewById(R.id.text_major);
        TextView textMobilePhoneNum = (TextView) view.findViewById(R.id.text_mobile_phone_num);
        textName.setText(mName);
        textMajor.setText(mMajor);
        textMobilePhoneNum.setText(mMobilePhoneNum);
        builder.setView(view);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pay();
            }
        });
        builder.show();

    }

    private void pay() {
        switch (mPayType) {
            case Constants.PAY_ALIPAY:
                new BmobPay(RegistrationInformationActivity.this).pay(0.01, "会费", this);
                break;
            case Constants.PAY__WEPAY:
                new BmobPay(RegistrationInformationActivity.this).payByWX(0.01, "会费", this);
                break;
        }
    }


    @Override
    public void orderId(String s) {
        mJoin.put("orderId", s);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void succeed() {
        showToast("报名成功");
        mJoin.put("payStatus", 0);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

    }

    @Override
    public void fail(int code, String s) {

        switch (code) {
            case Constants.ERROR_USER_CANCEL_WEPAY:
            case Constants.ERROR_USER_CANCEL_ALIPAY:
                showToast("支付取消");

                break;
            case Constants.ERROR_NO_INSTALL_WEIXIN:
                showToast("未安装微信客户端");

                break;
            case Constants.ERROR_NO_INSTALL_PLUGIN:
                showToast("未安装微信支付插件");

        }
        showToast("支付失败");
        mJoin.put("payStatus", 1);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

            }
        });

    }

    @Override
    public void unknow() {
        showToast("发生未知错误");
        mJoin.put("payStatus", 2);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                setResult(RESULT_PAY_FAIL, getIntent());
                finish();
            }
        });

    }
}
