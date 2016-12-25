package techgravy.nextstop.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 25/12/16.
 */

public class DetailsCityActivity extends AppCompatActivity {

    @BindView(R.id.placeImageView)
    ImageView mPlaceImageView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    Places mPlaces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_city);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        Glide.with(DetailsCityActivity.this).load(mPlaces.getPhotos().get(0)).into(mPlaceImageView);
    }
}
