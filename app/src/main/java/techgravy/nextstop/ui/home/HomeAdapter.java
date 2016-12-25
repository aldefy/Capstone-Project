package techgravy.nextstop.ui.home;

import android.app.Activity;
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
    // we need to hold on to an activity ref for the shared element transitions :/
    private final Activity host;

    public HomeAdapter(Context context, List<Places> placesList) {
        this.mPlacesList = placesList;
        this.host = (Activity) context;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_home, parent, false));
       /* RxView.clicks(holder.itemView).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(host, DetailsCityActivity.class);
                intent.putExtra(DetailsCityActivity.EXTRA_PLACE,
                        (Places) getItem(holder.getAdapterPosition()));
                setGridItemContentTransitions(holder.image);
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(host,
                                Pair.create(view, host.getString(R.string.transition_shot)),
                                Pair.create(view, host.getString(R.string
                                        .transition_shot_background)));
                host.startActivityForResult(intent, REQUEST_CODE_VIEW_SHOT, options.toBundle());
            }
        });*/

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Places places = getItem(position);
        Glide.with(mContext).load(places.getPhotos().get(0)).into(holder.mPlaceImageView);
        holder.mPlaceNameTextView.setText(places.getPlace());
    }

    @Override
    public int getItemCount() {
        return mPlacesList.size();
    }

    private Places getItem(int position) {
        return mPlacesList.get(position);
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
