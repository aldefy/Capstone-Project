package techgravy.nextstop;

import android.app.Application;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.github.simonpercic.oklog3.OkLogInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import timber.log.Timber;

/**
 * Created by aditlal on 24/11/16.
 */

@Module
public class NetModule {

    private String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    // Dagger will only look for methods annotated with @Provides

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        // create an instance of OkLogInterceptor using a builder()
        OkLogInterceptor okLogInterceptor =
                OkLogInterceptor.builder()
                        .setLogInterceptor(url -> {
                            Timber.tag("API_TAG").d(url);
                            return true;
                        }).withAllLogData().build();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(okLogInterceptor);        // add OkLogInterceptor to OkHttpClient's application interceptors
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

}
