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

/**
 * 用户发布的idea列表   Adapter
 */
public class UserPublishListAdapter extends RecyclerView.Adapter<UserPublishListAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;

    public UserPublishListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public UserPublishListAdapter(Context context, List<AVObject> data) {
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
    public UserPublishListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_publish, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserPublishListAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });

        AVObject idea = mDataSet.get(i);
        viewHolder.titleText.setText(idea.getString("title"));
        viewHolder.joinNumText.setText(idea.getString("joinNum"));
        viewHolder.startDateText.setText(idea.getString("startDate"));
        viewHolder.endDateText.setText(idea.getString("endDate"));
        viewHolder.addressText.setText(idea.getString("address"));
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onEditBtnClick(viewHolder, i);
                }
            }
        });

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

        void onEditBtnClick(MyViewHolder viewHolder, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView joinNumText;
        public TextView startDateText;
        public TextView endDateText;
        public TextView addressText;
        public Button editBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.tv_title);
            joinNumText = (TextView) itemView.findViewById(R.id.tv_join_num);
            startDateText = (TextView) itemView.findViewById(R.id.tv_start_date);
            endDateText = (TextView) itemView.findViewById(R.id.tv_end_date);
            addressText = (TextView) itemView.findViewById(R.id.tv_address);
            editBtn = (Button) itemView.findViewById(R.id.btn_edit);

        }
    }


}
