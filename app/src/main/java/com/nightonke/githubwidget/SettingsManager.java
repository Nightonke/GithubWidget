package com.nightonke.githubwidget;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Created by Weiping on 2016/4/26.
 */
public class SettingsManager {

    private static boolean showToast = true;

    private static int baseColor = Color.parseColor("#D6E685");
    
    private static int textColor = Color.parseColor("#000000");

    private static int startWeekDay = WeekDay.SUN.v;

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

    public static int getBaseColor() {
        baseColor = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("BASE_COLOR", baseColor);
        return baseColor;
    }

    public static void setBaseColor(int baseColor) {
        SettingsManager.baseColor = baseColor;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("BASE_COLOR", baseColor);
        editor.apply();
    }

    public static int getTextColor() {
        textColor = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("TEXT_COLOR", textColor);
        return textColor;
    }

    public static void setTextColor(int textColor) {
        SettingsManager.textColor = textColor;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("TEXT_COLOR", textColor);
        editor.apply();
    }

    public static int getStartWeekDay() {
        startWeekDay = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("START_WEEKDAY", startWeekDay);
        return startWeekDay;
    }

    public static void setStartWeekDay(int startWeekDay) {
        SettingsManager.startWeekDay = startWeekDay;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("START_WEEKDAY", startWeekDay);
        editor.apply();
    }
    
    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance() {
        return ourInstance;
    }

    private SettingsManager() {
    }
}
