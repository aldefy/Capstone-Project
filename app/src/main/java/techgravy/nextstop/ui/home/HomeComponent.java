package techgravy.nextstop.ui.home;

import dagger.Component;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 24/12/16.
 */
@CustomScope
@Component(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity activity);

    SharedPrefManager prefManager();
}
