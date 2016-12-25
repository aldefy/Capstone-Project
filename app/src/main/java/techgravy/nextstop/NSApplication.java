package techgravy.nextstop;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.beltaief.reactivefb.ReactiveFB;
import com.beltaief.reactivefb.SimpleFacebookConfiguration;
import com.beltaief.reactivefb.util.PermissionHelper;
import com.facebook.login.DefaultAudience;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import techgravy.nextstop.utils.logger.LoggerTree;
import timber.log.Timber;

/**
 * Created by aditlal on 13/12/16.
 */

public class NSApplication extends MultiDexApplication {
    private NetComponent mNetComponent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupDependencies();
    }

    private void setupDependencies() {

        /*
            Joda Android Init
         */
        JodaTimeAndroid.init(getApplicationContext());

        /*
            Reactive Facebook init
         */
        // define list of permissions
        PermissionHelper[] permissions = new PermissionHelper[]{
                PermissionHelper.USER_ABOUT_ME,
                PermissionHelper.EMAIL,
                PermissionHelper.USER_FRIENDS,
                PermissionHelper.USER_BIRTHDAY,
                PermissionHelper.USER_TAGGED_PLACES,
                PermissionHelper.PUBLISH_ACTION};
        // add permission to a configuration
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(String.valueOf(R.string.facebook_app_id))
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.FRIENDS)
                .setAskForAllPermissionsAtOnce(false)
                .build();
        // init lib
        ReactiveFB.sdkInitialize(this);
        ReactiveFB.setConfiguration(configuration);

        /*
            Init NetModule
         */
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule(""))
                .build();


         /*
          Logging init
         */
        if (BuildConfig.DEBUG) {
            Timber.plant(new LoggerTree());
            Timber.tag("NextStop");
        }

        /*
           Database Firebase
         */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public NetComponent getmNetComponent() {
        return mNetComponent;
    }
}
