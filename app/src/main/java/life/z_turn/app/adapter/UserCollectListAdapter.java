package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;

/**
 * 收藏列表 Adapter
 */
public class UserCollectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public UserCollectListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getAVObject("idea").getInt("type");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.TYPE_DEFAULT:
                viewHolder = new DefaultViewHolder(mLayoutInflater.inflate(R.layout.item_idea_default, viewGroup, false));
                break;
            case Constants.TYPE_CROWDFUNDING:
                viewHolder = new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_collect_crowdfunding, viewGroup, false));
                break;
            case Constants.TYPE_RECRUITMENT:
                viewHolder = new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_collect_recruitment, viewGroup, false));
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, position,getItemViewType(position));
                }
            }
        });


        switch (getItemViewType(position)) {
            case Constants.TYPE_DEFAULT:
                onBindDefaultViewHolder(viewHolder, position);
                break;
            case Constants.TYPE_RECRUITMENT:
                onBindRecruitmentViewHolder(viewHolder, position);
                break;
            case Constants.TYPE_CROWDFUNDING:
                onBindCrowdfundingViewHolder(viewHolder, position);
                break;
        }
    }

    private void onBindCrowdfundingViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CrowdfundingViewHolder holder = (CrowdfundingViewHolder) viewHolder;
        AVObject idea = mDataSet.get(position).getAVObject("idea");
        holder.titleText.setText(idea.getString("title"));
    }

    private void onBindRecruitmentViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
        AVObject idea = mDataSet.get(position).getAVObject("idea");
        holder.titleText.setText(idea.getString("title"));
    }

    private void onBindDefaultViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DefaultViewHolder holder = (DefaultViewHolder) viewHolder;
        AVObject idea = mDataSet.get(position).getAVObject("idea");
        holder.title.setText(idea.getString("title"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
        holder.startTime.setText(format.format(idea.getDate("startDate")));
        holder.endTime.setText(format.format(idea.getDate("endDate")));
        holder.join.setText(idea.getInt("joinNum") + "");
        holder.address.setText(idea.getString(idea.getString("address")));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView startTime;
        public TextView endTime;

        public TextView join;
        public TextView address;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            startTime = (TextView) itemView.findViewById(R.id.id_tv_start_time);
            endTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            join = (TextView) itemView.findViewById(R.id.id_tv_join);
            address = (TextView) itemView.findViewById(R.id.id_tv_address);
        }
    }


    public static class RecruitmentViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;

        public RecruitmentViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public static class CrowdfundingViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;

        public CrowdfundingViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos,int viewType);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
        }
    }


}
