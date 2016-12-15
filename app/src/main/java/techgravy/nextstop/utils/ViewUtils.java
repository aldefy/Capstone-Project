package techgravy.nextstop.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;



public class ViewUtils {

    public static int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

}
