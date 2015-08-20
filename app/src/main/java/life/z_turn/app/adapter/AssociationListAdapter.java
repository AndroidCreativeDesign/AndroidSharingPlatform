package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import life.z_turn.app.R;


public class AssociationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private List<AVObject> mQueryResultSet;
    public boolean isQuery;


    public AssociationListAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void addQueryResultSet(List<AVObject> dataSet) {
        this.mQueryResultSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == -1) {
            View view = mLayoutInflater.inflate(R.layout.item_show_all, viewGroup, false);
            return new showAllViewHolder(view);
        }
        View view = mLayoutInflater.inflate(R.layout.item_association_new, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {

        if (isQuery && i == 0) {
            if (mQueryResultSet.isEmpty()) {
                ((showAllViewHolder) viewHolder).searchResultSize.setText("没有找到相关的协会");
            } else
                ((showAllViewHolder) viewHolder).searchResultSize.setText(mContext.getResources().getString(R.string.search_result, mQueryResultSet.size()));
            return;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (isQuery) {
                        mListener.onItemClick(viewHolder, i - 1);
                    } else {
                        mListener.onItemClick(viewHolder, i);
                    }
                }
            }
        });
        AVObject association;
        if (isQuery) {
            association = mQueryResultSet.get(i - 1);
        } else {
            association = mDataSet.get(i);
        }

        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.associationName.setText(association.getString("name"));
    }

    @Override
    public int getItemCount() {
        if (isQuery) {
            return mQueryResultSet.size() + 1;
        }
        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int pos);

        void onShowAllBtnClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView associationName;
        public TextView memberNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            associationName = (TextView) itemView.findViewById(R.id.text_association_name);
            memberNum = (TextView) itemView.findViewById(R.id.text_member_num);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isQuery && position == 0) {
            return -1;
        } else if (isQuery) {
            return super.getItemViewType(position - 1);
        }
        return super.getItemViewType(position);
    }

    public class showAllViewHolder extends RecyclerView.ViewHolder {
        public TextView searchResultSize;

        public showAllViewHolder(View itemView) {
            super(itemView);
            searchResultSize = (TextView) itemView.findViewById(R.id.text_search_result_size);
            itemView.findViewById(R.id.btn_show_all).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onShowAllBtnClick();
                    }
                }
            });
        }
    }
}
