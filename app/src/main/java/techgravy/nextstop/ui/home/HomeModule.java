package techgravy.nextstop.ui.home;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 24/12/16.
 */
@Module
public class HomeModule {
    private final HomeContract.View mView;

    public HomeModule(HomeContract.View view) {
        this.mView = view;
    }

    @Provides
    @CustomScope
    public HomeContract.View provideView(){
        return mView;
    }
}
