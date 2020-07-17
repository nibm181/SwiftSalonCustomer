package lk.xtracheese.swiftsalon.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonToken;

public class Session {

    private SharedPreferences preferences;
    private int userId;
    private JsonToken token;
    private boolean isSignedIn;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        isSignedIn = false;
    }

    public int getUserId() {
        userId = preferences.getInt("userId", 0);
        return userId;
    }

    public void setUserId(int userId) {
        preferences.edit().putInt("userId", userId).apply();
    }

    public boolean isSignedIn() {
        isSignedIn = preferences.getBoolean("isSignedIn", false);
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
        preferences.edit().putBoolean("isSignedIn", isSignedIn).apply();
    }

    public void clearSession() {
        preferences.edit().clear().apply();
    }
}
