package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import cn.daixiaodong.myapp.R;

/**
 *  用户参与的idea 列表  Adapter
 */
public class UserJoinListAdapter extends RecyclerView.Adapter<UserJoinListAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;

    public UserJoinListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public UserJoinListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setDataSet(List<AVObject> data) {
        this.mDataSet = data;
        notifyDataSetChanged();
    }

    public void addData(List<AVObject> data) {
        this.mDataSet.addAll(0, data);
        this.notifyItemInserted(1);
    }

    @Override
    public UserJoinListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_join, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserJoinListAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });

        AVObject dream = mDataSet.get(i).getAVObject("idea");
        viewHolder.title.setText(dream.getString("title"));
       /* SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
        Date startDate = dream.getDate("startDate");
        Date endDate = dream.getDate("endDate");
        viewHolder.time.setText(format.format(dream.getDate("startDate")));
        viewHolder.join.setText(dream.getInt("joinNum"));
        viewHolder.address.setText(dream.getString(dream.getString("address")));
        Date currentDate = new Date();
        long days = (startDate.getTime() - currentDate.getTime()) / (24 * 60 * 60 * 1000);
        if(days < 0 && dream.getInt("status") == 0){
            viewHolder.status.setText("已结束");
        }else if( days < 1){
            viewHolder.status.setText("即将开始");
        }else if(days > 1){
            viewHolder.status.setText("距离开始还有"+days+"天");
        }*/
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(MyViewHolder viewHolder, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView time;
        public TextView join;
        public TextView address;
        public TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_tv_title);
            time = (TextView) itemView.findViewById(R.id.id_tv_time);
            join = (TextView) itemView.findViewById(R.id.id_tv_join);
            address = (TextView) itemView.findViewById(R.id.id_tv_address);
            status = (TextView) itemView.findViewById(R.id.id_tv_status);
        }
    }


}
