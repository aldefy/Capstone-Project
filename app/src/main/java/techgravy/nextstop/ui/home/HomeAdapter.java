package techgravy.nextstop.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.details.DetailsCityActivity;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.transitions.ReflowText;

/**
 * Created by aditlal on 24/12/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final int REQUEST_PLACE = 523; //Request code , random
    private List<Places> mPlacesList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    // we need to hold on to an activity ref for the shared element transitions :/
    private final Activity host;

    public HomeAdapter(Context context, List<Places> placesList) {
        setHasStableIds(true);
        this.mPlacesList = placesList;
        this.host = (Activity) context;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_home, parent, false));
        holder.itemView.setOnClickListener(view -> {
            Places places = getItem(holder.getAdapterPosition());
            Intent intent = new Intent();
            intent.setClass(host, DetailsCityActivity.class);
            intent.putExtra(DetailsCityActivity.EXTRA_PLACE, places);
            ImageView imageView = (ImageView) view.findViewById(R.id.placeImageView);
            AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.placeNameTextView);
            ReflowText.addExtras(
                    intent,
                    new ReflowText.ReflowableTextView(textView));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(host, Pair.create(imageView, imageView.getTransitionName()),
                            Pair.create(textView, textView.getTransitionName()));
            host.startActivityForResult(intent, REQUEST_PLACE, options.toBundle());
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Places places = getItem(position);
        Glide.with(mContext).load(places.photos().get(0)).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.mPlaceImageView);
        holder.mPlaceNameTextView.setText(places.place());
    }

    @Override
    public int getItemCount() {
        return mPlacesList.size();
    }

    private Places getItem(int position) {
        return mPlacesList.get(position);
    }

    public int getItemPosition(final String itemId) {
        for (int position = 0; position < mPlacesList.size(); position++) {
            if (getItem(position).place_id().equals(itemId)) return position;
        }
        return RecyclerView.NO_POSITION;
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
