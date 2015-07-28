package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import cn.daixiaodong.myapp.R;

/**
 * 用户关注的其他用户列表  Adapter
 */
public class UserFollowListAdapter extends RecyclerView.Adapter<UserFollowListAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;

    public UserFollowListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public UserFollowListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setDataSet(List<AVObject> data) {
        this.mDataSet = data;
        notifyDataSetChanged();
    }

    public void addData(List<AVObject> data) {
        this.mDataSet.addAll(0, data);
        this.notifyItemInserted(1);
    }

    @Override
    public UserFollowListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_follow, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserFollowListAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
        viewHolder.unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onUnfollowBtnClick(viewHolder, i);
                }
            }
        });

        viewHolder.usernameText.setText(mDataSet.get(i).getString("username"));
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(MyViewHolder viewHolder, int pos);
        void onUnfollowBtnClick(MyViewHolder viewHolder,int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView usernameText;
        public Button unfollowBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            usernameText = (TextView) itemView.findViewById(R.id.tv_username);
            unfollowBtn = (Button) itemView.findViewById(R.id.btn_unfollow);
        }
    }


}
