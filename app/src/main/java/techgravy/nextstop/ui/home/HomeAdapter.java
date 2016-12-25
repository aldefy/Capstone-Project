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
        Places places = mPlacesList.get(position);
        Glide.with(mContext).load(places.getPhotos().get(0)).into(holder.mPlaceImageView);
        holder.mPlaceNameTextView.setText(places.getPlace());
    }

    @Override
    public int getItemCount() {
        return mPlacesList.size();
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
