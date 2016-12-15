package techgravy.nextstop.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import techgravy.nextstop.data.SharedPrefManager;
import timber.log.Timber;

/**
 * Created by aditlal on 29/11/16.
 */

public class FCMIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FCM_TAG";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        SharedPrefManager sharedPreferencesManager = SharedPrefManager.getInstance(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.tag(TAG).d("FCM token  = " + refreshedToken);
        sharedPreferencesManager.setFCMToken(refreshedToken);
    }
}
