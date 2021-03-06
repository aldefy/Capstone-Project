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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mancj.slideup.SlideUp;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import techgravy.nextstop.R;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.ui.about.AboutActivity;
import techgravy.nextstop.ui.details.DetailsCityActivity;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.landing.PersonaTags;
import techgravy.nextstop.ui.search.SearchActivity;
import techgravy.nextstop.utils.AnimUtils;
import techgravy.nextstop.utils.Constants;
import techgravy.nextstop.utils.ItemOffsetDecoration;
import techgravy.nextstop.utils.SimpleDividerItemDecoration;
import timber.log.Timber;


public class HomeActivity extends AppCompatActivity
        implements HomeContract.View, HomeAdapter.PlaceAdapterClickInterface, FilterAdapter.PersonaInterface {

    private static final int RC_SEARCH = 0;
    private static final String TAG = "HOME"; // cant be saved to strings , context prob
    private static final int REQUEST_PLACE = 523; //Request code , random
    @BindView(android.R.id.empty)
    ProgressBar mLoading;
    @BindView(R.id.no_connection)
    ImageView noConnection;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.placesRecyclerView)
    RecyclerView mPlacesRecyclerView;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.dimLayout)
    FrameLayout mDimLayout;
    @BindView(R.id.slideView)
    RelativeLayout mSlideView;
    @BindView(R.id.content_slide_up_view)
    RelativeLayout mContentSlideUpView;
    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @Inject
    HomePresenter mHomePresenter;
    @BindView(R.id.filterHeading)
    TextView mFilterHeading;
    @BindView(R.id.actionsRecyclerView)
    RecyclerView mActionsRecyclerView;
    @BindView(R.id.btnApply)
    Button mBtnApply;
    @BindView(R.id.btnSlideDown)
    Button mBtnSlideDown;
    @BindColor(R.color.drawer_dark_bg)
    int drawerBackground;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.accent)
    int accent;
    int x = 0, y = 0, hypotenuse;
    private HomeAdapter mHomeAdapter;
    private SharedPrefManager sharedPrefManager;
    private List<Places> mPlacesList;
    private CompositeSubscription mCompositeSubscription;
    private boolean connected = true;
    private boolean monitoringConnectivity = false;
    private SlideUp mSlideUp;
    private float pixelDensity;
    private HashMap<String, Boolean> userPersonaMap;
    private List<String> personaList;
    private List<PersonaTags> tagsList;
    private FilterAdapter mFilterAdapter;
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
            });
        }

        @Override
        public void onLost(Network network) {
            connected = false;
        }
    };
    private Animation alphaAnimation;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }*/
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        pixelDensity = getResources().getDisplayMetrics().density;
        Timber.tag(TAG);
        DaggerHomeComponent.builder()
                .homeModule(new HomeModule(HomeActivity.this, SharedPrefManager.getInstance(getApplicationContext())))
                .build().inject(HomeActivity.this);
        setupViews();
    }

    private void setupViews() {
        setupToolbar();
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName(sharedPrefManager.getUserFullName()).withIcon(sharedPrefManager.getAvatarUrl()).withIdentifier(100))
                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        item1.withTextColor(white);
        item1.withSelectedColor(drawerBackground);
        item1.withSelectedTextColor(accent);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);
        item2.withTextColor(white);
        item2.withSelectedTextColor(accent);
        item2.withSelectedColor(drawerBackground);


        new DrawerBuilder()
                .withToolbar(mToolbar)
                .withSliderBackgroundColor(drawerBackground)
                .addDrawerItems(item1,
                        new DividerDrawerItem(),
                        item2)
                .withAccountHeader(headerResult)
                .withActivity(this).build();
        showFab();
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        mCompositeSubscription = new CompositeSubscription();
        setupSlideView();
        mActionsRecyclerView.setVisibility(View.GONE);
        initDataFetch();
        checkEmptyState();
    }


    private void setupSlideView() {
        tagsList = new ArrayList<>();
        userPersonaMap = new HashMap<>();
        personaList = new ArrayList<>();
        tagsList.add(new PersonaTags(Constants.FAMILY));
        tagsList.add(new PersonaTags(Constants.ENTERTAINMENT));
        tagsList.add(new PersonaTags(Constants.SHOPPING));
        tagsList.add(new PersonaTags(Constants.SUN));
        tagsList.add(new PersonaTags(Constants.ADVENTURE));
        tagsList.add(new PersonaTags(Constants.LANDMARKS));
        tagsList.add(new PersonaTags(Constants.WATER_SPORTS));
        tagsList.add(new PersonaTags(Constants.SPORTS));
        tagsList.add(new PersonaTags(Constants.NIGHT_LIFE));
        tagsList.add(new PersonaTags(Constants.FOOD));
        tagsList.add(new PersonaTags(Constants.CITYSCAPE));
        tagsList.add(new PersonaTags(Constants.HISTORY));
        tagsList.add(new PersonaTags(Constants.PICTURESQUE));
        tagsList.add(new PersonaTags(Constants.BEACHES));
        tagsList.add(new PersonaTags(Constants.ISLAND));
        tagsList.add(new PersonaTags(Constants.ROMANTIC));
        tagsList.add(new PersonaTags(Constants.ART));
        tagsList.add(new PersonaTags(Constants.LUXURY));

        x = mSlideView.getRight();
        y = mSlideView.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        hypotenuse = (int) Math.hypot(mSlideView.getWidth(), mSlideView.getHeight());

        mSlideUp = new SlideUp(mSlideView);
        mSlideUp.hideImmediately();
        Subscription fabClickSub = RxView.clicks(mFab).subscribe(aVoid -> {
            mActionsRecyclerView.setVisibility(View.VISIBLE);
            mSlideUp.animateIn();
            mFab.hide();
        });
        mCompositeSubscription.add(fabClickSub);

        Subscription applyFilterSub = RxView.clicks(mBtnApply).subscribe(aVoid -> {
            mHomePresenter.filterPlaces(userPersonaMap);
            mSlideUp.animateOut();
        });
        mCompositeSubscription.add(applyFilterSub);


        mSlideUp.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float percent) {
                mDimLayout.setAlpha(1 - (percent / 100));
            }

            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE) {
                    mActionsRecyclerView.setVisibility(View.GONE);
                    mFab.show();
                }
            }
        });

        mFilterAdapter = new FilterAdapter(HomeActivity.this, tagsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 3);
        mActionsRecyclerView.setLayoutManager(gridLayoutManager);
        mActionsRecyclerView.addItemDecoration(new ItemOffsetDecoration(1));
        mActionsRecyclerView.setAdapter(mFilterAdapter);
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
        mHomePresenter.fetchUserPersonaTags();
        mPlacesList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mPlacesRecyclerView.setLayoutManager(linearLayoutManager);
        //    mPlacesRecyclerView.addOnScrollListener(toolbarElevation);
        mPlacesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        mHomeAdapter = new HomeAdapter(HomeActivity.this, mPlacesList);
        mPlacesRecyclerView.setAdapter(mHomeAdapter);

        //Added to trigger a Scroll Behaviour similar to ScrollAwareFABBehavior
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
        });
    }

    @Override
    public void onBackPressed() {
        if (mSlideUp != null) {
            if (mSlideUp.isVisible()) {
                Animator anim = ViewAnimationUtils.createCircularReveal(mSlideView, x, y, hypotenuse, 0);
                anim.setDuration(400);
                anim.start();
                mDimLayout.setAlpha(0);
                mSlideUp.hideImmediately();
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
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void attachUserPlaces(List<Places> placesList) {
        for (Places places : placesList) {
            Timber.tag(TAG).d(places.printNames());
        }
        mPlacesList.addAll(placesList);
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void attachPlaces(List<Places> placesList) {
        mPlacesList.clear();
        mPlacesList.addAll(placesList);
        mHomeAdapter.notifyDataSetChanged();
    }


    @Override
    public void filterPersonaTags(List<PersonaTags> personaTagsList) {
        for (PersonaTags tags : personaTagsList) {
            userPersonaMap.put(tags.getActionName(), tags.isChecked());
        }
        if (userPersonaMap.size() > 0) {
            List<String> personTagNames = new ArrayList<>(userPersonaMap.keySet());
            for (String personaName : personTagNames) { //User selections
                for (PersonaTags tag : tagsList) { //Entire list
                    if (tag.getActionName().equalsIgnoreCase(personaName)) {
                        tag.setChecked(true);
                    }
                }
            }
            mFilterAdapter.notifyDataSetChanged();
        }
        mHomePresenter.fetchUserPlaces();

    }

    @Override
    public void showProgress() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mLoading.setVisibility(View.GONE);
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
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, Pair.create(imageView, imageView.getTransitionName()),
                        Pair.create(textView, textView.getTransitionName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(intent, REQUEST_PLACE, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_PLACE);
        }
    }

    @Override
    public void filterItem(PersonaTags personaTags) {
        if (userPersonaMap.containsKey(personaTags.getActionName())) {
            if (!personaTags.isChecked())
                userPersonaMap.remove(personaTags.getActionName());        //Removal of personaTag object from map
            else
                userPersonaMap.put(personaTags.getActionName(), personaTags.isChecked());  //Add of personaTag object
        } else {
            userPersonaMap.put(personaTags.getActionName(), personaTags.isChecked());  //Add of personaTag object
        }
    }
}
