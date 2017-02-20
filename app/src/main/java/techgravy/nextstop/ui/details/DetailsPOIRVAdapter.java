package techgravy.nextstop.ui.details;

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
import techgravy.nextstop.ui.details.model.POI;
import timber.log.Timber;

/**
 * Created by aditlal on 27/12/16.
 */

class DetailsPOIRVAdapter extends RecyclerView.Adapter<DetailsPOIRVAdapter.ViewHolder> {
    private static final String IS_LOADING = " is loading";
    private final LayoutInflater layoutInflater;
    private Context mContext;
    private static final String TAG = "DetailsPOVAdapter"; // cant be saved to strings , context prob
    private List<POI> mResultsList;

    DetailsPOIRVAdapter(Context context, List<POI> resultsList) {
        this.layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mResultsList = resultsList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.item_poi, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        POI results = mResultsList.get(position);
        holder.mPlaceImageView.layout(0, 0, 0, 0);
        Glide.with(mContext).load(results.photo())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Timber.tag(TAG).d(mContext.getString(R.string.log_error), model, e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Timber.tag(TAG).d(mContext.getString(R.string.log_error), model, IS_LOADING);
                        return false;
                    }
                })
                .into(holder.mPlaceImageView);

        holder.itemView.setTag(results);
        holder.mPlaceNameTextView.setText(results.place());
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPlace)
        ImageView mPlaceImageView;
        @BindView(R.id.placeNameTextView)
        AppCompatTextView mPlaceNameTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
