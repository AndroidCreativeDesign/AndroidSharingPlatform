package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;

/**
 * 用户发布的idea列表   Adapter
 */
public class UserPublishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public UserPublishListAdapter(Context context, List<AVObject> data) {
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
            holder.title.setText(idea.getString("title"));
            holder.receive.setText(idea.getDouble("receive") + "");
            holder.completion.setText(idea.getDouble("receive") / idea.getDouble("total") * 100 + "");

        }
    }

    private void onRecruitmentViewHolder(final RecyclerView.ViewHolder viewHolder, final int position, AVObject idea) {
        if (viewHolder instanceof RecruitmentViewHolder) {
            RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
            holder.title.setText(idea.getString("title"));

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onEditBtnClick(viewHolder, position);
                }
            });
            holder.btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onCheckBtnClick(viewHolder, position);

                }
            });
        }
    }

    private void onBindDefaultViewHolder(final RecyclerView.ViewHolder viewHolder, final int position, AVObject idea) {
        if (viewHolder instanceof DefaultViewHolder) {
            DefaultViewHolder holder = (DefaultViewHolder) viewHolder;
            holder.title.setText(idea.getString("title"));
            holder.joinNum.setText(idea.getString("joinNum"));
            holder.startDate.setText(idea.getString("startDate"));
            holder.endDate.setText(idea.getString("endDate"));
            holder.address.setText(idea.getString("address"));
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEditBtnClick(viewHolder, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);

        void onEditBtnClick(RecyclerView.ViewHolder viewHolder, int pos);

        void onCheckBtnClick(RecyclerView.ViewHolder viewHolder, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView joinNum;
        public TextView startDate;
        public TextView endDate;
        public TextView address;
        public Button btnEdit;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            joinNum = (TextView) itemView.findViewById(R.id.text_current_join_num);
            startDate = (TextView) itemView.findViewById(R.id.text_start_date);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
            address = (TextView) itemView.findViewById(R.id.text_address);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);

        }
    }


    public static class RecruitmentViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView joinNum;
        public Button btnCheck;
        public Button btnEdit;

        public RecruitmentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            joinNum = (TextView) itemView.findViewById(R.id.text_current_join_num);
            btnCheck = (Button) itemView.findViewById(R.id.btn_check);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
        }
    }

    public static class CrowdfundingViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView receive;
        public TextView completion;
        public TextView endDate;

        public CrowdfundingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            receive = (TextView) itemView.findViewById(R.id.text_receive);
            completion = (TextView) itemView.findViewById(R.id.text_completion);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getInt("type");
    }
}
