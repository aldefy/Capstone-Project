package techgravy.nextstop.ui.home;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 24/12/16.
 */
@Module
public class HomeModule {
    private final HomeContract.View mView;
    private SharedPrefManager mSharedPrefManager;

    public HomeModule(HomeContract.View view,SharedPrefManager prefManager) {
        this.mView = view;
        this.mSharedPrefManager = prefManager;
    }

    @Provides
    @CustomScope
    public HomeContract.View provideView(){
        return mView;
    }

    @Provides
    @CustomScope
    public SharedPrefManager provideSharedPrefManager(){
        return mSharedPrefManager;
    }
}
