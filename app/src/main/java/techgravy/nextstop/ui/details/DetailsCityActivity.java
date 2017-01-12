package techgravy.nextstop.ui.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.NSApplication;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 25/12/16.
 */

public class DetailsCityActivity extends AppCompatActivity implements DetailsContract.View {

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
    @BindView(R.id.tv_overview)
    TextView mTvOverview;
    @BindView(R.id.rv_poi)
    RecyclerView mRvPOI;
    @BindView(R.id.scrollContent)
    NestedScrollView mScrollContent;
    @BindView(R.id.iv_weather_icon)
    ImageView mIvWeatherIcon;
    @BindView(R.id.tv_weather)
    TextView mTvWeather;
    @BindView(R.id.weather_layout)
    LinearLayout mWeatherLayout;
    @BindView(R.id.rv_tags)
    RecyclerView mRvTags;
    private Places mPlaces;
    public final static String EXTRA_PLACE = "EXTRA_PLACE";
    private List<String> mTagsList;
    private TagRVAdapter mTagRVAdapter;

    @Inject
    DetailsPresenter mDetailsPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_city);
        ButterKnife.bind(this);
        DaggerDetailsComponent.builder()
                .netComponent(NSApplication.getInstance().getmNetComponent())
                .detailsModule(new DetailsModule(DetailsCityActivity.this))
                .build().inject(DetailsCityActivity.this);
        mTagsList = new ArrayList<>();
        mTagRVAdapter = new TagRVAdapter(DetailsCityActivity.this, mTagsList);
        initViews();
    }

    private void initViews() {
        mPlaceNameTextView.setTransitionName("toolbarTitle");
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PLACE)) {
            mPlaces = getIntent().getParcelableExtra(EXTRA_PLACE);
            postponeEnterTransition();
        } else {
            finish();
        }
        setupToolBar();
        mRvTags.setLayoutManager(new LinearLayoutManager(DetailsCityActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRvTags.setAdapter(mTagRVAdapter);
        mDetailsPresenter.computePlaceTags(mPlaces);

        mTvOverview.setText(mPlaces.desc());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
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
                            Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                            if (vibrant != null) {
                                mCollapsingToolbar.setContentScrim(new ColorDrawable(vibrant.getRgb()));
                            } else {
                                mCollapsingToolbar.setContentScrim(new ColorDrawable(ContextCompat.getColor(DetailsCityActivity.this, R.color.accent_70)));
                            }

                            if (darkVibrant != null) {
                                window.setStatusBarColor(darkVibrant.getRgb());
                            } else {
                                window.setStatusBarColor(ContextCompat.getColor(DetailsCityActivity.this, R.color.accent));
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
    }


    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        mPlaceNameTextView.setText(mPlaces.place() + " | " + mPlaces.tag());
        mPlaceNameTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mPlaceNameTextView.setSelected(true);
        mPlaceNameTextView.requestFocus();
        mCollapsingToolbar.setTitle(mPlaces.place());
        mToolbar.setBackground(new ColorDrawable(Color.TRANSPARENT));
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
        resultData.putExtra(RESULT_EXTRA_PLACES_ID, mPlaces.hashCode());
        setResult(RESULT_OK, resultData);
        finishAfterTransition();
    }

    @Override
    public void loadPlaceTags(List<String> placeTags) {
        mTagsList.addAll(placeTags);
        mTagRVAdapter.notifyDataSetChanged();
    }
}