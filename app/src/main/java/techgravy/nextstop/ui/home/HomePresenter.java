package techgravy.nextstop.ui.home;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import techgravy.nextstop.ui.home.model.AutoJson_Places;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.utils.FirebaseJSONUtil;

/**
 * Created by aditlal on 24/12/16.
 */

class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mHomeView;
    private DatabaseReference database;

    @Inject
    HomePresenter(HomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void fetchListOfPlaces() {
        mHomeView.showProgress();
        Query query = database.child("places");
        //.orderByChild("sun").equalTo("true");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //     Timber.tag(TAG).d(dataSnapshot.toString());
                if (dataSnapshot.getValue() != null) {
                    try {
                        List<Places> placesList = new ArrayList<Places>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Places places = FirebaseJSONUtil.deserialize(snapshot, AutoJson_Places.class);
                            placesList.add(places);
                        }
                        mHomeView.attachData(placesList);
                        mHomeView.hideProgress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mHomeView.dataError(new Throwable(databaseError.getMessage()));
            }
        });
    }


}
