package life.z_turn.app.fragment;

import android.app.Activity;
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
import life.z_turn.app.activity.RegistrationInformationActivity;
import life.z_turn.app.activity.RegistrationInformationActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.activity.SignInActivity_;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.common.BaseFragment;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.LogUtil;
import life.z_turn.app.utils.ToastUtil;


public class AssociationIntroduceFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mAssociationId;
    private String mAssociationStringObject;

    private AVObject mAssociation;
    private View convertView;
    private TextView mIntroduce;
    private Button mBtnJoin;


    public static AssociationIntroduceFragment newInstance(String param1, String param2) {
        AssociationIntroduceFragment fragment = new AssociationIntroduceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AssociationIntroduceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAssociationId = getArguments().getString(ARG_PARAM1);
            mAssociationStringObject = getArguments().getString(ARG_PARAM2);
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
        initData();

    }

    private void initData() {
        try {
            mAssociation = AVObject.parseAVObject(mAssociationStringObject);
            LogUtil.i("mAssociation", mAssociation.getObjectId());
            mIntroduce.setText(mAssociation.getString(Constants.COLUMN_INTRODUCE));
            updateBtnJoin();
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(getActivity(), e);
        }
    }

    private void updateBtnJoin() {
        if (isSignIn()) {
            AVQuery<AVObject> query1 = new AVQuery<>(Constants.TABLE_USER_JOIN);
            query1.whereEqualTo(Constants.COLUMN_USER, AVUser.getCurrentUser());
            query1.whereEqualTo(Constants.COLUMN_ASSOCIATION, mAssociation);
            query1.countInBackground(new CountCallback() {
                @Override
                public void done(int i, AVException e) {
                    if (e == null) {
                        if (i > 0) {
                            mBtnJoin.setText("已加入");
                            mBtnJoin.setEnabled(false);
                        } else {
                            mBtnJoin.setText("加入");
                            mBtnJoin.setEnabled(true);
                        }
                    } else {
                        ExceptionUtil.printStackTrace(e);
                        ToastUtil.showErrorMessageToastByException(getActivity(), e);
                    }
                }
            });
        } else {
            mBtnJoin.setText("登录并报名加入该协会");
            mBtnJoin.setEnabled(true);

        }
    }


    private void initViews() {
        mIntroduce = (TextView) convertView.findViewById(R.id.tv_introduce);
        mBtnJoin = (Button) convertView.findViewById(R.id.btn_apply);
        mBtnJoin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (isSignIn()) {
            RegistrationInformationActivity_.intent(this).extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT,mAssociationStringObject).mAssociationId(mAssociationId).startForResult(RegistrationInformationActivity.REQUEST_JOIN);
        } else {
            SignInActivity_.intent(this).startForResult(SignInActivity.SIGN_IN_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE && resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            RegistrationInformationActivity_.intent(this).mAssociationId(mAssociationId).startForResult(RegistrationInformationActivity.REQUEST_JOIN);
        }
        if (requestCode == RegistrationInformationActivity.REQUEST_JOIN && resultCode == Activity.RESULT_OK) {
            mBtnJoin.setEnabled(false);
            mBtnJoin.setText("已加入");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("你已成功加入" + mAssociation.get(Constants.COLUMN_TITLE));
            builder.setNegativeButton("确定", null);
        }
    }
}
