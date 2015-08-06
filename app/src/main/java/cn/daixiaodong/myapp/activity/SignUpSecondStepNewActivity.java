package cn.daixiaodong.myapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.config.Constants;
import cn.daixiaodong.myapp.utils.FileUtil;
import de.hdodenhof.circleimageview.CircleImageView;


@EActivity(R.layout.activity_sign_up_second_step_new)
public class SignUpSecondStepNewActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    @Extra("mobilePhoneNumber")
    String mMobilePhoneNumber;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.img_profile_photo)
    CircleImageView mImgProfilePhoto;

    @ViewById(R.id.edit_password)
    EditText mEditPassword;

    @ViewById(R.id.edit_username)
    EditText mEditUsername;


    @ViewById(R.id.spinner_year)
    Spinner mSpinnerYear;

    @ViewById(R.id.spinner_college)
    Spinner mSpinnerCollege;

    @ViewById(R.id.group_gender)
    RadioGroup mGroupGender;

    private int mYearId;
    private int mCollegeId;
    private int mGenderId;
    private File mImageCaptureFile;
    private File mImageOutputFile;
    private String mUsername;
    private String mPassword;
    private AVFile mProfilePhotoAvFile;
    private ProgressDialog mProgressDialog;


    @AfterExtras
    void initData() {
        showToast(mMobilePhoneNumber + "");
    }

    @AfterViews
    void initViews() {
        initToolbar();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_year, getResources().getStringArray(R.array.year));
        mSpinnerYear.setAdapter(adapter);
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_year, getResources().getStringArray(R.array.college));
        mSpinnerCollege.setAdapter(collegeAdapter);

        mSpinnerYear.setOnItemSelectedListener(this);
        mSpinnerCollege.setOnItemSelectedListener(this);
        mGroupGender.setOnCheckedChangeListener(this);
    }


    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("完善资料");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_second_step_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int viewId = parent.getId();
        switch (viewId) {
            case R.id.spinner_year:
                mYearId = position;
                break;
            case R.id.spinner_college:
                mCollegeId = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_male:
                mGenderId = 0;
                break;
            case R.id.rbtn_female:
                mGenderId = 1;
                break;
        }
    }


    @Click(R.id.img_profile_photo)
    void chooseProfilePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.item_choose_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        choosePhotoByCamera();
                        break;
                    case 1:
                        choosePhotoByGallery();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void choosePhotoByGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_PICK);
    }

    private void choosePhotoByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mImageCaptureFile = FileUtil.createImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageCaptureFile));
            startActivityForResult(intent, Constants.REQUEST_MAGE_CAPTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_MAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageOutputFile = FileUtil.createImageFile();
                cropImage(Uri.fromFile(mImageCaptureFile), mImageOutputFile, 100, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == Constants.REQUEST_CROP_IMAGE && resultCode == RESULT_OK) {
            mImgProfilePhoto.setImageURI(Uri.fromFile(mImageOutputFile));
        }
        if (requestCode == Constants.REQUEST_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    mImageOutputFile = FileUtil.createImageFile();
                    cropImage(uri, mImageOutputFile, 100, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void cropImage(Uri uri, File outputFile, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constants.REQUEST_CROP_IMAGE);
    }


    @Click(R.id.btn_done)
    void done() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("注册中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mUsername = mEditUsername.getText().toString();
        mPassword = mEditPassword.getText().toString();


        // 判断输入数据

        if (mImageOutputFile != null) {
            if (mProfilePhotoAvFile != null && mImageOutputFile.getName().equals(mProfilePhotoAvFile.getOriginalName())) {
                submitUserProfile();
            } else {
                uploadProfilePhoto();

            }
        } else {
            submitUserProfile();
        }


    }

    private void uploadProfilePhoto() {
        try {
            mProfilePhotoAvFile = AVFile.withFile(mImageOutputFile.getName(), mImageOutputFile);
            mProfilePhotoAvFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("头像上传成功");
                        submitUserProfile();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void submitUserProfile() {
        AVUser user = new AVUser();
        user.setMobilePhoneNumber("18079609748");
        user.setUsername(mUsername);
        user.setPassword(mPassword);
        user.put("year", mYearId);
        user.put("college", mCollegeId);
        user.put("gender", mGenderId);
        if (mImageOutputFile != null) {
            user.put("profilePhotoUrl", mProfilePhotoAvFile.getUrl());
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (e == null) {
                    showToast("注册成功");
                    SignUpSecondStepNewActivity.this.setResult(RESULT_OK);
                    finish();
                } else {
                    e.printStackTrace();
                    showToast("用户名已被使用");
                }
            }
        });
    }
}
