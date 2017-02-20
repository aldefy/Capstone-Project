package techgravy.nextstop.ui.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.NSApplication;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.details.model.POI;
import techgravy.nextstop.ui.details.model.WeatherModel;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.utils.WeatherUtils;
import techgravy.nextstop.utils.logger.Logger;
import timber.log.Timber;

import static techgravy.nextstop.utils.Constants.METRIC;

/**
 * Created by aditlal on 25/12/16.
 */

public class DetailsCityActivity extends AppCompatActivity implements DetailsContract.View {

    public static final String TOOLBAR_TITLE = "toolbarTitle"; // cant be saved to strings , context prob
    public static final String TAG = "DetailsCity";  // cant be saved to strings , context prob
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
    @BindColor(R.color.accent)
    int accent;
    @BindColor(R.color.accent_70)
    int accent70;
    @BindColor(R.color.background_dark)
    int dark;

    private Places mPlaces;
    public final static String EXTRA_PLACE = "EXTRA_PLACE"; // cant be saved to strings , context prob
    public final static String WIDGET_PLACE = "WIDGET_PLACE"; // cant be saved to strings , context prob
    public final static String RESULT_EXTRA_PLACES_ID = "RESULT_EXTRA_PLACES_ID";// cant be saved to strings , context prob
    private List<String> mTagsList;
    private List<POI> mSearchResultsList;
    private TagRVAdapter mTagRVAdapter;
    private DetailsPOIRVAdapter mDetailsPOIRVAdapter;

    @Inject
    DetailsPresenter mDetailsPresenter;
    private boolean shouldScroll;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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
        mSearchResultsList = new ArrayList<>();
        mTagRVAdapter = new TagRVAdapter(DetailsCityActivity.this, mTagsList);
        mDetailsPOIRVAdapter = new DetailsPOIRVAdapter(DetailsCityActivity.this, mSearchResultsList);
        initViews();
    }

    private void initViews() {
        mPlaceNameTextView.setTransitionName(TOOLBAR_TITLE);
        Intent intent = getIntent();
        Logger.t(TAG).printIntent(getIntent());
        if (intent.hasExtra(WIDGET_PLACE)) {
            Timber.tag(TAG).d(getString(R.string.place_load) + ((Places) getIntent().getParcelableExtra(WIDGET_PLACE)).place());
            mPlaces = getIntent().getParcelableExtra(WIDGET_PLACE);
        } else if (intent.hasExtra(EXTRA_PLACE)) {
            mPlaces = getIntent().getParcelableExtra(EXTRA_PLACE);
            postponeEnterTransition();
        } else {
            finish();
        }

        Logger.t(TAG).d(mPlaces.place());
        setupToolBar();
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> shouldScroll = Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange());
        mScrollContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!shouldScroll)
                    mScrollContent.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        mRvTags.setLayoutManager(new LinearLayoutManager(DetailsCityActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRvTags.setAdapter(mTagRVAdapter);
        mRvPOI.setLayoutManager(new LinearLayoutManager(DetailsCityActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRvPOI.setAdapter(mDetailsPOIRVAdapter);
        mDetailsPresenter.computePlaceTags(mPlaces);
        mDetailsPresenter.getWeather(mPlaces.place());
        mDetailsPresenter.getPOI(mPlaces.place());

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
                                mCollapsingToolbar.setContentScrim(new ColorDrawable(accent70));
                            }

                            if (darkVibrant != null) {
                                window.setStatusBarColor(darkVibrant.getRgb());
                            } else {
                                window.setStatusBarColor(accent);
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
    public void loadSearchResults(List<POI> resultsList) {
        mSearchResultsList.addAll(resultsList);
        mDetailsPOIRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadPlaceTags(List<String> placeTags) {
        mTagsList.addAll(placeTags);
        mTagRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadWeather(WeatherModel model) {
        mTvWeather.setText(WeatherUtils.formatTemperature(DetailsCityActivity.this, model.temp(), METRIC));
        mIvWeatherIcon.setImageDrawable(WeatherUtils.getWeatherIconFromWeather(DetailsCityActivity.this, model.weatherID(), WeatherUtils.ICON_PACK_METEOCONCS));
    }


    @Override
    public void onBackPressed() {
        mDetailsPresenter.onStop();
        setResultAndFinish();
    }

    void setResultAndFinish() {
        final Intent resultData = new Intent();
        resultData.putExtra(RESULT_EXTRA_PLACES_ID, mPlaces.hashCode());
        setResult(RESULT_OK, resultData);
        finishAfterTransition();
    }

}
