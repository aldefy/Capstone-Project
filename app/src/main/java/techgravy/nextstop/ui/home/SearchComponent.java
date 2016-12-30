package techgravy.nextstop.ui.home;

import dagger.Component;
import techgravy.nextstop.NetComponent;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 24/12/16.
 */
@CustomScope
@Component(modules = SearchModule.class, dependencies = NetComponent.class)
public interface SearchComponent {
    void inject(SearchActivity activity);
}
