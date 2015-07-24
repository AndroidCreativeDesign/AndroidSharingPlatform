package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


@EActivity
public class RegistrationInformationActivity extends BaseActivity {

    @ViewById(R.id.id_et_name)
    EditText mName;
    @ViewById(R.id.id_et_student_id)
    EditText mStudentId;
    @ViewById(R.id.id_et_phone_number)
    EditText mPhoneNumber;
    @ViewById(R.id.id_et_class)
    EditText mClass;

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mToolbar;

    @Extra("associationId")
    String mAssociationId;


    private AVObject mAssociation;

    @AfterExtras
    void initData() {
        if (mAssociationId == null) {
//              new Exception("mAssociationId null");
        }
        AVQuery<AVObject> query = new AVQuery<>("association");
        query.whereEqualTo("objectId", mAssociationId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mAssociation = list.get(0);
                }
            }
        });
    }


    @AfterViews
    void initViews(){
//        setUpToolbar();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
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
                AVObject object = new AVObject("user_association");
                object.put("name", mName.getText().toString());
                object.put("studentId", mStudentId.getText().toString());
                object.put("phoneNum", mPhoneNumber.getText().toString());
                object.put("class", mClass.getText().toString());
                object.put("user", AVUser.getCurrentUser());
                object.put("association",mAssociation);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showToast("报名成功");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case android.R.id.home:
                finish();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
