package techgravy.nextstop.ui.details;

import dagger.Module;
import dagger.Provides;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 12/01/17 - 12.
 */

@Module
public class DetailsModule {
    private final DetailsContract.View mView;

    public DetailsModule(DetailsContract.View view) {
        this.mView = view;
    }

    @Provides
    @CustomScope
    public DetailsContract.View provideView() {
        return mView;
    }

}
