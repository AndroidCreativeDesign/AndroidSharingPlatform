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
 * 用户参与的idea 列表  Adapter
 */
public class OtherUserJoinListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private View mHeaderView;
    private View mFooterView;

    public OtherUserJoinListAdapter(Context context, List<AVObject> data) {
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case Constants.TYPE_DEFAULT:
                View view = mLayoutInflater.inflate(R.layout.item_join_default, viewGroup, false);
                return new DefaultViewHolder(view);

            case Constants.TYPE_ASSOCIATION:
                return new AssociationViewHolder(mLayoutInflater.inflate(R.layout.item_other_association, viewGroup, false));

            case Constants.TYPE_RECRUITMENT:
                return new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_other_recruit, viewGroup, false));

            case Constants.TYPE_CROWDFUNDING:
                return new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_other_crowdfunding, viewGroup, false));
            case Constants.TYPE_STUDENT_UNION_DEPARTMENT:
                return new DefaultViewHolder(mLayoutInflater.inflate(R.layout.item_other_department, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        AVObject object = mDataSet.get(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
        switch (object.getInt("type")) {

            case Constants.TYPE_DEFAULT:
                onBindDefaultViewHolder(viewHolder, i, object);
                break;

            case Constants.TYPE_ASSOCIATION:
                onBindAssociationViewHolder(viewHolder, i, object);

                break;
            case Constants.TYPE_RECRUITMENT:
                onBindRecruitmentViewHolder(viewHolder, i, object);
                break;

            case Constants.TYPE_CROWDFUNDING:
                onBindCrowdfundingViewHolder(viewHolder, i, object);
                break;
            case Constants.TYPE_STUDENT_UNION_DEPARTMENT:
                onBindDepartmentViewHolder(viewHolder, i, object);
                break;
        }
    }

    private void onBindCrowdfundingViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof CrowdfundingViewHolder) {
            CrowdfundingViewHolder holder = (CrowdfundingViewHolder) viewHolder;
            holder.title.setText(object.getAVObject("idea").getString("title"));
        }
    }

    private void onBindRecruitmentViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof RecruitmentViewHolder) {
            RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
            holder.title.setText(object.getAVObject("idea").getString("title"));
        }
    }

    private void onBindAssociationViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof AssociationViewHolder) {
            final AssociationViewHolder holder = (AssociationViewHolder) viewHolder;
            holder.associationName.setText(object.getAVObject("association").getString("name"));
        }
    }

    private void onBindDefaultViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof DefaultViewHolder) {
            DefaultViewHolder holder = (DefaultViewHolder) viewHolder;
            AVObject idea = object.getAVObject("idea");
            holder.title.setText(idea.getString("title"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
            holder.startTime.setText(format.format(idea.getDate("startDate")));
            holder.endTime.setText(format.format(idea.getDate("endDate")));
            holder.joinNum.setText(idea.getInt("joinNum") + "");
            holder.address.setText(idea.getString(idea.getString("address")));
        }
    }

    private void onBindDepartmentViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        DepartmentViewHolder holder = (DepartmentViewHolder) viewHolder;
        AVObject department = object.getAVObject(Constants.COLUMN_DEPARTMENT);
        holder.college.setText(department.getAVObject("student_union").getString("name"));
        holder.department.setText(department.getString("name"));
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

    public static class DefaultViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView startTime;
        public TextView endTime;
        public TextView joinNum;
        public TextView address;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            joinNum = (TextView) itemView.findViewById(R.id.text_current_join_num);
            startTime = (TextView) itemView.findViewById(R.id.text_start_date);
            endTime = (TextView) itemView.findViewById(R.id.text_end_date);
            address = (TextView) itemView.findViewById(R.id.text_address);
        }
    }

    public static class AssociationViewHolder extends RecyclerView.ViewHolder {

        public TextView associationName;

        public AssociationViewHolder(View itemView) {
            super(itemView);
            associationName = (TextView) itemView.findViewById(R.id.text_association_name);
        }
    }

    public static class RecruitmentViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public RecruitmentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
        }
    }

    public static class CrowdfundingViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public CrowdfundingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
        }
    }

    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {

        public TextView college;
        public TextView department;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            college = (TextView) itemView.findViewById(R.id.text_college);
            department = (TextView) itemView.findViewById(R.id.text_department);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getInt("type");
    }
}
