package techgravy.nextstop.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;

/**
 * Created by aditlal on 13/12/16.
 */

public class OnBoardingActivity extends AppCompatActivity {


    @BindView(R.id.containerImageView)
    ImageView containerImageView;
    @BindView(R.id.filteringView)
    View filteringView;
    @BindView(R.id.frameContainer)
    FrameLayout frameContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        setupViews();
    }

    private void setupViews() {
        Glide.with(OnBoardingActivity.this).load(R.drawable.onboarding).into(containerImageView);

    }


}
