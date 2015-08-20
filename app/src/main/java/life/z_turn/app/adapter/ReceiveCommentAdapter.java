package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.List;

import life.z_turn.app.R;


public class ReceiveCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mListener;
    private List<AVObject> mDataSet;
    private LayoutInflater mInflater;
    private Context mContext;

    public ReceiveCommentAdapter(Context context, List<AVObject> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DiscoverViewHolder(mInflater.inflate(R.layout.item_receive_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        DiscoverViewHolder viewHolder = (DiscoverViewHolder) holder;
        AVObject object = mDataSet.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder, position);
                }
            }
        });
        AVUser user = object.getAVUser("user");
        viewHolder.content.setText(user.getUsername() + "回复你：" + object.getString("content"));
        viewHolder.time.setText(object.getCreatedAt().toString());
    }


    @Override
    public int getItemCount() {

        return mDataSet.size();
    }


    static class DiscoverViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView time;

        public DiscoverViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.text_reply_content);
            time = (TextView) itemView.findViewById(R.id.text_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}