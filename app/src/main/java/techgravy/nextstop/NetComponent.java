package techgravy.nextstop;


import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import techgravy.nextstop.data.SharedPrefManager;

/**
 * Created by aditlal on 24/11/16.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    Retrofit retrofit();

    SharedPrefManager prefManager();
}
