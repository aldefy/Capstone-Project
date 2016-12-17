package techgravy.nextstop;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import techgravy.nextstop.home.HomeActivity;
import techgravy.nextstop.landing.LandingActivity;
import timber.log.Timber;

/**
 * Created by aditlal on 16/12/16 - 16.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Timber.tag(TAG).d("onAuthStateChanged: signed_in:" + user.getUid());
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finishAffinity();
            } else {
                // User is signed out
                Timber.tag(TAG).d("onAuthStateChanged: signed_out");
                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                finishAffinity();
            }
        };

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
}
