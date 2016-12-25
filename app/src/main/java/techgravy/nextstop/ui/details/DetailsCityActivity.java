package techgravy.nextstop.ui.details;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.transitions.ReflowText;

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
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.placeNameTextView)
    AppCompatTextView mPlaceNameTextView;
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

        Glide.with(DetailsCityActivity.this)
                .load(mPlaces.photos().get(0))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .priority(Priority.IMMEDIATE)
                .into(new BitmapImageViewTarget(mPlaceImageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(bitmap, glideAnimation);
                        Palette.from(bitmap).generate(palette -> {
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
                            if (vibrant != null) {
                                mCollapsingToolbar.setContentScrim(new ColorDrawable(vibrant.getRgb()));
                            }
                            if (mutedDark != null) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(mutedDark.getRgb());
                            }
                        });
                    }
                });

        mPlaceImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mPlaceImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
        setupToolBar();
    }

    private void setupToolBar() {
        mPlaceNameTextView.setText(mPlaces.place());
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(
                    List<String> sharedElementNames,
                    List<View> sharedElements,
                    List<View> sharedElementSnapshots) {
                ReflowText.setupReflow(getIntent(), mPlaceNameTextView);
            }

            @Override
            public void onSharedElementEnd(
                    List<String> sharedElementNames,
                    List<View> sharedElements,
                    List<View> sharedElementSnapshots) {
                ReflowText.setupReflow(
                        new ReflowText.ReflowableTextView(mPlaceNameTextView));
            }
        });
        setSupportActionBar(mToolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResultAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
    }

    public final static String RESULT_EXTRA_PLACES_ID = "RESULT_EXTRA_PLACES_ID";

    void setResultAndFinish() {
        final Intent resultData = new Intent();
        resultData.putExtra(RESULT_EXTRA_PLACES_ID, mPlaces.place_id());
        setResult(RESULT_OK, resultData);
        finishAfterTransition();
    }
}
