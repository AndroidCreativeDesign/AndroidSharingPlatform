package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.daixiaodong.myapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户关注的其他用户列表  Adapter
 */
public class UserFollowListAdapter extends RecyclerView.Adapter<UserFollowListAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public UserFollowListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

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


        AVUser user = (AVUser) mDataSet.get(i);

        viewHolder.usernameText.setText(user.getUsername());
        Picasso.with(mContext).load(user.getString("profilePhotoUrl")).into(viewHolder.profilePhoto);
        // Picasso.with(mContext).load("http://p3.gexing.com/touxiang/20121101/1742/509243ee3badd_200x200_3.jpg").resize(40, 40).placeholder(R.drawable.test_img).into(viewHolder.profilePhoto);
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

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView usernameText;

        public CircleImageView profilePhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            usernameText = (TextView) itemView.findViewById(R.id.tv_username);

            profilePhoto = (CircleImageView) itemView.findViewById(R.id.img_profile_photo);
        }
    }


}
