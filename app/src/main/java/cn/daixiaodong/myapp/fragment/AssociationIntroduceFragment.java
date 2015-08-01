package cn.daixiaodong.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.RegistrationInformationActivity;
import cn.daixiaodong.myapp.activity.RegistrationInformationActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.config.Constants;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;


public class AssociationIntroduceFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mAssociationId;


    private AVObject mAssociation;
    private View convertView;
    private TextView mIntroduce;
    private Button mBtnJoin;
    private ProgressBar mProgressLoad;
    private RelativeLayout mLayoutContainer;


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
            Log.i("mAssociationId", mAssociationId);

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
        loadData();

    }

    private void loadData() {
        final AVQuery<AVObject> query = new AVQuery<>("association");
        query.whereEqualTo("objectId", mAssociationId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        mAssociation = list.get(0);
                        mIntroduce.setText(mAssociation.getString("introduce"));
                        if (isSignIn()) {
                            AVQuery<AVObject> query1 = new AVQuery<>(Constants.TABLE_USER_JOIN);
                            query1.whereEqualTo("user", AVUser.getCurrentUser());
                            query1.whereEqualTo("association", mAssociation);
                            query1.countInBackground(new CountCallback() {
                                @Override
                                public void done(int i, AVException e) {
                                    if (e == null) {
                                        if (i > 0) {
                                            mBtnJoin.setText("你已加入");
                                            mBtnJoin.setEnabled(false);

                                        }
                                        mProgressLoad.setVisibility(View.GONE);
                                        mLayoutContainer.setVisibility(View.VISIBLE);

                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        mProgressLoad.setVisibility(View.GONE);
                        mLayoutContainer.setVisibility(View.VISIBLE);

                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViews() {
        mIntroduce = (TextView) convertView.findViewById(R.id.tv_introduce);
        mBtnJoin = (Button) convertView.findViewById(R.id.btn_join);
        mBtnJoin.setOnClickListener(this);
        mProgressLoad = (ProgressBar) convertView.findViewById(R.id.progress_load);
        mLayoutContainer = (RelativeLayout) convertView.findViewById(R.id.rlayout_container);
    }


    @Override
    public void onClick(View v) {
        if (isSignIn()) {
            RegistrationInformationActivity_.intent(this).mAssociationId(mAssociationId).startForResult(RegistrationInformationActivity.REQUEST_JOIN);
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
        if (requestCode == RegistrationInformationActivity.REQUEST_JOIN && resultCode == RegistrationInformationActivity.RESULT_OK) {
            mBtnJoin.setEnabled(false);
            mBtnJoin.setText("已加入");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("你已成功加入" + mAssociation.get("title"));
            builder.setNegativeButton("确定", null);
        }
    }
}
