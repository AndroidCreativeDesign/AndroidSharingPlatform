package life.z_turn.app.adapter;

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

import de.hdodenhof.circleimageview.CircleImageView;
import life.z_turn.app.R;

/**
 * 用户关注的其他用户列表  Adapter
 */
public class UserFollowListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER_VIEW = -1;
    private static final int TYPE_DEFAULT = 0;
    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private View mFooterView;
    private int mItemCount;

    public UserFollowListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void addFooterView(View view) {
        this.mFooterView = view;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        switch (type) {
            case TYPE_FOOTER_VIEW:
                return new FooterViewHolder(mFooterView);
            case TYPE_DEFAULT:
                return new MyViewHolder(mLayoutInflater.inflate(R.layout.item_follow, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {

        switch (getItemViewType(i)) {
            case TYPE_FOOTER_VIEW:
                return;
            case TYPE_DEFAULT:
                AVUser user = (AVUser) mDataSet.get(i);
                onBindMyViewHolder(viewHolder, user);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
    }

    private void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, AVUser user) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.username.setText(user.getUsername());
        holder.signature.setText(user.getString("signature"));
        Picasso.with(mContext).load(user.getString("profilePhotoUrl")).into(holder.profilePhoto);
    }

    @Override
    public int getItemCount() {
        if (mFooterView != null) {
            mItemCount = mDataSet.size() + 1;

            return mItemCount;
        }
        mItemCount = mDataSet.size();

        return mItemCount;
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public CircleImageView profilePhoto;

        public TextView signature;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.text_username);
            profilePhoto = (CircleImageView) itemView.findViewById(R.id.img_profile_photo);
            signature = (TextView) itemView.findViewById(R.id.text_signature);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterView != null && position == mItemCount - 1) {
            return TYPE_FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }
}
