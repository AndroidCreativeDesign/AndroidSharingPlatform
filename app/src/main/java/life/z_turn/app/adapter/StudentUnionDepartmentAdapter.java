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

/**
 * Created by daixiaodong on 15/8/4.
 */
public class StudentUnionDepartmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mListener;
    private List<AVObject> mDataSet;
    private LayoutInflater mInflater;
    private Context mContext;

    public StudentUnionDepartmentAdapter(Context context, List<AVObject> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DepartmentViewHolder(mInflater.inflate(R.layout.item_department, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        DepartmentViewHolder viewHolder = (DepartmentViewHolder) holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder, position);
                }
            }
        });

        AVObject object = mDataSet.get(position);
        viewHolder.name.setText(object.getString("name"));


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {

        public TextView name;


        public DepartmentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
