package techgravy.nextstop.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;
import com.bumptech.glide.Glide;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;

/**
 * Created by aditlal on 13/12/16.
 */

public class GettingStartedFragment extends Fragment {

    @BindView(R.id.containerImageView)
    ImageView containerImageView;
    @BindView(R.id.filteringView)
    View filteringView;
    @BindView(R.id.appNameTextView)
    TextView appNameTextView;
    @BindView(R.id.descriptionText)
    TextView descriptionText;
    @BindView(R.id.subDescriptionText)
    TextView subDescriptionText;
    @BindColor(R.color.coral)
    int coral;
    @BindColor(R.color.sandstorm)
    int sandstorm;
    @BindColor(R.color.yellow_green)
    int yellowGreen;
    private int page = -1;
    private Spanny spanny;
    private static final String BUNDLE_TAG = "page_no";


    public static GettingStartedFragment newInstance(int arg) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_TAG, arg);
        GettingStartedFragment fragment = new GettingStartedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, rootView);
        initPage();
        return rootView;
    }

    private void initPage() {

        Bundle bundle = getArguments();
        if (bundle == null)
            page = 0;
        else
            page = bundle.getInt(BUNDLE_TAG);

        switch (page) {
            case 0:
                Glide.with(getActivity()).load(R.drawable.landing_1).into(containerImageView);
                spanny = new Spanny(getString(R.string.desc_1))
                        .append(" " + getString(R.string.desc_1_append_1), new ForegroundColorSpan(sandstorm));
                descriptionText.setText(spanny);
                subDescriptionText.setText(getString(R.string.sub_desc_1));
                break;
            case 1:
                Glide.with(getActivity()).load(R.drawable.landing_2).into(containerImageView);
                spanny = new Spanny(getString(R.string.desc_2))
                        .append(getString(R.string.desc_2_append_1), new ForegroundColorSpan(coral))
                        .append(getString(R.string.desc_2_append_2));
                descriptionText.setText(spanny);
                subDescriptionText.setText(getString(R.string.sub_desc_2));
                break;
            case 2:
                Glide.with(getActivity()).load(R.drawable.landing_3).into(containerImageView);
                spanny = new Spanny(getString(R.string.desc_3))
                        .append(" "+getString(R.string.desc_3_append_1), new ForegroundColorSpan(yellowGreen))
                        .append(getString(R.string.desc_3_append_2));
                descriptionText.setText(spanny);
                subDescriptionText.setText(getString(R.string.sub_desc_3));
                break;
            default:
                Glide.with(getActivity()).load(R.drawable.landing_1).into(containerImageView);
                spanny = new Spanny(getString(R.string.desc_1))
                        .append(" " + getString(R.string.desc_1_append_1), new ForegroundColorSpan(sandstorm));
                descriptionText.setText(spanny);
                subDescriptionText.setText(getString(R.string.sub_desc_1));
                break;
        }

    }
}
