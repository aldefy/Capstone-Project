package techgravy.nextstop.data;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {
    private static SharedPrefManager instance;
    private SharedPreferences sharedPrefs;
    private static final String PREF_NAME = "nextstop_pref";
    private static final String PREF_FCM = "fcm_token";
    private static final String PREF_AVATAR_URL = "user_avatar_url";
    private static final String PREF_UUID = "uuid";
    private static final String PREF_USER_FULL_NAME = "user_full_name";


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

    public String getAvatarUrl() {
        return sharedPrefs.getString(PREF_AVATAR_URL, "");
    }

    public void setAvatarUrl(String avatarUrl) {
        sharedPrefs.edit().putString(PREF_AVATAR_URL, avatarUrl).apply();
    }

    public String getUserFullName() {
        return sharedPrefs.getString(PREF_USER_FULL_NAME, "");
    }

    public void setUserFullName(String name) {
        sharedPrefs.edit().putString(PREF_USER_FULL_NAME, name).apply();
    }

    public String getUUID() {
        return sharedPrefs.getString(PREF_UUID, "");
    }

    public void setUUID(String id) {
        sharedPrefs.edit().putString(PREF_UUID, id).apply();
    }
}
