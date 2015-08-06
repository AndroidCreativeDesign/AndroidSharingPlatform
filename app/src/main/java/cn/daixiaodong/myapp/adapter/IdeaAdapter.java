package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.daixiaodong.myapp.R;

/**
 *  首页所有的idea列表   Adapter
 */
public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private int mWidth;
    private float mScale;


    public IdeaAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public IdeaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_idea_new, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final IdeaAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });
        AVObject dream = mDataSet.get(i);

        if (dream.getInt("type") == 1) {
            viewHolder.introduce.setVisibility(View.GONE);
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.tagName.setVisibility(View.VISIBLE);
            viewHolder.tagName.setText(dream.getString("typeName"));
        } else {

            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.introduce.setVisibility(View.GONE);
            viewHolder.tagName.setVisibility(View.GONE);
            viewHolder.title.setText(mDataSet.get(i).getString("title"));
            viewHolder.introduce.setText(dream.getString("introduce"));


        }

/*
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.image.getLayoutParams();

        layoutParams.width = mWidth - (int) (16 * mScale + 0.5f);
        layoutParams.height = (int) ((9 * layoutParams.width) / 16.0f);
        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.image.setLayoutParams(layoutParams);*/

     /*   Log.i("tag", layoutParams.width + "");
        Log.i("tag", layoutParams.height + "");*/
        Picasso.with(mContext).load(mDataSet.get(i).getString("imgUrl")).into(viewHolder.image);
        // Picasso.with(mContext).load(mDataSet.get(i).getString("imgUrl")).resize()
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(MyViewHolder viewHolder, int pos);
    }

    public void setImageSize(int width, float scale) {
        this.mWidth = width;
        this.mScale = scale;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public TextView time;
        public TextView tagName;
        public TextView introduce;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_tv_title);
            image = (ImageView) itemView.findViewById(R.id.id_iv_img);
            time = (TextView) itemView.findViewById(R.id.id_tv_time);
            tagName = (TextView) itemView.findViewById(R.id.id_tv_tag_name);
            introduce = (TextView) itemView.findViewById(R.id.id_tv_introduce);

        }
    }


}
