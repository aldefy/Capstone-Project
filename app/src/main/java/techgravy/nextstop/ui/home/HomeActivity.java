package techgravy.nextstop.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import techgravy.nextstop.R;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.search.SearchActivity;
import techgravy.nextstop.utils.AnimUtils;
import techgravy.nextstop.utils.SimpleDividerItemDecoration;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeContract.View {

    private static final int RC_SEARCH = 0;
    private static final String TAG = "HOME";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.content_home)
    RelativeLayout contentHome;
    @Inject
    HomePresenter mHomePresenter;
    @BindView(R.id.placesRecyclerView)
    RecyclerView mPlacesRecyclerView;
    private HomeAdapter mHomeAdapter;
    private SharedPrefManager sharedPrefManager;
    private List<Places> mPlacesList;
    CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        setupViews();
    }

    private void setupViews() {
        setupToolbar();
        showFab();
        mCompositeSubscription = new CompositeSubscription();
        Timber.tag(TAG);
        DaggerHomeComponent.builder()
                .homeModule(new HomeModule(HomeActivity.this))
                .build().inject(HomeActivity.this);
        View headerView = navView.getHeaderView(0);
        Subscription fabClickSub = RxView.clicks(fab).subscribe(aVoid -> {

            MaterialStyledDialog dialog = new MaterialStyledDialog(HomeActivity.this)
                    .setTitle(getString(R.string.sort))
                    .setHeaderColor(R.color.primary)
                    .setScrollable(true)
                    .withDialogAnimation(true, Duration.FAST)
                    .setCancelable(true)
                    .setNegative("Sort", (dialog1, which) -> dialog1.dismiss())
                    .setPositive("Cancel", (dialog1, which) -> dialog1.dismiss())
                    .setDescription("Sort according to :")
                    .build();
            dialog.show();
        });
        mCompositeSubscription.add(fabClickSub);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ((TextView) headerView.findViewById(R.id.nameTextView)).setText(sharedPrefManager.getUserFullName());
        Glide.with(HomeActivity.this).load(sharedPrefManager.getAvatarUrl()).into((CircleImageView) headerView.findViewById(R.id.avatarImageView));
        navView.setNavigationItemSelectedListener(this);
        initDataFetch();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        View t = toolbar.getChildAt(0);
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

    private void showFab() {
        fab.setAlpha(0f);
        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.setTranslationY(fab.getHeight() / 2);
        fab.animate()
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mPlacesRecyclerView.setLayoutManager(linearLayoutManager);
        mPlacesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        mHomeAdapter = new HomeAdapter(HomeActivity.this, mPlacesList);
        mPlacesRecyclerView.setAdapter(mHomeAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                        getString(R.string.transition_search_back)).toBundle();
                startActivityForResult(new Intent(this, SearchActivity.class), RC_SEARCH, options);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void attachData(List<Places> placesList) {
        Timber.d(placesList.toString());
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
}
