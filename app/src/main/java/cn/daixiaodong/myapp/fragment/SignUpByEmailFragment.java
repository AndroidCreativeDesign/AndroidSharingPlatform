package cn.daixiaodong.myapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.utils.TextUtil;

/**
 * Created by daixiaodong on 15/8/6.
 */
public class SignUpByEmailFragment extends Fragment {


    private View convertView;
    private EditText mEditEmail;
    private EditText mEditUsername;
    private EditText mEditPassword;
    private Button mBtnDone;

    private Spinner mSpinnerYear;
    private Spinner mSpinnerCollege;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.sign_in_by_email_fragment, container, false);
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
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
        mSpinnerYear = (Spinner) convertView.findViewById(R.id.spinner_year);
        mSpinnerCollege = (Spinner) convertView.findViewById(R.id.spinner_college);
        ArrayAdapter<String> mYearAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_year, getResources().getStringArray(R.array.year));
        ArrayAdapter<String> mCollegeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_year, getResources().getStringArray(R.array.college));
        mSpinnerYear.setAdapter(mYearAdapter);
        mSpinnerCollege.setAdapter(mCollegeAdapter);
    }


    public void done() {
        // 数据校验
        String email = mEditEmail.getText().toString();
        String username = mEditUsername.getText().toString();
        String password = mEditPassword.getText().toString();
        Log.i("email", email);
        if (!TextUtil.isEmail(email)) {
            Toast.makeText(getActivity(), "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty()) {
            Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        AVUser user = new AVUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
