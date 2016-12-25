package techgravy.nextstop.ui.home;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

    public HomeAdapter(Context context, List<Places> placesList) {
        this.mPlacesList = placesList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_home, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  runEnterAnimation(viewHolder.itemView, position);
        Places places = mPlacesList.get(position);
        Glide.with(mContext).load(places.getPhotos().get(0)).into(holder.mPlaceImageView);
        holder.mPlaceNameTextView.setText(places.getPlace());
    }

    @Override
    public int getItemCount() {
        return mPlacesList.size();
    }

  /*  private int lastAnimatedPosition = -1;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }*/

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
