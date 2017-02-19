package techgravy.nextstop.ui.search;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.data.model.SearchResults;
import timber.log.Timber;

/**
 * Created by aditlal on 27/12/16.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private Context mContext;
    List<SearchResults> mResultsList;

    public SearchResultsAdapter(Context context, List<SearchResults> resultsList) {
        this.layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mResultsList = resultsList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResults results = mResultsList.get(position);
        if (results.getSearchResultPhotosList() != null && results.getSearchResultPhotosList().size() > 0) {
            holder.mPlaceImageView.layout(0,0,0,0);
            Glide.with(mContext).load(results.generatePhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Timber.tag("Glide").d(mContext.getString(R.string.log_error), model, e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Timber.tag("Glide").d(mContext.getString(R.string.log_error), model, " is Loading");
                            return false;
                        }
                    })
                    .into(holder.mPlaceImageView);

        }
        holder.itemView.setTag(results);
        holder.mPlaceNameTextView.setText(results.getName());
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeImageView)
        ImageView mPlaceImageView;
        @BindView(R.id.placeNameTextView)
        AppCompatTextView mPlaceNameTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
