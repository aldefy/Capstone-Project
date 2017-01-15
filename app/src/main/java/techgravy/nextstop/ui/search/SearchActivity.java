/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package techgravy.nextstop.ui.search;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.TransitionRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import techgravy.nextstop.NSApplication;
import techgravy.nextstop.R;
import techgravy.nextstop.data.model.SearchResults;
import techgravy.nextstop.ui.transitions.CircularReveal;
import techgravy.nextstop.utils.ImeUtils;
import techgravy.nextstop.utils.ItemOffsetDecoration;
import techgravy.nextstop.utils.TransitionsUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends Activity implements SearchContract.View {

    @BindView(R.id.scrim)
    View mScrim;
    @BindView(R.id.search_background)
    View mSearchBackground;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.searchback)
    ImageButton mSearchback;
    @BindView(R.id.searchback_container)
    FrameLayout mSearchbackContainer;
    @BindView(R.id.search_toolbar)
    FrameLayout mSearchToolbar;
    @BindView(android.R.id.empty)
    ProgressBar mEmpty;
    @BindView(R.id.search_results)
    RecyclerView mSearchRecyclerView;
    @BindView(R.id.results_scrim)
    View mResultsScrim;
    @BindView(R.id.results_container)
    FrameLayout mResultsContainer;
    @BindView(R.id.container)
    FrameLayout mContainer;

    private SearchResultsAdapter mSearchAdapter;
    private List<SearchResults> mResultsList;
    private TextView noResults;
    private SparseArray<Transition> transitions = new SparseArray<>();

    @Inject
    SearchPresenter mSearchPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupRecyclerView();
        setupSearchView();
        DaggerSearchComponent.builder()
                .netComponent(NSApplication.getInstance().getmNetComponent())
                .searchModule(new SearchModule(SearchActivity.this))
                .build().inject(SearchActivity.this);
        setExitSharedElementCallback(TransitionsUtil.createSharedElementReenterCallback(SearchActivity.this));
        setupTransitions();
        onNewIntent(getIntent());
    }

    private void setupRecyclerView() {
        mResultsList = new ArrayList<>();
        mSearchAdapter = new SearchResultsAdapter(SearchActivity.this, mResultsList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mSearchRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mSearchRecyclerView.addItemDecoration(new ItemOffsetDecoration(1));
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(SearchManager.QUERY)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (!TextUtils.isEmpty(query)) {
                mSearchView.setQuery(query, false);
                searchFor(query);
            }
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    protected void onPause() {
        // needed to suppress the default window animation when closing the activity
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onEnterAnimationComplete() {
        // focus the search view once the enter transition finishes
        mSearchView.requestFocus();
        ImeUtils.showIme(mSearchView);
    }

    @OnClick({R.id.scrim, R.id.searchback})
    protected void dismiss() {
        // clear the background else the touch ripple moves with the translation which looks bad
        mSearchback.setBackground(null);
        finishAfterTransition();
    }

    void clearResults() {
        TransitionManager.beginDelayedTransition(mContainer, getTransition(R.transition.auto));
        mResultsList.clear();
        mSearchAdapter.notifyDataSetChanged();
        mSearchRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mResultsScrim.setVisibility(View.GONE);
        setNoResultsVisibility(View.GONE);
    }

    void setNoResultsVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                noResults.setOnClickListener(v -> {
                    mSearchView.setQuery("", false);
                    mSearchView.requestFocus();
                    ImeUtils.showIme(mSearchView);
                });
            }
            String message = String.format(
                    getString(R.string.no_search_results), mSearchView.getQuery().toString());
            SpannableStringBuilder ssb = new SpannableStringBuilder(message);
            ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            noResults.setText(ssb);
        }
        if (noResults != null) {
            noResults.setVisibility(visibility);
        }
    }

    void searchFor(String query) {
        clearResults();
        mSearchView.clearFocus();
        mSearchPresenter.queryForString(query);

    }

    Transition getTransition(@TransitionRes int transitionId) {
        Transition transition = transitions.get(transitionId);
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId);
            transitions.put(transitionId, transition);
        }
        return transition;
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        mSearchView.setImeOptions(mSearchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (TextUtils.isEmpty(query)) {
                    clearResults();
                }
                return true;
            }
        });

    }

    private void setupTransitions() {
        // grab the position that the search icon transitions in *from*
        // & use it to configure the return transition
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(
                    List<String> sharedElementNames,
                    List<View> sharedElements,
                    List<View> sharedElementSnapshots) {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    View searchIcon = sharedElements.get(0);
                    if (searchIcon.getId() != R.id.searchback) return;
                    int centerX = (searchIcon.getLeft() + searchIcon.getRight()) / 2;
                    CircularReveal hideResults = (CircularReveal) TransitionsUtil.findTransition(
                            (TransitionSet) getWindow().getReturnTransition(),
                            CircularReveal.class, R.id.results_container);
                    if (hideResults != null) {
                        hideResults.setCenter(new Point(centerX, 0));
                    }
                }
            }
        });
    }

    @Override
    public void showResults(List<SearchResults> results) {
        if (results != null && results.size() > 0) {
            if (mSearchRecyclerView.getVisibility() != View.VISIBLE) {
                TransitionManager.beginDelayedTransition(mContainer,
                        getTransition(R.transition.search_show_results));
                mEmpty.setVisibility(View.GONE);
                mSearchRecyclerView.setVisibility(View.VISIBLE);

                // fab.setVisibility(View.VISIBLE);
            }
            mResultsList.addAll(results);
            mSearchAdapter.notifyDataSetChanged();
        } else {
            TransitionManager.beginDelayedTransition(
                    mContainer, getTransition(R.transition.auto));
            mEmpty.setVisibility(View.GONE);
            setNoResultsVisibility(View.VISIBLE);
        }
    }

    @Override
    public void searchError(String errorMsg) {
        TransitionManager.beginDelayedTransition(
                mContainer, getTransition(R.transition.auto));
        mEmpty.setVisibility(View.GONE);
        setNoResultsVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        mEmpty.setVisibility(View.VISIBLE);
        ImeUtils.hideIme(mSearchView);
    }

    @Override
    public void hideProgress() {
        mEmpty.setVisibility(View.GONE);
    }
}
