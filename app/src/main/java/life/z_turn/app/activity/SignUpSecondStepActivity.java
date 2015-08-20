package life.z_turn.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.ActivityCollector;
import life.z_turn.app.activity.common.BaseActivity;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 验证验证码，完善注册信息页面
 */
@EActivity(R.layout.activity_sign_up_second_step)
public class SignUpSecondStepActivity extends BaseActivity {


    private static final int RESULT_REQUEST_PHOTO = 4585;
    private static final int REQUEST_IMAGE_CAPTURE = 1854;
    private static final int REQUEST_IMAGE_CROP = 1983;
    private static final int REQUEST_PICK = 2345;
    @ViewById(R.id.toolbar)
    Toolbar mViewToolbar;

    @ViewById(R.id.id_et_sign_up_second_step_verify_code)
    EditText mViewVerifyCode;


    @ViewById(R.id.id_btn_sign_up_second_step_get_verify_code)
    Button mViewGetVerifyCode;


    @ViewById(R.id.img_profile_photo)
    CircleImageView mProfilePhoto;


    @ViewById(R.id.group_gender)
    RadioGroup mGenderGroup;

    @ViewById(R.id.et_username)
    EditText mUsername;

    private int mGender = 1;  // 1:男  0: 女
    private AVFile file;

    @AfterViews
    void init() {
        initToolbar();
        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_male:
                        mGender = 1;
                        break;
                    case R.id.rbtn_female:
                        mGender = 0;
                        break;

                }
            }
        });
    }


    @Click(R.id.img_profile_photo)
    void choosePhoto() {
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
        startActivityForResult(intent, REQUEST_PICK);
    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private File photoFile;
    File extraOutputFile = null;

    private void choosePhotoByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /* fileUri = CameraPhotoUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);*/
        // Create the File where the photo should go

        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
        }
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                extraOutputFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            cropImage(Uri.fromFile(photoFile), 500, 500);
        }
        if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {
            try {
                file = AVFile.withFile("sdfsdfads.jpg", extraOutputFile);
                showToast("保存");
                Picasso.with(this).load(extraOutputFile).into(mProfilePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    extraOutputFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = data.getData();
                String path = uri.getPath();
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                showToast(picturePath);
//                mProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                mProfilePhoto.setImageURI(uri);
               /* Picasso.with(this).load(picturePath).into(mProfilePhoto);*/
                try {
                    file = AVFile.withAbsoluteLocalPath("shdf.jpg", picturePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cropImage(uri, 500, 500);

            }
        }
    }

    private void cropImage(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        if (extraOutputFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(extraOutputFile));

        }
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_IMAGE_CROP);
    }

    @Click(R.id.id_btn_sign_up_second_step_get_verify_code)
    void getVerifyCode() {
        //如果你的账号需要重新发送短信请参考下面的代码
        AVUser.requestMobilePhoneVerifyInBackground("18370661127", new RequestMobileCodeCallback() {

            @Override
            public void done(AVException e) {
                //发送了验证码以后做点什么呢
                if (e == null) {
                    showToast("发送成功");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @ViewById(R.id.id_btn_sign_up_second_step_done)
    Button mViewDone;

    @Click(R.id.id_btn_sign_up_second_step_done)
    void done() {
        final String verifyCode = mViewVerifyCode.getText().toString();
        if (verifyCode.isEmpty()) {
            showToast("请输入验证码");
            return;
        }
        if (mUsername.getText().toString().isEmpty()) {
            showToast("请输入用户名");
            return;
        }

        if (file != null) {
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Log.i("profilePhotoUrl", file.getUrl());
                        saveUserInfoWithProfilePhoto(verifyCode);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            saveUserInfoWithoutProfilePhoto(verifyCode);
        }


    }

    private void saveUserInfoWithoutProfilePhoto(final String verifyCode) {
        AVUser user = AVUser.getCurrentUser();
        user.setUsername(mUsername.getText().toString());
        user.put("gender", mGender);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                toVerifyCode(verifyCode);
            }
        });
    }

    private void saveUserInfoWithProfilePhoto(final String verifyCode) {
        AVUser user = AVUser.getCurrentUser();
        user.setUsername(mUsername.getText().toString());
        user.put("gender", mGender);
        user.put("profilePhotoUrl", file.getUrl());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    toVerifyCode(verifyCode);
                } else {
                    e.printStackTrace();
                    showToast("用户名已被使用");
                }

            }
        });
    }

    private void toVerifyCode(String verifyCode) {
        AVUser.verifyMobilePhoneInBackground(verifyCode, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("验证成功");
                    MainActivity_.intent(SignUpSecondStepActivity.this).start();
                    ActivityCollector.finishActivity(SignUpFirstStepActivity_.class.getSimpleName());
                    ActivityCollector.finishActivity(SignInActivity_.class.getSimpleName());
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle("完善信息");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
