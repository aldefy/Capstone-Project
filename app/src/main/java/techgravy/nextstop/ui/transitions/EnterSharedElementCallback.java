package techgravy.nextstop.ui.transitions;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import techgravy.nextstop.R;
import timber.log.Timber;

/**
 * Created by aditlal on 11/01/17 - 11.
 */

public class EnterSharedElementCallback extends SharedElementCallback {
    private static final String TAG = "EnterSECallback";

    private final float mStartTextSize;
    private final float mEndTextSize;
    private final int mStartColor;
    private final int mEndColor;

    public EnterSharedElementCallback(Context context) {
        Resources res = context.getResources();
        mStartTextSize = res.getDimensionPixelSize(R.dimen.text_medium);
        mEndTextSize = res.getDimensionPixelSize(R.dimen.text_big);
        mStartColor = res.getColor(R.color.primary_text);
        mEndColor = res.getColor(R.color.primary_text);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Timber.tag(TAG).i( "=== onSharedElementStart(List<String>, List<View>, List<View>)");
        TextView textView = (TextView) sharedElements.get(0);

        // Setup the TextView's start values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
        textView.setTextColor(mStartColor);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Log.i(TAG, "=== onSharedElementEnd(List<String>, List<View>, List<View>)");
        TextView textView = (TextView) sharedElements.get(0);

        // Record the TextView's old width/height.
        int oldWidth = textView.getMeasuredWidth();
        int oldHeight = textView.getMeasuredHeight();

        // Setup the TextView's end values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEndTextSize);
        textView.setTextColor(mEndColor);

        // Re-measure the TextView (since the text size has changed).
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthSpec, heightSpec);

        // Record the TextView's new width/height.
        int newWidth = textView.getMeasuredWidth();
        int newHeight = textView.getMeasuredHeight();

        // Layout the TextView in the center of its container, accounting for its new width/height.
        int widthDiff = newWidth - oldWidth;
        int heightDiff = newHeight - oldHeight;
        textView.layout(textView.getLeft() - widthDiff / 2, textView.getTop() - heightDiff / 2,
                textView.getRight() + widthDiff / 2, textView.getBottom() + heightDiff / 2);
    }
}
