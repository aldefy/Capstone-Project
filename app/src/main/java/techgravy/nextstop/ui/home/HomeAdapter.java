package techgravy.nextstop.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 24/12/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Places> mPlacesList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private PlaceAdapterClickInterface mPlaceAdapterClickInterface;

    public HomeAdapter(Context context, List<Places> placesList) {
        setHasStableIds(true);
        this.mPlacesList = placesList;
        this.mPlaceAdapterClickInterface = (PlaceAdapterClickInterface) context;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_home, parent, false), mPlaceAdapterClickInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Places places = getItemAtPosition(position);
        Glide.with(mContext).load(places.photos().get(0)).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(holder.mPlaceImageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(bitmap, glideAnimation);
                        Palette.from(bitmap).generate(palette -> {
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
                            if (vibrant != null) {
                                holder.mScrim.setBackground(new ColorDrawable(vibrant.getRgb()));
                            }

                        });
                    }
                });


        holder.itemView.setTag(places);
        holder.mPlaceNickNameTextView.setText(places.tag());
        holder.mPlaceNameTextView.setText(places.place());
    }

    @Override
    public long getItemId(int position) {
        return getItemAtPosition(position).hashCode();
    }

    private Places getItemAtPosition(int position) {
        return mPlacesList.get(position);
    }


    @Override
    public int getItemCount() {
        return mPlacesList.size();
    }

    public int getItemPosition(final int itemHashCode) {
        for (int position = 0; position < mPlacesList.size(); position++) {
            if (getItemAtPosition(position).hashCode() == itemHashCode) return position;
        }
        return RecyclerView.NO_POSITION;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeImageView)
        ImageView mPlaceImageView;
        @BindView(R.id.placeNameTextView)
        AppCompatTextView mPlaceNameTextView;
        @BindView(R.id.placeNickNameTextView)
        AppCompatTextView mPlaceNickNameTextView;
        @BindView(R.id.rowScrim)
        View mScrim;

        ViewHolder(View view, PlaceAdapterClickInterface placeAdapterClickInterface) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(view1 -> {
                Places places = (Places) view1.getTag();
                placeAdapterClickInterface.itemClicked(places, view1.findViewById(R.id.placeImageView), view1.findViewById(R.id.placeNameTextView));
            });

        }
    }

    public interface PlaceAdapterClickInterface {
        void itemClicked(Places places, View imageView, View textView);
    }
}
