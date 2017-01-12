package techgravy.nextstop.ui.details;

import dagger.Component;
import techgravy.nextstop.NetComponent;
import techgravy.nextstop.utils.CustomScope;

/**
 * Created by aditlal on 12/01/17 - 12.
 */
@CustomScope
@Component(modules = DetailsModule.class, dependencies = NetComponent.class)
public interface DetailsComponent {
    void inject(DetailsCityActivity activity);
}
