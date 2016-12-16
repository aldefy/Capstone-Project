package techgravy.nextstop.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;

public class OnboardingFragment extends Fragment {

    @BindView(R.id.appNameTextView)
    TextView appNameTextView;
    @BindView(R.id.descriptionText)
    TextView descriptionText;
    @BindView(R.id.subDescriptionText)
    TextView subDescriptionText;
    @BindColor(R.color.medium_spring_bud)
    int spring;
    @BindView(R.id.skipBtn)
    TextView skipBtn;
    @BindView(R.id.btnLayout)
    LinearLayout btnLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    private void initViews() {
        Spanny spanny = new Spanny(getString(R.string.desc_on_boarding), new ForegroundColorSpan(spring))
                .append(getString(R.string.desc_on_boarding_append));
        descriptionText.setText(spanny);

    }

}
