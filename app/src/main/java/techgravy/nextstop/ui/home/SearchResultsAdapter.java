package techgravy.nextstop.ui.home;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.BuildConfig;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.SearchResultPhotos;
import techgravy.nextstop.ui.home.model.SearchResults;
import timber.log.Timber;

/**
 * Created by aditlal on 27/12/16.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private Context mContext;
    NumberFormat mNumberFormat = new DecimalFormat("#");
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
        View rootView = layoutInflater.inflate(R.layout.item_home, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResults results = mResultsList.get(position);
        if (results.getSearchResultPhotosList() != null && results.getSearchResultPhotosList().size() > 0) {
            SearchResultPhotos photo = results.getSearchResultPhotosList().get(0);
            Glide.with(mContext).load(BuildConfig.GOOGLE_URL + "photo?maxwidth=" + mNumberFormat.format(photo.getWidth())
                    + "&photoreference=" + photo.getPhoto_reference() + "&key=" + BuildConfig.GOOGLE_API_KEY)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Timber.d("URL =" + model + " : " + e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Timber.d("URL =" + model + " loaded");
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
