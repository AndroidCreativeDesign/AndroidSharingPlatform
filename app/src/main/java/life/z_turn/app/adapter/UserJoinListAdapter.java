package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                View view = mLayoutInflater.inflate(R.layout.item_join_default, viewGroup, false);
                return new MyViewHolder(view);

            case Constants.TYPE_ASSOCIATION:
                return new AssociationViewHolder(mLayoutInflater.inflate(R.layout.item_join_association, viewGroup, false));

            case Constants.TYPE_RECRUITMENT:
                return new RecruitmentViewHolder(mLayoutInflater.inflate(R.layout.item_join_recruit, viewGroup, false));

            case Constants.TYPE_CROWDFUNDING:
                return new CrowdfundingViewHolder(mLayoutInflater.inflate(R.layout.item_join_crowdfunding, viewGroup, false));
            case Constants.TYPE_STUDENT_UNION_DEPARTMENT:
                return new DepartmentViewHolder(mLayoutInflater.inflate(R.layout.item_join_department, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        AVObject object = mDataSet.get(i);
        int viewType = object.getInt("type");

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
                onBindMyViewHolder(viewHolder, i, object);
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
            AVObject crowdfunding = object.getAVObject(Constants.COLUMN_IDEA);
            holder.title.setText(crowdfunding.getString("title"));
        }
    }

    private void onBindRecruitmentViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof RecruitmentViewHolder) {
            RecruitmentViewHolder holder = (RecruitmentViewHolder) viewHolder;
            AVObject recruitment = object.getAVObject(Constants.COLUMN_IDEA);
            holder.title.setText(recruitment.getString("title"));
        }
    }

    private void onBindAssociationViewHolder(RecyclerView.ViewHolder viewHolder, final int i, AVObject object) {
        if (viewHolder instanceof AssociationViewHolder) {
            final AssociationViewHolder holder = (AssociationViewHolder) viewHolder;
            AVObject association = object.getAVObject("association");
            holder.associationName.setText(association.getString("name"));
            holder.btnMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemBtnMemberClick(i);
                    }
                }
            });
            holder.btnRecentActivities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemBtnRecentActivitiesClick(i);
                    }
                }
            });
        }
    }

    private void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            AVObject idea = object.getAVObject("idea");
            holder.title.setText(idea.getString("title"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
            holder.startDate.setText(format.format(idea.getDate("startDate")));
            holder.endDate.setText(format.format(idea.getDate("endDate")));
            holder.joinNum.setText(idea.getInt("joinNum") + "");
            holder.address.setText(idea.getString(idea.getString("address")));
        }
    }

    private void onBindDepartmentViewHolder(RecyclerView.ViewHolder viewHolder, int i, AVObject object) {
        DepartmentViewHolder holder = (DepartmentViewHolder) viewHolder;
        AVObject department = object.getAVObject(Constants.COLUMN_DEPARTMENT);
        holder.departmentName.setText(department.getString("name"));
    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);

        void onItemBtnMemberClick(int pos);

        void onItemBtnRecentActivitiesClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public TextView joinNum;
        public TextView address;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            startDate = (TextView) itemView.findViewById(R.id.text_start_date);
            endDate = (TextView) itemView.findViewById(R.id.text_end_date);
            joinNum = (TextView) itemView.findViewById(R.id.text_current_join_num);
            address = (TextView) itemView.findViewById(R.id.text_address);
        }
    }

    public static class AssociationViewHolder extends RecyclerView.ViewHolder {

        public TextView associationName;
        public Button btnMember;
        public Button btnRecentActivities;

        public AssociationViewHolder(View itemView) {
            super(itemView);
            associationName = (TextView) itemView.findViewById(R.id.text_association_name);
            btnMember = (Button) itemView.findViewById(R.id.btn_member);
            btnRecentActivities = (Button) itemView.findViewById(R.id.btn_recent_activities);
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

        public TextView collegeName;
        public TextView departmentName;
        public TextView applyNum;
        public TextView writtenExaminationDate;
        public TextView writtenExaminationAddress;
        public TextView interviewDate;
        public TextView interviewAddress;
        public TextView other;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            collegeName = (TextView) itemView.findViewById(R.id.text_college);
            departmentName = (TextView) itemView.findViewById(R.id.text_department_name);
            applyNum = (TextView) itemView.findViewById(R.id.text_apply_num);
            writtenExaminationDate = (TextView) itemView.findViewById(R.id.text_written_examination_date);
            writtenExaminationAddress = (TextView) itemView.findViewById(R.id.text_written_examination_address);
            interviewDate = (TextView) itemView.findViewById(R.id.text_interview_date);
            interviewAddress = (TextView) itemView.findViewById(R.id.text_interview_address);
            other = (TextView) itemView.findViewById(R.id.text_other);

        }
    }


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getInt("type");
    }
}
