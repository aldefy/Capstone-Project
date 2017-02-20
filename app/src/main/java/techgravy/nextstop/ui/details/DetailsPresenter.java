package techgravy.nextstop.ui.details;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.github.simonpercic.oklog3.OkLogInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import techgravy.nextstop.BuildConfig;
import techgravy.nextstop.ui.details.model.AutoJson_POI;
import techgravy.nextstop.ui.details.model.POI;
import techgravy.nextstop.ui.details.model.WeatherModel;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.utils.Constants;
import techgravy.nextstop.utils.FirebaseJSONUtil;
import timber.log.Timber;

import static techgravy.nextstop.utils.Constants.ADVENTURE;
import static techgravy.nextstop.utils.Constants.CITYSCAPE;
import static techgravy.nextstop.utils.Constants.ENTERTAINMENT;
import static techgravy.nextstop.utils.Constants.FAMILY;
import static techgravy.nextstop.utils.Constants.FOOD;
import static techgravy.nextstop.utils.Constants.LANDMARKS;
import static techgravy.nextstop.utils.Constants.LUXURY;
import static techgravy.nextstop.utils.Constants.METRIC;
import static techgravy.nextstop.utils.Constants.NIGHT_LIFE;
import static techgravy.nextstop.utils.Constants.SHOPPING;
import static techgravy.nextstop.utils.Constants.SPORTS;
import static techgravy.nextstop.utils.Constants.SUN;
import static techgravy.nextstop.utils.Constants.TRUE;

/**
 * Created by aditlal on 12/01/17 - 12.
 */

class DetailsPresenter implements DetailsContract.Presenter {
    public static final String WEATHER_TAG = "WeatherTAG";
    public static final String POI = "POI";
    private CompositeDisposable mCompositeDisposable;
    private DetailsContract.View mView;
    public Retrofit retrofit;
    private DatabaseReference mDatabaseReference;


    @Inject
    DetailsPresenter(Retrofit retrofit, DetailsContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        this.retrofit = retrofit;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void computePlaceTags(Places places) {
        Observable.just(places)
                .subscribeOn(Schedulers.computation())
                .map(places1 -> {
                    List<String> returnTags = new ArrayList<>();
                    if (places.family().equals(TRUE))
                        returnTags.add(FAMILY);
                    if (places.entertainment().equals(TRUE))
                        returnTags.add(ENTERTAINMENT);
                    if (places.sun().equals(TRUE))
                        returnTags.add(SUN);
                    if (places.adventure().equals(TRUE))
                        returnTags.add(ADVENTURE);

                    if (places.landmarks().equals(TRUE))
                        returnTags.add(LANDMARKS);
                    if (places.sports().equals(TRUE))
                        returnTags.add(SPORTS);
                    if (places.nightlife().equals(TRUE))
                        returnTags.add(NIGHT_LIFE);
                    if (places.food().equals(TRUE))
                        returnTags.add(FOOD);
                    if (places.shopping().equals(TRUE))
                        returnTags.add(SHOPPING);
                    if (places.luxury().equals(TRUE))
                        returnTags.add(LUXURY);
                    if (places.cityscape().equals(TRUE))
                        returnTags.add(CITYSCAPE);
                    if (places.history().equals(TRUE))
                        returnTags.add(Constants.HISTORY);
                    if (places.picturesque().equals(TRUE))
                        returnTags.add(Constants.PICTURESQUE);
                    if (places.beaches().equals(TRUE))
                        returnTags.add(Constants.BEACHES);
                    if (places.island().equals(TRUE))
                        returnTags.add(Constants.ISLAND);
                    if (places.romantic().equals(TRUE))
                        returnTags.add(Constants.ROMANTIC);
                    if (places.art().equals(TRUE))
                        returnTags.add(Constants.ART);
                    Collections.sort(returnTags);
                    return returnTags;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<String> value) {
                        mView.loadPlaceTags(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getWeather(String cityName) {
        OkLogInterceptor okLogInterceptor =
                OkLogInterceptor.builder()
                        .setLogInterceptor(url -> {
                            Timber.tag(WEATHER_TAG).d(url);
                            return true;
                        }).withAllLogData().build();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(okLogInterceptor);
        Retrofit weatherRetrofit = new Retrofit.Builder()
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.WEATHER_URL)
                .client(client.build())
                .build();
        weatherRetrofit.create(WeatherApi.class)
                .getTodaysForecast(cityName, BuildConfig.WEATHER_API_KEY, METRIC)
                .subscribeOn(Schedulers.io())
                .map(weatherResponse ->
                        WeatherModel.builder()
                                .weatherID(weatherResponse.getList().get(0).getWeather().get(0).getId())
                                .icon(weatherResponse.getList().get(0).getWeather().get(0).getIcon())
                                .temp(weatherResponse.getList().get(0).getMain().getTemp())
                                .build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(WeatherModel value) {
                        mView.loadWeather(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public void getPOI(String cityName) {
        Query query = mDatabaseReference.child(Constants.PLACE_DETAILS).child(cityName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        List<POI> results = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            POI poi = FirebaseJSONUtil.deserialize(snapshot, AutoJson_POI.class);
                            Timber.tag(POI).d(poi.place());
                            results.add(poi);
                        }
                        mView.loadSearchResults(results);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
/*
        retrofit.create(GoogleApiInterface.class).textSearch("Attractions in " + cityName)
                .subscribeOn(Schedulers.io())
                .map(SearchResponse::getResultsList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SearchResults>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<SearchResults> results) {
                        for (SearchResults result : results)
                            Timber.tag("SearchResults").d("result = " + result.toString());
                        mView.loadSearchResults(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.tag("SearchResults").e(e, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag("SearchResults").d("onComplete Called");
                    }
                });
*/

    }


    @Override
    public void onStop() {
        mCompositeDisposable.dispose();
    }
}
