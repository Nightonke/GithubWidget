package com.nightonke.githubwidget;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Weiping on 2016/4/26.
 */
public class SettingsManager {

    private static boolean showToast = true;

    public static boolean getShowToast() {
        showToast = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getBoolean("SHOW_TOAST", showToast);
        return showToast;
    }

    public static void setShowToast(boolean showToast) {
        SettingsManager.showToast = showToast;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_TOAST", showToast);
        editor.apply();
    }

    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance() {
        return ourInstance;
    }

    private SettingsManager() {
    }
}
