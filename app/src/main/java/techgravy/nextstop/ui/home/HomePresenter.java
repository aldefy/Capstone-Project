package techgravy.nextstop.ui.home;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.data.User;
import techgravy.nextstop.ui.home.model.AutoJson_Places;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.landing.PersonaTags;
import techgravy.nextstop.utils.Constants;
import techgravy.nextstop.utils.FirebaseJSONUtil;
import timber.log.Timber;

/**
 * Created by aditlal on 24/12/16.
 */

class HomePresenter implements HomeContract.Presenter {
    public static final String PLACES = "places";
    public final String TAG = "HomePresenter"; // cant be saved to strings , context prob
    private HomeContract.View mHomeView;
    private DatabaseReference mDatabaseReference;
    private SharedPrefManager mSharedPrefManager;
    private List<PersonaTags> userSelectionList;
    private List<String> personaList;
    private List<Places> thePlaceList;

    @Inject
    HomePresenter(HomeContract.View mHomeView, SharedPrefManager prefManager) {
        this.mHomeView = mHomeView;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userSelectionList = new ArrayList<>();
        personaList = new ArrayList<>();
        thePlaceList = new ArrayList<>();
        this.mSharedPrefManager = prefManager;
    }

    @Override
    public void filterPlaces(HashMap<String, Boolean> personaTagsList) {
        personaList.clear();
        computePlaces(thePlaceList, personaTagsList.keySet());
    }


    private void computePlaces(List<Places> placesList, Set<String> personaTagsList) {
        HashMap<String, Places> resultMap = new HashMap<>();
        personaList = new ArrayList<>(personaTagsList);
        if (personaList.size() > 0) {
            Iterator<Places> iterator = placesList.iterator();
            while (iterator.hasNext()) {
                Places places = iterator.next();
                for (String tags : personaList)
                    switch (tags) {
                        case Constants.FAMILY:
                            if (places.family().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ENTERTAINMENT:
                            if (places.entertainment().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SHOPPING:
                            if (places.shopping().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SUN:
                            if (places.sun().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ADVENTURE:
                            if (places.adventure().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.LANDMARKS:
                            if (places.landmarks().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.WATER_SPORTS:
                            if (places.sports().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SPORTS:
                            if (places.sports().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.NIGHT_LIFE:
                            if (places.nightlife().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.FOOD:
                            if (places.food().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.CITYSCAPE:
                            if (places.cityscape().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.HISTORY:
                            if (places.history().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.PICTURESQUE:
                            if (places.picturesque().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.BEACHES:
                            if (places.beaches().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ISLAND:
                            if (places.island().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ROMANTIC:
                            if (places.romantic().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ART:
                            if (places.art().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.LUXURY:
                            if (places.luxury().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;

                    }
            }

            List<Places> results = new ArrayList<>(resultMap.values());
            Collections.sort(results, (placeLeft, placeRight) -> placeLeft.place().compareTo(placeRight.place()));
            mHomeView.attachPlaces(results);
        }
        mHomeView.hideProgress();

    }

    @Override
    public void fetchUserPlaces() {
        mHomeView.showProgress();
        Query query = mDatabaseReference.child(PLACES);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        List<Places> placesList = new ArrayList<Places>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Places places = FirebaseJSONUtil.deserialize(snapshot, AutoJson_Places.class);
                            placesList.add(places);
                        }
                        thePlaceList.addAll(placesList);
                        computeUserSelectionPlaces(placesList);

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

    private String toTitle(String s) {
        String s1 = s.substring(0, 1).toUpperCase();
        return s1 + s.substring(1);
    }

    private void computeUserSelectionPlaces(List<Places> placesList) {
        HashMap<String, Places> resultMap = new HashMap<>();
        if (userSelectionList.size() > 0) {
            for (PersonaTags personaTags : userSelectionList)
                if (personaTags.isChecked())
                    personaList.add(toTitle(personaTags.getActionName()));
            Iterator<Places> iterator = placesList.iterator();
            while (iterator.hasNext()) {
                Places places = iterator.next();
                for (String tags : personaList)
                    switch (tags) {
                        case Constants.FAMILY:
                            if (places.family().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ENTERTAINMENT:
                            if (places.entertainment().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SHOPPING:
                            if (places.shopping().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SUN:
                            if (places.sun().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ADVENTURE:
                            if (places.adventure().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.LANDMARKS:
                            if (places.landmarks().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.SPORTS:
                            if (places.sports().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.NIGHT_LIFE:
                            if (places.nightlife().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.FOOD:
                            if (places.food().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.CITYSCAPE:
                            if (places.cityscape().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.HISTORY:
                            if (places.history().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.PICTURESQUE:
                            if (places.picturesque().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.BEACHES:
                            if (places.beaches().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ISLAND:
                            if (places.island().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ROMANTIC:
                            if (places.romantic().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.ART:
                            if (places.art().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;
                        case Constants.LUXURY:
                            if (places.luxury().equalsIgnoreCase(Constants.TRUE))
                                resultMap.put(places.place(), places);
                            break;

                    }
            }

            List<Places> results = new ArrayList<>(resultMap.values());
            Collections.sort(results, (placeLeft, placeRight) -> placeLeft.place().compareTo(placeRight.place()));
            mHomeView.attachUserPlaces(results);
        }
        mHomeView.hideProgress();

    }

    @Override
    public void fetchUserPersonaTags() {
        Query query = mDatabaseReference.child(Constants.USERS).orderByChild(mSharedPrefManager.getUUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.tag(TAG).d(dataSnapshot.toString());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getPersonaTagsList() != null)
                        userSelectionList.addAll(user.getPersonaTagsList());
                    else {
                        List<PersonaTags> personaTagList = new ArrayList<>();
                        personaTagList.add(new PersonaTags(Constants.FAMILY));
                        personaTagList.add(new PersonaTags(Constants.ADVENTURE));
                        personaTagList.add(new PersonaTags(Constants.SHOPPING));
                        userSelectionList.addAll(personaTagList);
                    }
                    mHomeView.filterPersonaTags(user.getPersonaTagsList());
                    Timber.tag(TAG).d(user.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.tag(TAG).e(databaseError.getMessage());
            }
        });
    }


}
