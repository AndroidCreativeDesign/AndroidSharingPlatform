package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.model.InfoBean;

/**
 * Created by daixiaodong on 15/8/4.
 */
public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mListener;
    private List<InfoBean> mDataSet;
    private LayoutInflater mInflater;
    private Context mContext;

    public InfoAdapter(Context context, List<InfoBean> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new InfoViewHolder(mInflater.inflate(R.layout.item_publish_info, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        InfoViewHolder viewHolder = (InfoViewHolder) holder;
        InfoBean info = mDataSet.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder, position);
                }
            }
        });
        viewHolder.property.setText(info.getProperty());
        if (info.getValue() == null) {
            viewHolder.value.setText("未填写");
        } else {
            viewHolder.value.setText(info.getValue());
        }


    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {

        public TextView property;
        public TextView value;

        public InfoViewHolder(View itemView) {
            super(itemView);
            property = (TextView) itemView.findViewById(R.id.text_property);
            value = (TextView) itemView.findViewById(R.id.text_value);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
