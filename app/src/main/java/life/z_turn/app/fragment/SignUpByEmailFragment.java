package life.z_turn.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import life.z_turn.app.R;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.DialogUtil;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.FileUtil;
import life.z_turn.app.utils.SharedPreferencesUtil;
import life.z_turn.app.utils.TextUtil;
import life.z_turn.app.utils.ToastUtil;

/**
 * Created by daixiaodong on 15/8/6.
 */
public class SignUpByEmailFragment extends Fragment {


    private View convertView;
    private CircleImageView mImgProfilePhoto;
    private EditText mEditEmail;
    private EditText mEditUsername;
    private EditText mEditPassword;
    private Button mBtnDone;

    private Spinner mSpinnerYear;
    private Spinner mSpinnerCollege;
    private AlertDialog mProgressDialog;

    private File mImageCaptureFile;
    private File mImageOutputFile;
    private AVFile mProfilePhotoAvFile;

    private int mYearId;
    private int mCollegeId;
    private int mGenderId;
    private String mEmail;
    private String mUsername;
    private String mPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_sign_in_by_email, container, false);
        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mEditEmail = (EditText) convertView.findViewById(R.id.edit_email);
        mEditUsername = (EditText) convertView.findViewById(R.id.edit_username);
        mEditPassword = (EditText) convertView.findViewById(R.id.edit_password);
        mBtnDone = (Button) convertView.findViewById(R.id.btn_done);
        mImgProfilePhoto = (CircleImageView) convertView.findViewById(R.id.img_profile_photo);
        mImgProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfilePhoto();
            }
        });
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
        mSpinnerYear = (Spinner) convertView.findViewById(R.id.spinner_year);
        mSpinnerCollege = (Spinner) convertView.findViewById(R.id.spinner_college);
        mSpinnerCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCollegeId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYearId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> mYearAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_year, getResources().getStringArray(R.array.year));
        ArrayAdapter<String> mCollegeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_year, getResources().getStringArray(R.array.college));
        mSpinnerYear.setAdapter(mYearAdapter);
        mSpinnerCollege.setAdapter(mCollegeAdapter);

    }


    public void done() {
        // 数据校验
        mEmail = mEditEmail.getText().toString();
        mUsername = mEditUsername.getText().toString();
        mPassword = mEditPassword.getText().toString();

        if (!TextUtil.isEmail(mEmail)) {
            Toast.makeText(getActivity(), "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mPassword.isEmpty()) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPassword.length() < 6) {
            ToastUtil.showToast(getActivity(), R.string.password_length_must_bigger_than_6);
            return;
        }

        if (mUsername.isEmpty()) {
            Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtil.isUsername(mUsername)){
            Toast.makeText(getActivity(), "用户名为1-7个中文或14个字符", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = DialogUtil.createProgressDialog(getActivity());


        if (mImageOutputFile != null) {
            if (mProfilePhotoAvFile != null && mImageOutputFile.getName().equals(mProfilePhotoAvFile.getOriginalName())) {
                submitUserProfile();
            } else {
                uploadProfilePhoto();

            }
        } else {
            submitUserProfile();
        }


/*
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                DialogUtil.dismissDialog(mProgressDialog);
                if (e == null) {
                    ToastUtil.showToast(getActivity(), R.string.success_sign_up);
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
*/
    }


    private void uploadProfilePhoto() {
        try {
            mProfilePhotoAvFile = AVFile.withFile(mImageOutputFile.getName(), mImageOutputFile);
            mProfilePhotoAvFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        ToastUtil.showToast(getActivity(), "上传成功");
                        submitUserProfile();
                    } else {

                        DialogUtil.dismissDialog(mProgressDialog);
                        ExceptionUtil.printStackTrace(e);
                        ToastUtil.showErrorMessageToastByException(getActivity(), e);
                    }
                }
            });
        } catch (IOException e) {
            DialogUtil.dismissDialog(mProgressDialog);
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(getActivity(), e);
        }
    }

    private void submitUserProfile() {
        AVUser user = new AVUser();
        user.setEmail(mEmail);
        user.setUsername(mUsername);
        user.setPassword(mPassword);
//        user.put(Constants.COLUMN_COLLECT_ID, mCollegeId);
//        user.put(Constants.COLUMN_YEAR_ID, mYearId);
        if (mProfilePhotoAvFile != null) {
            user.put("profilePhotoUrl", mProfilePhotoAvFile.getUrl());
        }
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                DialogUtil.dismissDialog(mProgressDialog);
                if (e == null) {
                    ToastUtil.showToast(getActivity(), R.string.success_sign_up);
                    SharedPreferencesUtil.saveUserInfo(getActivity());
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    void chooseProfilePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        builder.show();
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
            ToastUtil.showErrorMessageToastByException(getActivity(), e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_MAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                mImageOutputFile = FileUtil.createImageFile();
                cropImage(Uri.fromFile(mImageCaptureFile), mImageOutputFile, 100, 100);
            } catch (IOException e) {
                ExceptionUtil.printStackTrace(e);
                ToastUtil.showErrorMessageToastByException(getActivity(), e);
            }
        }
        if (requestCode == Constants.REQUEST_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
            mImgProfilePhoto.setImageURI(Uri.fromFile(mImageOutputFile));
        }
        if (requestCode == Constants.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    mImageOutputFile = FileUtil.createImageFile();
                    cropImage(uri, mImageOutputFile, 300, 300);
                } catch (IOException e) {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
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

}
