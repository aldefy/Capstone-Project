package techgravy.nextstop.ui.home;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import techgravy.nextstop.ui.home.model.SearchResponse;
import techgravy.nextstop.ui.home.model.SearchResults;
import timber.log.Timber;

/**
 * Created by aditlal on 26/12/16.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private CompositeDisposable mCompositeDisposable;
    private SearchContract.View mView;
    public Retrofit retrofit;

    @Inject
    public SearchPresenter(Retrofit retrofit, SearchContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        this.retrofit = retrofit;
    }

    @Override
    public void queryForString(String query) {
        mView.showProgress();
        retrofit.create(SearchApiInterface.class).textSearch(query)
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
                        mView.hideProgress();
                        mView.showResults(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideProgress();
                        mView.searchError(e.getMessage());
                        Timber.tag("SearchResults").e(e, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag("SearchResults").d("onComplete Called");
                    }
                });

    }

    public void onStop() {
        mCompositeDisposable.dispose();
    }
}
