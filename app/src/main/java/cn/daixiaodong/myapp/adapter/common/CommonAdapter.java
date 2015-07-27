package cn.daixiaodong.myapp.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by daixiaodong on 15/7/9.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {


    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected int mLayoutResId;

    public CommonAdapter(Context context, List<T> data, int layoutResId) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    public CommonAdapter(Context context, int layoutResId) {

        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    public void setData(List<T> data){
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder = CommonViewHolder.get(mContext,position,convertView,parent,mLayoutResId);


        convert(viewHolder,getItem(position),position);

        return viewHolder.getConvertView();
    }


    protected abstract void convert(CommonViewHolder viewHolder, T item,int position);


}
