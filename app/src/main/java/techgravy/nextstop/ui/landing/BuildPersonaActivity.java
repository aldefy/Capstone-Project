package techgravy.nextstop.ui.landing;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import techgravy.nextstop.R;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.ui.home.HomeActivity;
import timber.log.Timber;

/**
 * Created by aditlal on 16/12/16 - 16.
 */

public class BuildPersonaActivity extends AppCompatActivity implements PersonaGridRecyclerAdapter.PersonaInterface {

    private static final String TAG = BuildPersonaActivity.class.getSimpleName();
    @BindView(R.id.containerImageView)
    ImageView containerImageView;
    @BindView(R.id.filteringView)
    View filteringView;
    @BindView(R.id.headingTextView)
    TextView headingTextView;
    @BindView(R.id.subHeadingTextView)
    TextView subHeadingTextView;
    @BindView(R.id.actionsRecyclerView)
    RecyclerView actionsRecyclerView;
    PersonaGridRecyclerAdapter gridRecyclerAdapter;
    @BindView(R.id.nextBtn)
    AppCompatButton nextBtn;
    private List<PersonaTags> userSelectionList;
    private DatabaseReference database;
    private SharedPrefManager prefManager;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_persona);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance().getReference();
        prefManager = SharedPrefManager.getInstance(BuildPersonaActivity.this);
        compositeSubscription = new CompositeSubscription();
        setupViews();
    }

    private void setupViews() {
        List<PersonaTags> tagsList = new ArrayList<>();
        userSelectionList = new ArrayList<>();
        tagsList.add(new PersonaTags(getString(R.string.family)));
        tagsList.add(new PersonaTags(getString(R.string.entertainment)));
        tagsList.add(new PersonaTags(getString(R.string.Shopping)));
        tagsList.add(new PersonaTags(getString(R.string.Sun)));
        tagsList.add(new PersonaTags(getString(R.string.Adventure)));
        tagsList.add(new PersonaTags(getString(R.string.landmarks)));
        tagsList.add(new PersonaTags(getString(R.string.sports)));
        tagsList.add(new PersonaTags(getString(R.string.nightlife)));
        tagsList.add(new PersonaTags(getString(R.string.food)));
        tagsList.add(new PersonaTags(getString(R.string.cityscape)));
        tagsList.add(new PersonaTags(getString(R.string.history)));
        tagsList.add(new PersonaTags(getString(R.string.picturesque)));
        tagsList.add(new PersonaTags(getString(R.string.beaches)));
        tagsList.add(new PersonaTags(getString(R.string.islands)));
        tagsList.add(new PersonaTags(getString(R.string.romantic)));
        tagsList.add(new PersonaTags(getString(R.string.art)));
        tagsList.add(new PersonaTags(getString(R.string.luxury)));
        Glide.with(BuildPersonaActivity.this).load(R.drawable.persona_bg).into(containerImageView);
        nextBtn.setEnabled(false);
        gridRecyclerAdapter = new PersonaGridRecyclerAdapter(BuildPersonaActivity.this, tagsList);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        actionsRecyclerView.setLayoutManager(gridLayoutManager);
        actionsRecyclerView.setAdapter(gridRecyclerAdapter);
        Subscription startBtnSub = RxView.clicks(nextBtn).subscribe(view -> {
            updateUserInDataBase();

        });
        compositeSubscription.add(startBtnSub);
    }

    private void updateUserInDataBase() {
        Query query = database.child(getString(R.string.users)).orderByChild(prefManager.getUUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.tag(TAG).d(dataSnapshot.toString());
                //    User user = dataSnapshot.getValue(User.class);
                dataSnapshot.getRef().child(prefManager.getUUID()).child(getString(R.string.persona_tags)).setValue(userSelectionList);
                //    database.child("users").child(prefManager.getUUID()).setValue(user);
                startActivity(new Intent(BuildPersonaActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.tag(TAG).e(databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }


    @Override
    public void itemClicked(PersonaTags personaTags) {
        if (personaTags.isChecked())
            userSelectionList.add(personaTags);
        else
            userSelectionList.remove(personaTags);
        nextBtn.setEnabled(userSelectionList.size() > 0);
    }
}
