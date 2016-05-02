package com.nightonke.githubwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Weiping on 2016/4/26.
 */

public class ContributionsTask extends AsyncTask<String, Void, String> {

    private Widget widget;
    private RemoteViews remoteViews;
    private Context context;
    private ComponentName componentName;
    private int appWidgetId;
    private boolean is2D = true;
    private int bitmapWidth = 0;
    private int bitmapHeight = 0;
    private boolean tooFrequently = false;

    public ContributionsTask(
            Widget widget,
            RemoteViews remoteViews,
            Context context,
            ComponentName componentName,
            int appWidgetId,
            boolean is2D,
            int bitmapWidth,
            int bitmapHeight) {
        this.widget = widget;
        this.remoteViews = remoteViews;
        this.context = context;
        this.componentName = componentName;
        this.appWidgetId = appWidgetId;
        this.is2D = is2D;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Execute ContributionsTask");
        // check whether the user id is got
        String userName = SettingsManager.getUserName();
        if (userName == null) {
            // user didn't set the user name
            // we just use the default picture
            return null;
        } else {
            int userId = SettingsManager.getUserId();
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            if (userId == -1) {
                // we haven't got the user id
                try {
                    url = new URL("https://api.github.com/users/" + userName);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200){
                        InputStream in = httpURLConnection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while((len = in.read(buffer)) != -1){
                            byteArrayOutputStream.write(buffer, 0, len);
                        }
                        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Write user basic data: "
                                + byteArrayOutputStream.toString());
                        Util.writeUserBasicData(context, byteArrayOutputStream.toString());
                    } else {
                        if (httpURLConnection.getResponseCode() == 403) {
                            tooFrequently = true;
                            return null;
                        } else {
                            if (BuildConfig.DEBUG) Log.d("GithubWidget",
                                    "Get user id failed: " + httpURLConnection.getResponseCode());
                        }
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                } finally{
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }
            }
            if (SettingsManager.getUserId() == -1) return null;
            try {
                String urlString = "https://github.com/users/" + userName + "/contributions";
                if (BuildConfig.DEBUG)
                    Log.d("GithubWidget", "Get user contributions: " + urlString);
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                String cookie = Util.getLoginCookie();
                if (Util.getLoggedIn()) {
                    if (BuildConfig.DEBUG) Log.d("GithubWidget", "Cookie found: " + cookie);
                    httpURLConnection.setRequestProperty("Cookie", cookie);
                }

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == 200){
                    InputStream in = httpURLConnection.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = in.read(buffer)) != -1){
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    return byteArrayOutputStream.toString();
                } else {
                    return null;
                }
            } catch (IOException i) {
                i.printStackTrace();
            } finally{
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }
        }
        return null;
    }

    // Todo simplify this
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (tooFrequently) {
            Util.showToast(R.string.refresh_too_frequently);
            tooFrequently = false;
            if (BuildConfig.DEBUG) Log.d("GithubWidget", "Get user contributions failed: too frequently");
        }

        if (SettingsManager.getUserName() == null) {
            switch (widget) {
                case WIDGET_0:
                    remoteViews.setImageViewBitmap(R.id.contributions,
                            Util.getInputUserNameBitmap(
                                    context, SettingsManager.getBaseColor(), Util.dp2px(30)));
                    break;
                case WIDGET_1:
                    remoteViews.setImageViewBitmap(R.id.contributions,
                            Util.getInputUserNameBitmap(
                                    context, SettingsManager.getBaseColor(), Util.dp2px(30)));
                    break;
                case WIDGET_2:
                case WIDGET_3:
                case WIDGET_4:
                case WIDGET_5:
                case WIDGET_6:
                case WIDGET_7:
                    remoteViews.setImageViewBitmap(R.id.motto,
                            Util.getInputUserNameBitmap(
                                    context, SettingsManager.getBaseColor(), Util.dp2px(30)));
                    break;
                case WIDGET_8:
                    remoteViews.setImageViewBitmap(R.id.contributions,
                            Util.getInputUserNameBitmap(
                                    context, SettingsManager.getBaseColor(),
                                    (int) Util.getDimen(R.dimen.github_widget_8_avator_size)));
                    break;
            }
        } else {
            if (result == null) {
                if (BuildConfig.DEBUG) Log.d("GithubWidget", "Get user contributions failed");
            } else {
                if (BuildConfig.DEBUG)
                    Log.d("GithubWidget", "Get user contributions successfully");
                Weekday startWeekDay = SettingsManager.getStartWeekDay();
                int baseColor = SettingsManager.getBaseColor();
                int textColor = SettingsManager.getTextColor();
                Bitmap bitmap = null;
                float height;
                switch (widget) {
                    case WIDGET_0:
                        bitmap = Util.get2DBitmap(context, result, startWeekDay,
                                baseColor, textColor, bitmapWidth, bitmapHeight, false);
                        remoteViews.setImageViewBitmap(R.id.contributions, bitmap);
                        remoteViews.setImageViewBitmap(R.id.contributions_sum,
                                Util.getContributionsSumBitmap(context, SettingsManager.getBaseColor(),
                                        Util.getContributionsSum(result)));
                        break;
                    case WIDGET_1:
                    case WIDGET_8:
                        bitmap = Util.get3DBitmap(context, result, startWeekDay,
                                baseColor, textColor,
                                SettingsManager.getShowMonthDashIn3D(),
                                SettingsManager.getShowWeekdayDashIn3D(), true);
                        remoteViews.setImageViewBitmap(R.id.contributions, bitmap);
                        break;
                    case WIDGET_2:
                    case WIDGET_3:
                    case WIDGET_4:
                    case WIDGET_5:
                    case WIDGET_6:
                    case WIDGET_7:
                        if (widget.equals(Widget.WIDGET_2)
                                || widget.equals(Widget.WIDGET_3)
                                || widget.equals(Widget.WIDGET_6)
                                || widget.equals(Widget.WIDGET_7)) {
                            boolean monthBelow
                                    = widget.equals(Widget.WIDGET_2)
                                    || widget.equals(Widget.WIDGET_6);
                            bitmap = Util.get2DBitmap(context, result, startWeekDay,
                                    baseColor, textColor, bitmapWidth, bitmapHeight,
                                    monthBelow);
                            height = bitmap.getHeight() * 0.8f;
                        } else {
                            bitmap = Util.get3DBitmap(context, result, startWeekDay,
                                    baseColor, textColor,
                                    SettingsManager.getShowMonthDashIn3D(),
                                    SettingsManager.getShowWeekdayDashIn3D(),
                                    false);
                            height = bitmap.getHeight() * 0.2f;
                        }
                        remoteViews.setImageViewBitmap(R.id.contributions, bitmap);
                        remoteViews.setImageViewBitmap(R.id.contributions_sum,
                                Util.getContributionsSumWithLetterBitmap(context, baseColor,
                                        Util.getContributionsSum(result),
                                        Util.getScreenWidth(context) / 5, (int) height));
                        remoteViews.setImageViewBitmap(R.id.contributions_today,
                                Util.getContributionsTodayWithLetterBitmap(context, baseColor,
                                        Util.getContributionsToday(result),
                                        Util.getScreenWidth(context) / 5, (int) height));
                        remoteViews.setImageViewBitmap(R.id.current_streak,
                                Util.getCurrentStreakWithLetterBitmap(context, baseColor,
                                        Integer.valueOf(
                                                Util.getCurrentStreak(
                                                        Util.getContributionsFromString(result))[0]),
                                        Util.getScreenWidth(context) / 5, (int) height));
                        remoteViews.setImageViewBitmap(R.id.motto,
                                Util.getMottoBitmap(context, baseColor,
                                        (int) (Util.getScreenWidth(context)
                                                - Util.getDimen(R.dimen.github_widget_2_avator_size)),
                                        (int )Util.getDimen(R.dimen.github_widget_2_avator_size)));
                        new FollowersTask(widget, remoteViews, context, componentName, appWidgetId,
                                Util.getScreenWidth(context) / 5, (int) height)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                        new StarsTask(widget, remoteViews, context, componentName, appWidgetId,
                                Util.getScreenWidth(context) / 5, (int) height)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                        break;
                }
            }
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetId == -1) {
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        } else {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

}
