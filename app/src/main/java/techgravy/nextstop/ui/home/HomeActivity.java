package techgravy.nextstop.ui.home;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mancj.slideup.SlideUp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import techgravy.nextstop.R;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.ui.details.DetailsCityActivity;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.search.SearchActivity;
import techgravy.nextstop.ui.transitions.ReflowText;
import techgravy.nextstop.utils.AnimUtils;
import techgravy.nextstop.utils.SimpleDividerItemDecoration;
import timber.log.Timber;

import static techgravy.nextstop.R.id.imageView;


public class HomeActivity extends AppCompatActivity
        implements HomeContract.View, HomeAdapter.PlaceAdapterClickInterface {

    private static final int RC_SEARCH = 0;
    private static final String TAG = "HOME";
    private static final int REQUEST_PLACE = 523; //Request code , random
    @Nullable
    @BindView(android.R.id.empty)
    ProgressBar mLoading;
    @Nullable
    @BindView(R.id.no_connection)
    ImageView noConnection;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable
    @BindView(R.id.placesRecyclerView)
    RecyclerView mPlacesRecyclerView;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.dimLayout)
    FrameLayout mDimLayout;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(imageView)
    ImageView mImageView;
    @BindView(R.id.slideView)
    LinearLayout mSlideView;
    @BindView(R.id.content_slide_up_view)
    RelativeLayout mContentSlideUpView;
    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @Inject
    HomePresenter mHomePresenter;
    private HomeAdapter mHomeAdapter;
    private SharedPrefManager sharedPrefManager;
    private List<Places> mPlacesList;
    private LinearLayoutManager mLinearLayoutManager;
    private CompositeSubscription mCompositeSubscription;
    private boolean connected = true;
    private boolean monitoringConnectivity = false;
    private SlideUp mSlideUp;
    private float pixelDensity;

    private RecyclerView.OnScrollListener toolbarElevation = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // we want the grid to scroll over the top of the toolbar but for the toolbar items
            // to be clickable when visible. To achieve this we play games with elevation. The
            // toolbar is laid out in front of the grid but when we scroll, we lower it's elevation
            // to allow the content to pass in front (and reset when scrolled to top of the grid)
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLinearLayoutManager.findFirstVisibleItemPosition() == 0
                    && mLinearLayoutManager.findViewByPosition(0).getTop() == recyclerView.getPaddingTop()
                    && mToolbar.getTranslationZ() != 0) {
                // at top, reset elevation
                mToolbar.setTranslationZ(0f);
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                    && mToolbar.getTranslationZ() != -1f) {
                // grid scrolled, lower toolbar to allow content to pass in front
                mToolbar.setTranslationZ(-1f);
            }
        }
    };
    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            connected = true;
            if (mHomeAdapter.getItemCount() != 0) return;
            runOnUiThread(() -> {
                noConnection.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                showFab();
                //mHomePresenter.fetchListOfPlaces();
            });
        }

        @Override
        public void onLost(Network network) {
            connected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        pixelDensity = getResources().getDisplayMetrics().density;
        Timber.tag(TAG);
        DaggerHomeComponent.builder()
                .homeModule(new HomeModule(HomeActivity.this))
                .build().inject(HomeActivity.this);
        setupViews();
    }

    int x = 0, y = 0, hypotenuse;

    private void setupViews() {
        setupToolbar();
        showFab();

        mCompositeSubscription = new CompositeSubscription();
        x = mSlideView.getRight();
        y = mSlideView.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        hypotenuse = (int) Math.hypot(mSlideView.getWidth(), mSlideView.getHeight());

        mSlideUp = new SlideUp(mSlideView);
        mSlideUp.hideImmediately();
        Subscription fabClickSub = RxView.clicks(mFab).subscribe(aVoid -> {
            mSlideUp.animateIn();
            mFab.hide();
        });
        mSlideUp.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float percent) {
                mDimLayout.setAlpha(1 - (percent / 100));
            }

            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE) {
                    mFab.show();
                }
            }
        });
        mCompositeSubscription.add(fabClickSub);
        initDataFetch();
        checkEmptyState();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        View t = mToolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;

            // fade in and space out the title.  Animating the letterSpacing performs horribly so
            // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
            title.setAlpha(0f);
            title.setScaleX(0.8f);

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(50)
                    .setDuration(200)
                    .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(this));
        }


    }

    void checkEmptyState() {
        if (mHomeAdapter.getItemCount() == 0) {
            // if grid is empty check whether we're loading or if no filters are selected
            mLoading.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
        }
    }

    private void showFab() {
        mFab.setAlpha(0f);
        mFab.setScaleX(0f);
        mFab.setScaleY(0f);
        mFab.setTranslationY(mFab.getHeight() / 2);
        mFab.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(300L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this))
                .start();
    }

    private void initDataFetch() {
        mHomePresenter.fetchListOfPlaces();
        mPlacesList = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mPlacesRecyclerView.setLayoutManager(mLinearLayoutManager);
        //    mPlacesRecyclerView.addOnScrollListener(toolbarElevation);
        mPlacesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        mHomeAdapter = new HomeAdapter(HomeActivity.this, mPlacesList);
        mPlacesRecyclerView.setAdapter(mHomeAdapter);

      /*  //Added to trigger a Scoll Behaviour similar to ScrollAwareFABBehavior
        mPlacesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && mFab.isShown()) {
                    mFab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (mSlideUp != null) {
            if (mSlideUp.isVisible()) {
                mSlideUp.hideImmediately();
                Animator anim = ViewAnimationUtils.createCircularReveal(mSlideView, x, y, hypotenuse, 0);
                anim.setDuration(400);
                anim.start();
            } else super.onBackPressed();
        } else super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_search:
                View searchMenuView = mToolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                        getString(R.string.transition_search_back)).toBundle();
                startActivityForResult(new Intent(this, SearchActivity.class), RC_SEARCH, options);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void attachData(List<Places> placesList) {
        for (Places places : placesList)
            Timber.d(places.place());
        mPlacesList.addAll(placesList);
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void dataError(Throwable e) {
        Timber.e(e, e.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }

    private void checkConnectivity() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!connected) {
            if (mLoading != null) {
                mLoading.setVisibility(View.GONE);
            }
            final AnimatedVectorDrawable avd =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.avd_no_connection);
            if (noConnection != null && avd != null) {
                noConnection.setImageDrawable(avd);
                mFab.hide();
                avd.start();
            }

            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
                    connectivityCallback);
            monitoringConnectivity = true;
        } else {
            if (noConnection != null) {
                noConnection.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode != RESULT_OK
                || !data.hasExtra(DetailsCityActivity.RESULT_EXTRA_PLACES_ID)) return;

        // When reentering, if the shared element is no longer on screen (e.g. after an
        // orientation change) then scroll it into view.
        final int hashcode = data.getIntExtra(DetailsCityActivity.RESULT_EXTRA_PLACES_ID, -1);
        if (hashcode != -1                                             // returning from a shot
                && mHomeAdapter.getItemCount() > 0) {                           // adapter populated {    // view not attached
            final int position = mHomeAdapter.getItemPosition(hashcode);
            if (position == RecyclerView.NO_POSITION) return;

            // delay the transition until our shared element is on-screen i.e. has been laid out
            postponeEnterTransition();
            mPlacesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int l, int t, int r, int b,
                                           int oL, int oT, int oR, int oB) {
                    mPlacesRecyclerView.removeOnLayoutChangeListener(this);
                    startPostponedEnterTransition();
                }
            });
            mPlacesRecyclerView.scrollToPosition(position);
            mToolbar.setTranslationZ(-1f);

        }
    }

    @Override
    public void itemClicked(Places places, View imageView, View textView) {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, DetailsCityActivity.class);
        intent.putExtra(DetailsCityActivity.EXTRA_PLACE, places);
        ReflowText.addExtras(
                intent,
                new ReflowText.ReflowableTextView((TextView) textView));
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, Pair.create(imageView, imageView.getTransitionName()),
                        Pair.create(textView, textView.getTransitionName()));
        startActivityForResult(intent, REQUEST_PLACE, options.toBundle());
    }
}
