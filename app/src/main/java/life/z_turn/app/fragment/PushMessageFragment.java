package life.z_turn.app.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import life.z_turn.app.R;
import life.z_turn.app.activity.NotificationListActivity_;
import life.z_turn.app.activity.ReceiveCommentActivity_;
import life.z_turn.app.fragment.common.BaseFragment;

/**
 * 消息
 */
public class PushMessageFragment extends BaseFragment {

    private View mConvertView;
    private TextView mTextNotification;
    private TextView mTextComment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_message, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    void init() {
        LinearLayout lLayoutNotification = (LinearLayout) mConvertView.findViewById(R.id.llayout_notification);
        LinearLayout lLayoutComment = (LinearLayout) mConvertView.findViewById(R.id.llayout_comment);
        mTextNotification = (TextView) lLayoutNotification.findViewById(R.id.text_notification);
        mTextComment = (TextView) lLayoutComment.findViewById(R.id.text_comment);
        lLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListActivity_.intent(getActivity()).start();
            }
        });
        lLayoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveCommentActivity_.intent(getActivity()).start();
            }
        });
    }


}
