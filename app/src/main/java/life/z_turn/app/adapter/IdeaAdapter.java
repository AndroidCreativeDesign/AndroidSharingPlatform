package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;

/**
 * 首页所有的idea列表   Adapter
 */
public class IdeaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int HEADER_VIEW = -1;
    private static final int FOOTER_VIEW = -2;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private View mHeaderView;
    private View mFooterView;

    public IdeaAdapter(Context context, List<AVObject> data) {
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void addHeaderView(View view) {
        mHeaderView = view;

    }

    public void addFooterView(View view) {
        mFooterView = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case HEADER_VIEW:
                return new HeaderViewHolder(mHeaderView);
            case FOOTER_VIEW:
                return new FooterViewHolder(mFooterView);
            case Constants.TYPE_CROWDFUNDING:
                return new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_idea_crowdfunding, viewGroup, false));
            case Constants.TYPE_RECRUITMENT:
                return new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_idea_recruitment, viewGroup, false));
            case Constants.TYPE_DEFAULT:
                return new DefaultViewHolder(mLayoutInflater.inflate(R.layout.item_idea_default, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {



        switch (getItemViewType(position)) {
            case HEADER_VIEW:
                return;
            case FOOTER_VIEW:
                return;
            case Constants.TYPE_DEFAULT:
                onBindDefaultViewHolder(holder, position - 1);
                break;
            case Constants.TYPE_RECRUITMENT:
                onBindRecruitmentViewHolder(holder, position - 1);
                break;
            case Constants.TYPE_CROWDFUNDING:
                onBindCrowdfundingViewHolder(holder, position - 1);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder, position - 1);
                }
            }
        });

//        Picasso.with(mContext).load(dream.getString("imgUrl")).into(viewHolder.image);
/*       FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.image.getLayoutParams();

        layoutParams.width = mWidth - (int) (16 * mScale + 0.5f);
        layoutParams.height = (int) ((9 * layoutParams.width) / 16.0f);
        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.image.setLayoutParams(layoutParams);*/

     /*   Log.i("tag", layoutParams.width + "");
        Log.i("tag", layoutParams.height + "");*/
        // Picasso.with(mContext).load(mDataSet.get(i).getString("imgUrl")).resize()
    }

    private void onBindCrowdfundingViewHolder(RecyclerView.ViewHolder holder, int pos) {
        CrowdfundingViewHolder viewHolder = (CrowdfundingViewHolder) holder;
        AVObject object = mDataSet.get(pos);
        viewHolder.title.setText(object.getString(Constants.COLUMN_TITLE));
    }

    private void onBindRecruitmentViewHolder(RecyclerView.ViewHolder holder, int pos) {
        RecruitmentViewHolder viewHolder = (RecruitmentViewHolder) holder;
        AVObject object = mDataSet.get(pos);
        viewHolder.title.setText(object.getString(Constants.COLUMN_TITLE));
    }

    private void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, int pos) {
        DefaultViewHolder viewHolder = (DefaultViewHolder) holder;
        AVObject object = mDataSet.get(pos);
        viewHolder.title.setText(object.getString(Constants.COLUMN_TITLE));
    }

    @Override
    public int getItemCount() {
        if (mHeaderView != null && mFooterView != null && mDataSet.isEmpty()) {
            return mDataSet.size() + 1;
        }
        if (mHeaderView != null && mFooterView != null && !mDataSet.isEmpty()) {
            return mDataSet.size() + 2;
        }
        if (mHeaderView == null && mFooterView != null && !mDataSet.isEmpty()) {
            return mDataSet.size() + 1;
        }
        if (mHeaderView == null && mFooterView != null && mDataSet.isEmpty()) {
            return mDataSet.size();
        }
        if (mHeaderView == null && mFooterView == null) {
            return mDataSet.size();
        }
        if (mHeaderView != null && mFooterView == null) {
            return mDataSet.size() + 1;
        }
        if (mHeaderView == null) {
            return mDataSet.size() + 1;
        }
        return mDataSet.size() + 2;
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public TextView statDate;
        public TextView endDate;
        public TextView tag;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            image = (ImageView) itemView.findViewById(R.id.img);
            statDate = (TextView) itemView.findViewById(R.id.text_start_date);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
            tag = (TextView) itemView.findViewById(R.id.text_tag);

        }
    }


    public class CrowdfundingViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public TextView endDate;
        public TextView tag;

        public CrowdfundingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            image = (ImageView) itemView.findViewById(R.id.img);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
            tag = (TextView) itemView.findViewById(R.id.text_tag);

        }
    }

    public class RecruitmentViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public TextView endDate;
        public TextView tag;

        public RecruitmentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            image = (ImageView) itemView.findViewById(R.id.img);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
            tag = (TextView) itemView.findViewById(R.id.text_tag);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return HEADER_VIEW;
        }
        if (mFooterView != null && position == mDataSet.size() + 1) {
            return FOOTER_VIEW;
        }
        return mDataSet.get(position - 1).getInt(Constants.COLUMN_TYPE);
    }


}
