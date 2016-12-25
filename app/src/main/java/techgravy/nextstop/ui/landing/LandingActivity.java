package techgravy.nextstop.ui.landing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beltaief.reactivefb.actions.ReactiveLogin;
import com.beltaief.reactivefb.requests.ReactiveRequest;
import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import techgravy.nextstop.R;
import techgravy.nextstop.base.PagerAdapter;
import techgravy.nextstop.data.FacebookProfile;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.data.User;
import techgravy.nextstop.utils.ParallaxPagerTransformer;
import timber.log.Timber;

import static timber.log.Timber.tag;

/**
 * Created by aditlal
 */

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.pagerContainer)
    ViewPager pagerContainer;
    @BindView(R.id.radioBtnOne)
    RadioButton radioBtnOne;
    @BindView(R.id.radioBtnTwo)
    RadioButton radioBtnTwo;
    @BindView(R.id.radioBtnThree)
    RadioButton radioBtnThree;
    @BindView(R.id.viewPagerCountDots)
    RadioGroup viewPagerCountDots;
    @BindView(R.id.startBtn)
    Button startBtn;
    private CompositeSubscription compositeSubscription;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private final String TAG = LandingActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference database;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                getProfile();
            } else {
                // User is signed out
                tag(TAG).d("onAuthStateChanged: signed_out");
            }
        };
        database = FirebaseDatabase.getInstance().getReference();
        initViews();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(LandingActivity.this);
        progressDialog.setMessage("Authenticating..");
        fragmentList = new ArrayList<>();
        GettingStartedFragment page1 = GettingStartedFragment.newInstance(0);
        GettingStartedFragment page2 = GettingStartedFragment.newInstance(1);
        GettingStartedFragment page3 = GettingStartedFragment.newInstance(2);
        fragmentList.add(page1);
        fragmentList.add(page2);
        fragmentList.add(page3);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        pagerContainer.setAdapter(pagerAdapter);
        pagerContainer.setPageTransformer(false, new ParallaxPagerTransformer());
        pageSwitcher(4);
        pagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPagerCountDots.check(viewPagerCountDots.getChildAt(position).getId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        Subscription startBtnSub = RxView.clicks(startBtn).subscribe(view -> LandingActivity.this.authWithFacebook());
        compositeSubscription.add(startBtnSub);
    }

    private void authWithFacebook() {
        ReactiveLogin.login(LandingActivity.this).subscribe(new MaybeObserver<LoginResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            public void onSuccess(LoginResult value) {
                progressDialog.show();
                handleFacebookAccessToken(value.getAccessToken());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                tag(TAG).e(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReactiveLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        tag(TAG).d("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    tag(TAG).d("signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        tag(TAG).w("signInWithCredential", task.getException());
                        Toast.makeText(LandingActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void getProfile() {

        String fields = "picture.width(600).height(600),name,first_name";

        ReactiveRequest
                .getMe(fields) // get Profile
                .map(this::parseProfile) // parse json
                .subscribe(new SingleObserver<FacebookProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(FacebookProfile value) {
                        writeNewUser(FirebaseAuth.getInstance().getCurrentUser(), value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private FacebookProfile parseProfile(GraphResponse response) throws IOException {
        String data = null;
        try {
            data = response.getJSONObject().has("data") ?
                    response.getJSONObject().get("data").toString() :
                    response.getJSONObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return LoganSquare.parse(data, FacebookProfile.class);
    }

    private void writeNewUser(FirebaseUser firebaseUser, FacebookProfile fbProfile) {
        User user = new User("https://graph.facebook.com/" + fbProfile.getId() + "/picture?type=large", fbProfile.getName(), new ArrayList<>());
        database.child("users").child(firebaseUser.getUid()).setValue(user);
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getApplicationContext());
        prefManager.setAvatarUrl(user.getPhotoUrl());
        prefManager.setUUID(firebaseUser.getUid());
        prefManager.setUserFullName(fbProfile.getName());
        progressDialog.dismiss();
        Timber.tag(TAG).d("onAuthStateChanged: signed_in:" + firebaseUser.getUid());
        startActivity(new Intent(LandingActivity.this, BuildPersonaActivity.class));
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    Timer timer;
    int page = 0;

    public void pageSwitcher(int seconds) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(() -> {
                if (page > 3) {
                    timer.cancel();
                } else {
                    pagerContainer.setCurrentItem(page++);
                }
            });

        }
    }
}
