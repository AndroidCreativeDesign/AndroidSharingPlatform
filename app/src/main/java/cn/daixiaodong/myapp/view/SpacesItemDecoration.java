package cn.daixiaodong.myapp.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = mSpace;
//        outRect.right = mSpace;
        outRect.bottom = mSpace;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
            outRect.top = mSpace;
        }
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = mSpace;
            outRect.right = mSpace / 2;
        }
        if (parent.getChildAdapterPosition(view) % 2 == 1) {
            outRect.left = mSpace / 2;
            outRect.right = mSpace;
        }


    }
}