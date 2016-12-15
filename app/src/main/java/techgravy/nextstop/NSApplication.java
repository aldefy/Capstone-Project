package techgravy.nextstop;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;

import net.danlew.android.joda.JodaTimeAndroid;

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
            Facebook helper
         */
        FacebookSdk.sdkInitialize(getApplicationContext());

        /*
            Init NetModule
         */
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule(""))
                .build();
    }

    public NetComponent getmNetComponent() {
        return mNetComponent;
    }
}
