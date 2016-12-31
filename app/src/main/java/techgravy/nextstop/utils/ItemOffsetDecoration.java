package techgravy.nextstop.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by aditlal on 31/12/16.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = offset;
        outRect.right = offset;
        outRect.bottom = offset;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = offset;
        }

    }
}