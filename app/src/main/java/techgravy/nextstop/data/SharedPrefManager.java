package techgravy.nextstop.data;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPrefManager {
    private static SharedPrefManager instance;
    private SharedPreferences sharedPrefs;
    private static final String PREF_NAME = "nextstop_pref";
    private static final String PREF_FCM = "fcm_token";


    private SharedPrefManager(Context context) {
        //using application context just to make sure we don't leak any activities
        sharedPrefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPrefManager(context);
        return instance;
    }

    public String getFCMToken() {
        return sharedPrefs.getString(PREF_FCM, "");
    }

    public void setFCMToken(String token) {
        sharedPrefs.edit().putString(PREF_FCM, token).apply();
    }
}
