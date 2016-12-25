package techgravy.nextstop.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 25/12/16.
 */

public class DetailsCityActivity extends AppCompatActivity {

    @BindView(R.id.placeImageView)
    ImageView mPlaceImageView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    private Places mPlaces;
    public final static String EXTRA_PLACE = "EXTRA_PLACE";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_city);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PLACE)) {
            mPlaces = getIntent().getParcelableExtra(EXTRA_PLACE);
            postponeEnterTransition();

        } else {
            finish();
        }
        Glide.with(DetailsCityActivity.this).load(mPlaces.getPhotos().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE).into(mPlaceImageView);
        mPlaceImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mPlaceImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
