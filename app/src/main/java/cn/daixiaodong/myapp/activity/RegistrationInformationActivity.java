package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.config.Constants;

/**
 * 协会报名信息填写界面
 */
@EActivity
public class RegistrationInformationActivity extends BaseActivity implements PayListener {

    public static final int REQUEST_JOIN = 7834;
    private static final int RESULT_PAY_FAIL = 1045;
    @ViewById(R.id.id_et_name)
    EditText mName;
    @ViewById(R.id.id_et_student_id)
    EditText mStudentId;
    @ViewById(R.id.edit_mobile_phone_number)
    EditText mPhoneNumber;
    @ViewById(R.id.id_et_class)
    EditText mClass;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @ViewById(R.id.group_pay_type)
    RadioGroup mGroupPayType;

    @Extra("associationId")
    String mAssociationId;

    private int mPayType;

    private AVObject mAssociation;

    private AVObject mJoin;

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
                    case R.id.rbtn_wepay:
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
        mToolbar.setTitle("报名信息");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_information);
        setUpToolbar();

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
        AVQuery<AVObject> query = new AVQuery<>("association");
        query.whereEqualTo("objectId", mAssociationId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mAssociation = list.get(0);
                    mJoin = new AVObject(Constants.TABLE_USER_JOIN);
                    mJoin.put("name", mName.getText().toString());
                    mJoin.put("studentId", mStudentId.getText().toString());
                    mJoin.put("phoneNum", mPhoneNumber.getText().toString());
                    mJoin.put("class", mClass.getText().toString());
                    mJoin.put("user", AVUser.getCurrentUser());
                    mJoin.put("association", mAssociation);
                    mJoin.put("payType", mPayType);
                    mJoin.put("type", Constants.TYPE_ASSOCIATION);
                    mJoin.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                pay();

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
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
