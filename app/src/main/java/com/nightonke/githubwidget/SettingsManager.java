package com.nightonke.githubwidget;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    private static ListViewContent listViewContent = ListViewContent.TRENDING_DAILY;

    private static Language language = Language.JAVA;

    private static ArrayList<HashMap<String, String>> listViewContents = null;

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
        return receivedEventPerPage;
    }

    public static void setReceivedEventPerPage(int receivedEventPerPage) {
        SettingsManager.receivedEventPerPage = receivedEventPerPage;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("RECEIVED_EVENT_PER_PAGE", receivedEventPerPage);
        editor.commit();
    }

    public static ListViewContent getListViewContent() {
        listViewContent = ListViewContent.values()[PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getInt("LIST_VIEW_CONTENT", listViewContent.v)];
        return listViewContent;
    }

    public static void setListViewContent(ListViewContent listViewContent) {
        SettingsManager.listViewContent = listViewContent;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putInt("LIST_VIEW_CONTENT", listViewContent.v);
        editor.commit();
    }

    public static Language getLanguage() {
        language = Language.fromString(PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("LANGUAGE", language.v));
        return language;
    }

    public static void setLanguage(Language language) {
        SettingsManager.language = language;
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        editor.putString("LANGUAGE", language.v);
        editor.commit();
    }

    public static ArrayList<HashMap<String, String>> getListViewContents() {
        String contents = PreferenceManager.
                getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
                .getString("LIST_VIEW_CONTENTS", null);
        if (contents == null) {
            Util.log("No list view contents in xml file");
            listViewContents = null;
            return listViewContents;
        }
        try {
            JSONArray jsonArray = new JSONArray(contents);
            listViewContents = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, String> content = new HashMap<>();
                content.put("title", jsonObject.getString("title"));
                content.put("content", jsonObject.getString("content"));
                content.put("corner", jsonObject.getString("corner"));
                content.put("url", jsonObject.getString("url"));
                listViewContents.add(content);
            }
        } catch (JSONException j) {
            j.printStackTrace();
            listViewContents = null;
            Util.log("Json error in getting list view contents");
        }
        return listViewContents;
    }

    public static final String A_REPO = "<li class=\"repo-list-item\" id=\"";
    public static final String TITLE = "<a href=\"/login?return_to=%2F";
    public static final String TITLE_END = "\"";
    public static final String CONTENT = "<p class=\"repo-list-description\">\n      ";
    public static final String CONTENT_END = "\n    </p>\n";
    public static final String CORNER = "      &#8226;\n\n    ";
    public static final String CORNER_END = " star";
    public static void setListViewContents(String contents) {
        Util.log("Write trendings...");
        ArrayList<HashMap<String, String>> listViewContents = new ArrayList<>();
        try {
            switch (listViewContent) {
                case TRENDING_DAILY:
                case TRENDING_WEEKLY:
                case TRENDING_MONTHLY:
                    int repoIndex = -1;
                    while (true) {
                        repoIndex = contents.indexOf(A_REPO, repoIndex + 1);

                        if (repoIndex == -1) break;

                        int start = -1;
                        int end = -1;
                        HashMap<String, String> content = new HashMap<>();

                        start = contents.indexOf(TITLE, repoIndex) + TITLE.length();
                        end = contents.indexOf(TITLE_END, start);
                        String title = contents.substring(start, end);
                        title = title.replaceAll("%2F", "/");
                        content.put("title", title);
                        content.put("url", Util.getString(R.string.url_prefix) + title);

                        start = contents.indexOf(CORNER, repoIndex) + CORNER.length();
                        end = contents.indexOf(CORNER_END, start);
                        String starsString = contents.substring(start, end);
                        starsString = starsString.replaceAll(",", "");
                        try {
                            Integer.parseInt(starsString);
                            content.put("corner", starsString);
                        } catch (NumberFormatException n) {
                            content.put("corner", "0");
                        }
                        int starsIndex = start;

                        start = contents.indexOf(CONTENT, repoIndex) + CONTENT.length();
                        if (start > starsIndex) {
                            // no content
                            content.put("content", "");
                        } else {
                            end = contents.indexOf(CONTENT_END, start);
                            content.put("content", contents.substring(start, end));
                        }

                        listViewContents.add(content);
                    }
                    break;
            }

            if (listViewContents.size() != 0) SettingsManager.listViewContents = listViewContents;

            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, String> content : listViewContents) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", content.get("title"));
                jsonObject.put("content", content.get("content"));
                jsonObject.put("corner", content.get("corner"));
                jsonObject.put("url", content.get("url"));
                jsonArray.put(jsonObject);
            }

            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
            editor.putString("LIST_VIEW_CONTENTS", jsonArray.toString());
            editor.commit();
        } catch (JSONException j) {
            j.printStackTrace();
            Util.log("Json error in getting list view contents");
        }
    }

    public static void setListViewContents(JSONArray contents) {
        Util.log("Write events...");
        ArrayList<HashMap<String, String>> listViewContents = new ArrayList<>();
        try {
            switch (listViewContent) {
                case EVENT:
                    for (int i = 0; i < contents.length(); i++) {
                        try {
                            JSONObject event = contents.getJSONObject(i);
                            HashMap<String, String> content = new HashMap<>();

                            JSONObject repo = event.getJSONObject("repo");
                            String repoName = repo.getString("name");

                            String createAt = event.getString("created_at");

                            String actor = event.getJSONObject("actor").getString("login");
                            String act = "";
                            String url = "";
                            String con = "";

                            String type = event.getString("type");
                            JSONObject payload = null;
                            switch (type) {
                                case "CommitCommentEvent":
                                    payload = event.getJSONObject("payload");
                                    actor = payload.getJSONObject("comment").getJSONObject("user").getString("login");
                                    url = payload.getJSONObject("comment").getString("html_url");
                                    act = " commented commit of";
                                    con = payload.getJSONObject("repository").getString("full_name");
                                    break;
                                case "CreateEvent":
                                    payload = event.getJSONObject("payload");
                                    url = "https://github.com/" + event.getJSONObject("repo").getString("name");
                                    act = " created " + payload.getString("ref_type");
                                    con = "for " + event.getJSONObject("repo").getString("name");
                                    break;
                                case "DeleteEvent":
                                    // I cannot find an example for this
                                    break;
                                case "DeploymentEvent":
                                    // I cannot find an example for this
                                    break;
                                case "DeploymentStatusEvent":
                                    // I cannot find an example for this
                                    break;
                                case "DownloadEvent":
                                    // Events of this type are no longer created,
                                    // but it's possible that they exist in timelines of some users.
                                    break;
                                case "FollowEvent":
                                    // Events of this type are no longer created,
                                    // but it's possible that they exist in timelines of some users.
                                    break;
                                case "ForkEvent":
                                    payload = event.getJSONObject("payload");
                                    url = payload.getJSONObject("forkee").getString("html_url");
                                    act = " forked " + event.getJSONObject("repo").getString("name");
                                    con = "to " + payload.getJSONObject("forkee").getString("full_name");
                                    break;
                                case "ForkApplyEvent":
                                    // Events of this type are no longer created,
                                    // but it's possible that they exist in timelines of some users.
                                    break;
                                case "GistEvent":
                                    // Events of this type are no longer created,
                                    // but it's possible that they exist in timelines of some users.
                                    break;
                                case "GollumEvent":
                                    payload = event.getJSONObject("payload");
                                    JSONArray pages = payload.getJSONArray("pages");
                                    if (pages.getJSONObject(0).getString("html_url").indexOf("https://github.com") != 0) {
                                        url = "https://github.com" + pages.getJSONObject(0).getString("html_url");
                                    } else {
                                        url = pages.getJSONObject(0).getString("html_url");
                                    }
                                    act = " updated wiki of";
                                    con = event.getJSONObject("repo").getString("name");
                                    break;
                                case "IssueCommentEvent":
                                    payload = event.getJSONObject("payload");
                                    url = payload.getJSONObject("issue").getString("html_url");
                                    act = " comment the issue";
                                    con = event.getJSONObject("repo").getString("name") + payload.getJSONObject("issue").getString("title");
                                    break;
                                case "IssuesEvent":
                                    payload = event.getJSONObject("payload");
                                    url = payload.getJSONObject("issue").getString("html_url");
                                    act = payload.getString("action") + " the issue";
                                    con = event.getJSONObject("repo").getString("name") + payload.getJSONObject("issue").getString("title");
                                    break;
                                case "MemberEvent":
                                    // I cannot find an example for this
                                    break;
                                case "MembershipEvent":
                                    // I cannot find an example for this
                                    break;
                                case "PageBuildEvent":
                                    // I cannot find an example for this
                                    break;
                                case "PublicEvent":
                                    payload = event.getJSONObject("payload");
                                    url = event.getJSONObject("repo").getString("url");
                                    actor = event.getJSONObject("repo").getString("name");
                                    act = "";
                                    con = " is open sourced";
                                    break;
                                case "PullRequestEvent":
                                    payload = event.getJSONObject("payload");
                                    url = payload.getJSONObject("pull_request").getString("html_url");
                                    act = payload.getString("action") + " a pull request";
                                    con = payload.getJSONObject("pull_request").getString("title");
                                    break;
                                case "PullRequestReviewCommentEvent":
                                    // I cannot find an example for this
                                    break;
                                case "PushEvent":
                                    url = "https://github.com" + event.getJSONObject("repo").getString("name");
                                    act = " push an event";
                                    con = "to " + event.getJSONObject("repo").getString("name");
                                    break;
                                case "ReleaseEvent":
                                    payload = event.getJSONObject("payload");
                                    url = payload.getJSONObject("release").getString("html_url");
                                    act = " make a release";
                                    con = "to " + event.getJSONObject("repo").getString("name");
                                    break;
                                case "RepositoryEvent":
                                    // I cannot find an example for this
                                    break;
                                case "StatusEvent":
                                    // I cannot find an example for this
                                    break;
                                case "TeamAddEvent":
                                    // I cannot find an example for this
                                    break;
                                case "WatchEvent":
                                    url = "https://github.com/" + event.getJSONObject("repo").getString("name");
                                    act = " starred " + event.getJSONObject("repo").getString("name");
                                    break;
                            }

                            content.put("title", actor + act);
                            content.put("content", con);
                            content.put("corner", Util.getTime(createAt));
                            Util.log(Util.getTime(createAt));
                            content.put("url", url);

                            listViewContents.add(content);

                        } catch (JSONException j) {
                            j.printStackTrace();
                        }
                    }
                    break;
            }

            if (listViewContents.size() != 0) SettingsManager.listViewContents = listViewContents;

            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, String> content : listViewContents) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", content.get("title"));
                jsonObject.put("content", content.get("content"));
                jsonObject.put("corner", content.get("corner"));
                jsonObject.put("url", content.get("url"));
                jsonArray.put(jsonObject);
            }

            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
            editor.putString("LIST_VIEW_CONTENTS", jsonArray.toString());
            editor.commit();
        } catch (JSONException j) {
            j.printStackTrace();
            Util.log("Json error in getting list view contents");
        }
    }

    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance() {
        return ourInstance;
    }

    private SettingsManager() {
    }
}
