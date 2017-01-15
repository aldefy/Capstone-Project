package techgravy.nextstop.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import techgravy.nextstop.data.User;
import techgravy.nextstop.ui.home.HomeActivity;
import techgravy.nextstop.ui.landing.BuildPersonaActivity;
import techgravy.nextstop.ui.landing.LandingActivity;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aditlal on 16/12/16 - 16.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference database;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        mAuthListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                Timber.tag(TAG).d("onAuthStateChanged: signed_in:" + firebaseUser.getUid());   // User is signed in
                Query query = database.child("users").orderByChild(firebaseUser.getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User user =dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                            if (user.getPersonaTagsList() != null)
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            else
                                startActivity(new Intent(SplashActivity.this, BuildPersonaActivity.class));
                            finishAffinity();
                        }
                        else{ //User does not exist in DB
                            startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                            finishAffinity();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // User is signed out
                        Timber.tag(TAG).d("onAuthStateChanged: signed_out");
                        startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                        finishAffinity();
                    }
                });
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
