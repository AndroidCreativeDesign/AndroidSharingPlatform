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

        if (viewType == 1) {
            return new AssociationViewHolder(mLayoutInflater.inflate(R.layout.item_join_association, viewGroup, false));

        }

        View view = mLayoutInflater.inflate(R.layout.item_join, viewGroup, false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        AVObject dream = mDataSet.get(i).getAVObject("idea");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
        if (viewHolder instanceof MyViewHolder) {
             MyViewHolder holder = (MyViewHolder) viewHolder;



            holder.title.setText(dream.getString("title"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);


            holder.startTime.setText(format.format(dream.getDate("startDate")));
            holder.endTime.setText(format.format(dream.getDate("endDate")));


            holder.join.setText(dream.getInt("joinNum") + "");

            holder.address.setText(dream.getString(dream.getString("address")));
        }
        if (viewHolder instanceof AssociationViewHolder) {
            final AssociationViewHolder holder = (AssociationViewHolder) viewHolder;
            holder.associationNameText.setText(dream.getString("title"));
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


    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getAVObject("idea").getInt("type");
    }
}
