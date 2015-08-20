package life.z_turn.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.utils.DisplayUtil;


public class TopicViewPagerAdapter extends PagerAdapter {

    private List<AVObject> mDataSet;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ImageView> imageViews;
    private final int mDisplayWidthPixels;
    private float mScale;
    private OnItemClickListener mListener;


    public TopicViewPagerAdapter(Context context, List<AVObject> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        this.mLayoutInflater = LayoutInflater.from(context);
        mDisplayWidthPixels = DisplayUtil.getDisplayWidthPixels(context);
        mScale = DisplayUtil.getDisplayDensity(context);
   /*     imageViews = new ArrayList<>();
        for (int i = 0; i< 4;i++){
            View view = mLayoutInflater.inflate(R.layout.item_slider, null, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageResource(R.drawable.test_img);
        }
        imageViews.add(new ImageView(context));
        imageViews.add(new ImageView(context));
        imageViews.add(new ImageView(context));
        imageViews.add(new ImageView(context));*/
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        AVObject object = mDataSet.get(position);
        View view = mLayoutInflater.inflate(R.layout.item_slider, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
/*        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = 200;
        layoutParams.height = 111;
        imageView.setLayoutParams(layoutParams);*/
//        imageView.setImageResource(R.drawable.test_img);
        Picasso.with(mContext).load(object.getString("imgUrl")).into(imageView);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

        return view;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
