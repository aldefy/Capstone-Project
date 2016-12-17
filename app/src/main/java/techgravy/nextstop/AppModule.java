package techgravy.nextstop;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.data.SharedPrefManager;


@Module
public class AppModule {

    NSApplication mApplication;

    public AppModule(NSApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


    @Provides
    @Singleton
        // Application reference must come from AppModule.class
    SharedPrefManager providesSharedPreferences(Application application) {
        return SharedPrefManager.getInstance(application);
    }

}