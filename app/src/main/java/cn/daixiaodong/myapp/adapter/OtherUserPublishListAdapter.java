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
import cn.daixiaodong.myapp.config.Constants;

/**
 * 用户发布的idea列表   Adapter
 */
public class OtherUserPublishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public OtherUserPublishListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.TYPE_DEFAULT:
                viewHolder = new DefaultViewHolder(mLayoutInflater.inflate(R.layout.item_publish, viewGroup, false));
                break;
            case Constants.TYPE_RECRUITMENT:
                viewHolder = new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_publish_recruitment, viewGroup, false));
                break;
            case Constants.TYPE_CROWDFUNDING:
                viewHolder = new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_publish_crowdfunding, viewGroup, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        AVObject idea = mDataSet.get(position);
        int type = idea.getInt("type");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, position);
                }
            }
        });


        switch (type) {
            case Constants.TYPE_DEFAULT:
                onBindDefaultViewHolder(viewHolder, position, idea);
                break;
            case Constants.TYPE_RECRUITMENT:
                onRecruitmentViewHolder(viewHolder, position, idea);
                break;
            case Constants.TYPE_CROWDFUNDING:
                onCrowdfundingViewHolder(viewHolder, position, idea);
                break;
        }


    }

    private void onCrowdfundingViewHolder(RecyclerView.ViewHolder viewHolder, int position, AVObject idea) {
        if (viewHolder instanceof CrowdfundingViewHolder) {
            CrowdfundingViewHolder holder = (CrowdfundingViewHolder) viewHolder;
            holder.titleText.setText(idea.getString("title"));
        }
    }

    private void onRecruitmentViewHolder(RecyclerView.ViewHolder viewHolder, int position, AVObject idea) {
        if (viewHolder instanceof RecruitmentViewHolder) {
            RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
            holder.titleText.setText(idea.getString("title"));
        }
    }

    private void onBindDefaultViewHolder(final RecyclerView.ViewHolder viewHolder, final int position, AVObject idea) {
        if (viewHolder instanceof DefaultViewHolder) {
            DefaultViewHolder holder = (DefaultViewHolder) viewHolder;
            holder.titleText.setText(idea.getString("title"));
            holder.joinNumText.setText(idea.getString("joinNum"));
            holder.startDateText.setText(idea.getString("startDate"));
            holder.endDateText.setText(idea.getString("endDate"));
            holder.addressText.setText(idea.getString("address"));

        }
    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView joinNumText;
        public TextView startDateText;
        public TextView endDateText;
        public TextView addressText;
        public Button editBtn;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.tv_title);
            joinNumText = (TextView) itemView.findViewById(R.id.tv_join_num);
            startDateText = (TextView) itemView.findViewById(R.id.tv_start_date);
            endDateText = (TextView) itemView.findViewById(R.id.tv_end_date);
            addressText = (TextView) itemView.findViewById(R.id.tv_address);
            editBtn = (Button) itemView.findViewById(R.id.btn_edit);

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


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getInt("type");
    }
}
