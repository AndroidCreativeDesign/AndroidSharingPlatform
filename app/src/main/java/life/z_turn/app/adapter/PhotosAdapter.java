package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import life.z_turn.app.R;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;


    public PhotosAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDataSet = data;
    }

    @Override
    public PhotosAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(mLayoutInflater.inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotosAdapter.PhotoViewHolder holder, final int position) {
        AVObject photo = mDataSet.get(position);
        Picasso.with(mContext).load(photo.getString("url")).into(holder.photo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(holder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface  OnItemClickListener{
        void onItemClick(View itemView, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.img_photo);
        }
    }
}
