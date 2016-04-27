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

    private static int startWeekDay = Weekday.SUN.v;
    
    private static String userName = null;
    
    private static int userId = -1;

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
        editor.commit();
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
        editor.commit();
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
        editor.commit();
    }

    public static Weekday getStartWeekDay() {
        startWeekDay = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("START_WEEKDAY", startWeekDay);
        switch (startWeekDay) {
            case 0: return Weekday.SUN;
            case 1: return Weekday.MON;
            case 2: return Weekday.TUE;
            case 3: return Weekday.WED;
            case 4: return Weekday.THU;
            case 5: return Weekday.FRI;
            case 6: return Weekday.SAT;
            default: return Weekday.SUN;
        }
    }

    public static void setStartWeekDay(int startWeekDay) {
        SettingsManager.startWeekDay = startWeekDay;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("START_WEEKDAY", startWeekDay);
        editor.commit();
    }
    
    public static String getUserName() {
        userName = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("USER_NAME", userName);
        return userName;
    }
    
    public static void setUserName(String userName) {
        SettingsManager.userName = userName;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putString("USER_NAME", userName);
        editor.commit();
    }

    public static int getUserId() {
        userId = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("USER_ID", userId);
        return userId;
    }

    public static void setUserId(int userId) {
        SettingsManager.userId = userId;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("USER_ID", userId);
        editor.commit();
    }
    
    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance() {
        return ourInstance;
    }

    private SettingsManager() {
    }
}
