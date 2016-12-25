package techgravy.nextstop;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.NSApplication;


@Module
public class AppModule {

    private NSApplication mApplication;

    public AppModule(NSApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return mApplication;
    }


}