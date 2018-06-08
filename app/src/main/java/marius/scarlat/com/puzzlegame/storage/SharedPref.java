package marius.scarlat.com.puzzlegame.storage;


import android.content.Context;
import android.content.SharedPreferences;

import marius.scarlat.com.puzzlegame.general.Constants;

public class SharedPref {

    // Data storage
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor preferencesEditor;

    public static void setup (Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Constants.SCORE_INFO, Context.MODE_PRIVATE);
        }
    }

    public static void saveScore (final double score) {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(Constants.PLAYER_SCORE, String.valueOf(score));
        preferencesEditor.apply();
    }

    public static void savePlayer(final String username) {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(Constants.PLAYER_NAME, username);
        preferencesEditor.apply();
    }

    public static void clear() {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    public static double getLastScore() {
        return Double.valueOf(sharedPreferences.getString(Constants.PLAYER_SCORE, "0"));
    }

    public static String getLastPlayer() {
        return sharedPreferences.getString(Constants.PLAYER_NAME, Constants.UNSET);
    }
}
