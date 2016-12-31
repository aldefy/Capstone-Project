package techgravy.nextstop.ui.search;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 26/12/16.
 */
@Module
public class SearchModule {
    private final SearchContract.View mView;

    public SearchModule(SearchContract.View view) {
        this.mView = view;
    }

    @Provides
    @CustomScope
    public SearchContract.View provideView() {
        return mView;
    }
}
