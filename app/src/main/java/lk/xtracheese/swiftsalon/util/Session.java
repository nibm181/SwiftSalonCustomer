package lk.xtracheese.swiftsalon.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonToken;

public class Session {

    private SharedPreferences preferences;
    private int userId;
    private String userImg;
    private String userName;
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

    public void setUserImg(String userImg) {
        preferences.edit().putString("userImg", userImg).apply();
    }

    public String getUserImg() {
        userImg = preferences.getString("userImg", "http://10.0.2.2/swiftsalon-api/uploads/user_images/index.png");
        return userImg;
    }

    public void setUserName(String userName) {
        preferences.edit().putString("userName", userName).apply();
    }

    public String getUsername() {
        userName = preferences.getString("userName", "Lebowski");
        return userName;
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
