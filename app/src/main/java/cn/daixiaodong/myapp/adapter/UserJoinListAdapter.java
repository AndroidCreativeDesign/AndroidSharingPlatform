package cn.daixiaodong.myapp.adapter;

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

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.config.Constants;

/**
 * 用户参与的idea 列表  Adapter
 */
public class UserJoinListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public UserJoinListAdapter(Context context, List<AVObject> data) {
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {

            case Constants.TYPE_DEFAULT:
                View view = mLayoutInflater.inflate(R.layout.item_join, viewGroup, false);
                return new MyViewHolder(view);

            case Constants.TYPE_ASSOCIATION:
                return new AssociationViewHolder(mLayoutInflater.inflate(R.layout.item_join_association, viewGroup, false));

            case Constants.TYPE_RECRUITMENT:
                return new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_recruit, viewGroup, false));

            case Constants.TYPE_CROWDFUNDING:
                return new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_crowdfunding, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        AVObject idea = mDataSet.get(i).getAVObject("idea");
        int viewType = idea.getInt("type");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
        switch (viewType) {

            case Constants.TYPE_DEFAULT:
                onBindMyViewHolder(viewHolder, i, idea);
                break;

            case Constants.TYPE_ASSOCIATION:
                onBindAssociationViewHolder(viewHolder, i, idea);

                break;
            case Constants.TYPE_RECRUITMENT:
                onBindRecruitmentViewHolder(viewHolder, i, idea);
                break;

            case Constants.TYPE_CROWDFUNDING:
                onBindCrowdfundingViewHolder(viewHolder, i, idea);
                break;
        }
    }

    private void onBindCrowdfundingViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject idea) {
        if (viewHolder instanceof CrowdfundingViewHolder) {
            CrowdfundingViewHolder holder = (CrowdfundingViewHolder) viewHolder;
            holder.titleText.setText(idea.getString("title"));
        }
    }

    private void onBindRecruitmentViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject idea) {
        if (viewHolder instanceof RecruitmentViewHolder) {
            RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
            holder.titleText.setText(idea.getString("title"));
        }
    }

    private void onBindAssociationViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject idea) {
        if (viewHolder instanceof AssociationViewHolder) {
            final AssociationViewHolder holder = (AssociationViewHolder) viewHolder;
            holder.associationNameText.setText(idea.getString("title"));
        }
    }

    private void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject idea) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) viewHolder;


            holder.title.setText(idea.getString("title"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);


            holder.startTime.setText(format.format(idea.getDate("startDate")));
            holder.endTime.setText(format.format(idea.getDate("endDate")));


            holder.join.setText(idea.getInt("joinNum") + "");

            holder.address.setText(idea.getString(idea.getString("address")));
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView startTime;
        public TextView endTime;

        public TextView join;
        public TextView address;
        public TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_tv_title);
            startTime = (TextView) itemView.findViewById(R.id.id_tv_start_time);
            endTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            join = (TextView) itemView.findViewById(R.id.id_tv_join);
            address = (TextView) itemView.findViewById(R.id.id_tv_address);
            status = (TextView) itemView.findViewById(R.id.id_tv_status);
        }
    }

    public static class AssociationViewHolder extends RecyclerView.ViewHolder {

        public TextView associationNameText;

        public AssociationViewHolder(View itemView) {
            super(itemView);
            associationNameText = (TextView) itemView.findViewById(R.id.tv_association_name);
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
        return mDataSet.get(position).getAVObject("idea").getInt("type");
    }
}
