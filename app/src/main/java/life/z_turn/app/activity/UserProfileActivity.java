package life.z_turn.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.FileUtil;
import life.z_turn.app.utils.SharedPreferencesUtil;
import life.z_turn.app.utils.ToastUtil;


/**
 * 用户个人资料 界面
 */
@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivity {

    private String mUserId;
    private AVUser mUser;
    private File mImageCaptureFile;
    private File mImageOutputFile;
    private AVFile mProfilePhotoAvFile;
    private boolean isFollow;
    private boolean isCurrentUser;


    @Extra(Constants.EXTRA_KEY_USER_STRING_OBJECT)
    String mUserObjcetString;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.img_profile_photo)
    CircleImageView mImgProfilePhoto;


    @ViewById(R.id.text_username)
    TextView mTextUsername;

    @ViewById(R.id.text_gender)
    TextView mTextGender;

    @ViewById(R.id.text_college)
    TextView mTextCollege;

    @ViewById(R.id.text_major)
    TextView mTextMajor;

    @ViewById(R.id.text_year)
    TextView mTextYear;

    @ViewById(R.id.text_user_join_num)
    TextView mTextUserJoinNum;

    @ViewById(R.id.text_user_publish_num)
    TextView mTextUserPublishNum;

    @ViewById(R.id.text_signature)
    TextView mTextSignture;

    @ViewById(R.id.ripple_username)
    MaterialRippleLayout mRippleUsername;

    @ViewById(R.id.ripple_gender)
    MaterialRippleLayout mRippleGender;

    @ViewById(R.id.ripple_college)
    MaterialRippleLayout mRippleCollege;


    @ViewById(R.id.ripple_major)
    MaterialRippleLayout mRippleMajor;


    @ViewById(R.id.ripple_year)
    MaterialRippleLayout mRippleYear;
    private MenuItem mMenuItemFollow;


    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("个人资料");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Uri uri = intent.getData();


        if (AVUser.getCurrentUser().getObjectId().equals(intent.getStringExtra("userId"))) {
            mUser = AVUser.getCurrentUser();
            isCurrentUser = true;
            setCurrentUserData();
        } else if (uri != null) {
            mUserId = uri.getQueryParameter("userId");
            isCurrentUser = isSignIn() && mUserId.equals(AVUser.getCurrentUser().getObjectId());

            loadData();
        } else {
            try {
                mUser = (AVUser) AVObject.parseAVObject(mUserObjcetString);
                mUserId = mUser.getObjectId();
                isCurrentUser = isSignIn() && mUser.getObjectId().equals(AVUser.getCurrentUser().getObjectId());
                setData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setCurrentUserData() {
        AVUser user = SharedPreferencesUtil.readUserInfo(this);
        mTextUsername.setText(user.getUsername());
        if (user.getInt(Constants.COLUMN_COLLECT_ID) == Constants.FLAG_NO_DATA) {
            mTextCollege.setText(R.string.not_fill_out);
        } else {
            mTextCollege.setText(getResources().getStringArray(R.array.college)[user.getInt("college")]);

        }
        mTextMajor.setText(user.getString("major"));
        mTextYear.setText(getResources().getStringArray(R.array.year)[user.getInt("year")]);
        if (mUser.getInt("gender") == Constants.FLAG_NO_DATA) {
            mTextGender.setText("未填写");
        } else {
            mTextGender.setText(getResources().getStringArray(R.array.gender)[user.getInt("gender")]);
        }
        mTextUserJoinNum.setEnabled(false);
        mTextUserPublishNum.setEnabled(false);
        loadJoinData();
        loadPublishData();
        Picasso.with(this).load(mUser.getString("profilePhotoUrl")).into(mImgProfilePhoto);
    }

    private void loadData() {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("objectId", mUserId);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (list.size() == 1) {
                        mUser = list.get(0);
                        showToast("数据加载完毕");
                        setData();
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                }
            }
        });
    }

    private void setData() {
        mTextUsername.setText(mUser.getUsername());
        if (mUser.getInt(Constants.COLUMN_COLLECT_ID) == Constants.FLAG_NO_DATA) {
            mTextCollege.setText(R.string.not_fill_out);
        } else {
            mTextCollege.setText(getResources().getStringArray(R.array.college)[mUser.getInt("college")]);

        }
        mTextMajor.setText(mUser.getString("major"));
        mTextYear.setText(getResources().getStringArray(R.array.year)[mUser.getInt("year")]);
        if (mUser.getInt("gender") == Constants.FLAG_NO_DATA) {
            mTextGender.setText("未填写");
        } else {
            mTextGender.setText(getResources().getStringArray(R.array.gender)[mUser.getInt("gender")]);
        }
        loadJoinData();
        loadPublishData();

        Picasso.with(this).load(mUser.getString("profilePhotoUrl")).into(mImgProfilePhoto);

        if (!isCurrentUser) {
            loadFollowStatus();
        }

        if (!isCurrentUser) {
            mImgProfilePhoto.setEnabled(false);
            mRippleCollege.setEnabled(false);
            mRippleUsername.setEnabled(false);
            mRippleGender.setEnabled(false);
            mRippleYear.setEnabled(false);
            mRippleMajor.setEnabled(false);
        }
    }

    private void loadPublishData() {
        AVQuery query = new AVQuery(Constants.TABLE_IDEA);
        query.whereEqualTo("user", mUser);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    String num = getResources().getString(R.string.user_publish_num, i);
                    mTextUserPublishNum.setText(num);
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                }
            }
        });
    }

    private void loadJoinData() {
        AVQuery query = new AVQuery(Constants.TABLE_USER_JOIN);
        query.whereEqualTo("user", mUser);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    String num = getResources().getString(R.string.user_join_num, i);
                    mTextUserJoinNum.setText(num);
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                }
            }
        });

    }

    private void loadFollowStatus() {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<AVObject> relation = user.getRelation("follow");
        AVQuery<AVObject> query = relation.getQuery();
        query.whereEqualTo("objectId", mUserId);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    if (i > 0) {
                        isFollow = true;

                    } else {
                        isFollow = false;
                    }
                    updateMenuItemFollow();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                }
            }
        });


    }

    private void updateMenuItemFollow() {
        if (isFollow) {
            if (mMenuItemFollow != null) {
                mMenuItemFollow.setTitle("已关注");
            }
        } else {
            if (mMenuItemFollow != null) {
                mMenuItemFollow.setTitle("关注");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isCurrentUser) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            mMenuItemFollow = menu.findItem(R.id.action_follow);
        }
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
            finish();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_follow:
                setFollowStatus();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Click(R.id.text_user_join_num)
    void toUserJoin() {
        if (!isCurrentUser) {
//            UserJoinActivity_.intent(this).extra().start();
            UserJoinActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_ID, mUser.getObjectId()).start();
        }
    }

    @Click(R.id.text_user_publish_num)
    void toUserPublish() {
        if (!isCurrentUser) {
            UserPublishActivity_.intent(this).extra(Constants.EXTRA_KEY_OBJECT_STRING, mUser.toString()).start();
        }
    }


    void setFollowStatus() {

        if (!isSignIn()) {
            SignInActivity_.intent(this).startForResult(Constants.REQUEST_SIGN_IN);
            return;
        }
        saveFollowStatus();

    }

    private void saveFollowStatus() {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<AVObject> relation = user.getRelation("follow");
        if (isFollow) {
            relation.remove(mUser);
        } else {
            relation.add(mUser);
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    if (isFollow) {
                        showToast("已取消关注");
                        isFollow = false;

//                        mBtnFollow.setText("关注");
                    } else {
                        showToast("成功关注");
                        isFollow = true;
//                        mBtnFollow.setText("取消关注");
                    }
                    updateMenuItemFollow();
                }
            }
        });
    }


    @Click(R.id.ripple_username)
    void setUsername() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_real_name, null);
        final EditText editRealName = (EditText) view.findViewById(R.id.dialog_edit_content);
        editRealName.setHint("设置用户名");
        editRealName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        editRealName.setMaxLines(1);
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = editRealName.getText().toString();
                        if (username.trim().isEmpty()) {
                            return;
                        } else {
                            dialog.dismiss();
                            mTextUsername.setText(username);
                            saveProfile("username", username);
                        }


                    }
                });
            }
        });
        dialog.show();

    }

    @Click(R.id.ripple_gender)
    void setGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("取消", null);
        builder.setItems(R.array.gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextGender.setText(getResources().getStringArray(R.array.gender)[which]);
                saveProfile("gender", which);
            }
        });
        builder.show();
    }

    @Click(R.id.ripple_college)
    void setCollege() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setItems(R.array.college, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextCollege.setText(getResources().getStringArray(R.array.college)[which]);
                saveProfile("college", which);
            }
        });
        builder.show();
    }

    @Click(R.id.ripple_major)
    void setMajor() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择专业");
        builder.show();
    }


    @Click(R.id.ripple_year)
    void setYear() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("取消", null);
        builder.setItems(R.array.year, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextYear.setText(getResources().getStringArray(R.array.year)[which]);
                saveProfile("year", which);
            }
        });
        builder.show();
    }

    @Click(R.id.ripple_signature)
    void setSignature() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_real_name, null);
        final EditText editRealName = (EditText) view.findViewById(R.id.dialog_edit_content);
        editRealName.setHint("设置折腾宣言");
        editRealName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        editRealName.setMaxLines(3);
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String signature = editRealName.getText().toString();
                        if (signature.trim().isEmpty()) {
                            return;
                        } else {
                            dialog.dismiss();
                            mTextSignture.setText(signature);
                            saveProfile("signature", signature);
                        }


                    }
                });
            }
        });
        dialog.show();
    }

    public void saveProfile(final String key, final Object object) {
        AVUser user = AVUser.getCurrentUser();

        user.put(key, object);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

                if (e != null) {
                    e.printStackTrace();
                } else {
                    switch (key) {
                        case "username":
                            SharedPreferencesUtil.saveUserUsername(UserProfileActivity.this, (String) object);
                            Intent intent = new Intent(Constants.ACTION_USER_INFO_CHANGE);
                            sendBroadcast(intent);
                            break;
                        case "gender":
                            SharedPreferencesUtil.saveUserGender(UserProfileActivity.this, (int) object);
                            break;
                        case "college":
                            SharedPreferencesUtil.saveUserCollege(UserProfileActivity.this, (int) object);
                            break;
                        case "year":
                            SharedPreferencesUtil.saveUserYear(UserProfileActivity.this, (int) object);
                            break;
                    }
                }
            }
        });
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
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_MAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageOutputFile = FileUtil.createImageFile();
                cropImage(Uri.fromFile(mImageCaptureFile), mImageOutputFile, 100, 100);
            } catch (IOException e) {
                ExceptionUtil.printStackTrace(e);
                ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
            }
        }
        if (requestCode == Constants.REQUEST_CROP_IMAGE && resultCode == RESULT_OK) {
            mImgProfilePhoto.setImageURI(Uri.fromFile(mImageOutputFile));
            uploadProfilePhoto();
        }
        if (requestCode == Constants.REQUEST_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    mImageOutputFile = FileUtil.createImageFile();
                    cropImage(uri, mImageOutputFile, 100, 100);
                } catch (IOException e) {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
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


    private void uploadProfilePhoto() {
        try {
            mProfilePhotoAvFile = AVFile.withFile(mImageOutputFile.getName(), mImageOutputFile);
            mProfilePhotoAvFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("头像上传成功");
                        saveProfilePhoto();
                    } else {
                        ExceptionUtil.printStackTrace(e);
                        ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                    }
                }
            });
        } catch (IOException e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
        }
    }

    private void saveProfilePhoto() {
        AVUser user = AVUser.getCurrentUser();
        if (mImageOutputFile != null) {
            user.put("profilePhotoUrl", mProfilePhotoAvFile.getUrl());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("成功");
                        SharedPreferencesUtil.saveUserProfilePhotoUrl(UserProfileActivity.this, mProfilePhotoAvFile.getUrl());
                        Intent intent = new Intent(Constants.ACTION_USER_INFO_CHANGE);
                        sendBroadcast(intent);
                    } else {
                        ExceptionUtil.printStackTrace(e);
                        ToastUtil.showErrorMessageToastByException(UserProfileActivity.this, e);
                    }
                }
            });
        }
    }

}
