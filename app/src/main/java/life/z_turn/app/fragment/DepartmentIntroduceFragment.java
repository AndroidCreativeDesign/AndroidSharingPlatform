package life.z_turn.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;

import life.z_turn.app.R;
import life.z_turn.app.activity.ApplyInfoActivity;
import life.z_turn.app.activity.ApplyInfoActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;


public class DepartmentIntroduceFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mDepartmentId;

    private AVObject mDepartment;
    private View convertView;
    private TextView mIntroduce;
    private Button mBtnApply;
    private String mDepartmentStringObject;


    public static DepartmentIntroduceFragment newInstance(String param1, String param2) {
        DepartmentIntroduceFragment fragment = new DepartmentIntroduceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DepartmentIntroduceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDepartmentId = getArguments().getString(ARG_PARAM1);
            mDepartmentStringObject = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_association_introduce, container, false);
        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        try {
            mDepartment = AVObject.parseAVObject(mDepartmentStringObject);
            mIntroduce.setText(mDepartment.getString(Constants.COLUMN_INTRODUCE));
            if (isSignIn()) {
                updateBtnApply();
            }
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(getActivity(), e);
        }

    }


    private void updateBtnApply() {
        AVQuery<AVObject> query1 = new AVQuery<>(Constants.TABLE_USER_JOIN);
        query1.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());
        query1.whereEqualTo(Constants.COLUMN_DEPARTMENT, mDepartment);
        query1.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    if (i > 0) {
                        mBtnApply.setText("已报名");
                        mBtnApply.setEnabled(false);
                    } else {
                        mBtnApply.setText("申请加入");
                        mBtnApply.setEnabled(true);
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void initViews() {
        mIntroduce = (TextView) convertView.findViewById(R.id.tv_introduce);
        mBtnApply = (Button) convertView.findViewById(R.id.btn_apply);
        mBtnApply.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (isSignIn()) {
            ApplyInfoActivity_.intent(this).extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT, mDepartmentStringObject).startForResult(Constants.REQUEST_APPLY);
        } else {
            SignInActivity_.intent(this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE && resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            ApplyInfoActivity_.intent(this).extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT, mDepartmentStringObject).startForResult(Constants.REQUEST_APPLY);
        }
        if (requestCode == Constants.REQUEST_APPLY && resultCode == ApplyInfoActivity.RESULT_OK) {
            mBtnApply.setEnabled(false);
            mBtnApply.setText("已申请");
            new AlertDialog.Builder(getActivity()).setMessage("申请成功").setNegativeButton("确定",null).show();
        }
    }
}
