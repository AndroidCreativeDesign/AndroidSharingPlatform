package life.z_turn.app.adapter.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by daixiaodong on 15/7/9.
 */
public class CommonViewHolder {

    private  View mConvertView;
    private int mPosition;
    private SparseArray<View> mViews;
    private Context mContext;

    public CommonViewHolder(Context context, int position, ViewGroup parent, int resId) {
        this.mPosition = position;
        mViews = new SparseArray<>();
        this.mContext  = context;
        mConvertView = LayoutInflater.from(context).inflate(resId, parent, false);
        mConvertView.setTag(this);
    }


    public static CommonViewHolder get(Context context, int position, View convertView, ViewGroup parent, int resId) {
        if (convertView == null) {
            return new CommonViewHolder(context,position,parent,resId);
        }else{
            CommonViewHolder viewHolder = (CommonViewHolder)convertView.getTag();
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int resId){
        View view = mViews.get(resId);
        if(view == null){
            view = mConvertView.findViewById(resId);
            mViews.put(resId,view);
        }

        return (T)view;
    }


    public CommonViewHolder setText(int resId, String text){
        TextView tv = getView(resId);
        tv.setText(text);
        return this;
    }

    public void setImageUrl(int resId, String imgUrl) {
        ImageView iv = getView(resId);
     //   Picasso.with(mContext).load(imgUrl).resize(250,250).into(iv);
    }
}
