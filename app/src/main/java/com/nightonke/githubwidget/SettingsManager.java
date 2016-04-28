package com.nightonke.githubwidget;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Weiping on 2016/4/26.
 */
public class SettingsManager {

    private static boolean showToast = true;

    private static int defaultBaseColor = Color.parseColor("#D6E685");
    private static int baseColor = Color.parseColor("#D6E685");
    
    private static int textColor = Color.parseColor("#000000");

    private static int startWeekDay = Weekday.SUN.v;
    
    private static String userName = null;
    
    private static int userId = -1;
    
    private static boolean showMonthDashIn3D = false;
    
    private static boolean showWeekdayDashIn3D = false;
    
    private static int updateTime = Util.HALF_AN_HOUR;

    private static String motto = "";
    
    private static int followers = -1;
    
    private static long lastUpdateFollowersTime = -1;

    private static String lastUpdateStarsId = null;
    
    private static String lastUpdateStarsDate = null;
    
    private static int todayStars = 0;

    private static int receivedEventPerPage = 30;

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

    public static void resetBaseColor() {
        setBaseColor(defaultBaseColor);
    }

    public static int getDefaultBaseColor() {
        return defaultBaseColor;
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

    public static boolean getShowMonthDashIn3D() {
        showMonthDashIn3D = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getBoolean("SHOW_MONTH_DASH_IN_3D", showMonthDashIn3D);
        return showMonthDashIn3D;
    }

    public static void setShowMonthDashIn3D(boolean showMonthDashIn3D) {
        SettingsManager.showMonthDashIn3D = showMonthDashIn3D;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_MONTH_DASH_IN_3D", showMonthDashIn3D);
        editor.commit();
    }

    public static boolean getShowWeekdayDashIn3D() {
        showWeekdayDashIn3D = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getBoolean("SHOW_WEEKDAY_DASH_IN_3D", showWeekdayDashIn3D);
        return showWeekdayDashIn3D;
    }

    public static void setShowWeekdayDashIn3D(boolean showWeekdayDashIn3D) {
        SettingsManager.showWeekdayDashIn3D = showWeekdayDashIn3D;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_WEEKDAY_DASH_IN_3D", showWeekdayDashIn3D);
        editor.commit();
    }

    public static int getUpdateTime() {
        updateTime = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("UPDATE_TIME", updateTime);
        return updateTime;
    }

    public static void setUpdateTime(int updateTime) {
        SettingsManager.updateTime = updateTime;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("UPDATE_TIME", updateTime);
        editor.commit();
    }

    public static String getMotto() {
        motto = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("MOTTO", motto);
        return motto;
    }

    public static void setMotto(String motto) {
        SettingsManager.motto = motto;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putString("MOTTO", motto);
        editor.commit();
    }

    public static int getFollowers() {
        followers = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("FOLLOWERS", followers);
        return followers;
    }

    public static void setFollowers(int followers) {
        SettingsManager.followers = followers;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("FOLLOWERS", followers);
        editor.commit();
    }

    public static int getLastUpdateFollowersTime() {
        lastUpdateFollowersTime = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getLong("LAST_UPDATE_FOLLOWERS_TIME", lastUpdateFollowersTime);
        return followers;
    }

    public static void setLastUpdateFollowersTime(long lastUpdateFollowersTime) {
        SettingsManager.lastUpdateFollowersTime = lastUpdateFollowersTime;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putLong("LAST_UPDATE_FOLLOWERS_TIME", lastUpdateFollowersTime);
        editor.commit();
    }

    public static String getLastUpdateStarsId() {
        lastUpdateStarsId = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("LAST_UPDATE_STARS_ID", lastUpdateStarsId);
        return lastUpdateStarsId;
    }

    public static void setLastUpdateStarsId(String lastUpdateStarsId) {
        SettingsManager.lastUpdateStarsId = lastUpdateStarsId;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putString("LAST_UPDATE_STARS_ID", lastUpdateStarsId);
        editor.commit();
    }

    public static String getLastUpdateStarsDate() {
        lastUpdateStarsDate = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("LAST_UPDATE_STARS_DATE", lastUpdateStarsDate);
        return lastUpdateStarsDate;
    }

    public static void setLastUpdateStarsDate(String lastUpdateStarsDate) {
        SettingsManager.lastUpdateStarsDate = lastUpdateStarsDate;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putString("LAST_UPDATE_STARS_DATE", lastUpdateStarsDate);
        editor.commit();
    }

    public static int getTodayStars() {
        todayStars = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("TODAY_STARS", todayStars);
        return todayStars;
    }

    public static void setTodayStars(int todayStars) {
        SettingsManager.todayStars = todayStars;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("TODAY_STARS", todayStars);
        editor.commit();
    }
    
    public static int getReceivedEventPerPage() {
        receivedEventPerPage = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("RECEIVED_EVENT_PER_PAGE", receivedEventPerPage);
        return followers;
    }

    public static void setReceivedEventPerPage(int receivedEventPerPage) {
        SettingsManager.receivedEventPerPage = receivedEventPerPage;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("RECEIVED_EVENT_PER_PAGE", receivedEventPerPage);
        editor.commit();
    }
    
    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance() {
        return ourInstance;
    }

    private SettingsManager() {
    }
}
