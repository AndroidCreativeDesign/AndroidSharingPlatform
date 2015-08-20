package life.z_turn.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import life.z_turn.app.R;


public class ToolbarSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mValuesList;

    public ToolbarSpinnerAdapter(Context mContext, List<String> mValuesList) {
        this.mContext = mContext;
        this.mValuesList = mValuesList;
    }




    @Override
    public int getCount() {
        return mValuesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mValuesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_spinner_dropdown_type, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(R.id.spinner_item_text);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_spinner_type, parent, false);
            view.setTag("NON_DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(R.id.spinner_item_text);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mValuesList.size() ? mValuesList.get(position) : "";
    }
}