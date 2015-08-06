package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.daixiaodong.myapp.R;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;


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
    public void onBindViewHolder(PhotosAdapter.PhotoViewHolder holder, int position) {
        AVObject photo = mDataSet.get(position);
        Picasso.with(mContext).load(photo.getString("url")).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.img_photo);
        }
    }
}
