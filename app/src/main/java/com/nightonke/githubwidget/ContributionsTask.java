package com.nightonke.githubwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
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

    private RemoteViews remoteViews;
    private Context context;
    private ComponentName componentName;
    private int appWidgetId;
    private boolean is2D = true;
    private int bitmapWidth = 0;
    private int bitmapHeight = 0;

    public ContributionsTask(
            RemoteViews remoteViews,
            Context context,
            ComponentName componentName,
            int appWidgetId,
            boolean is2D,
            int bitmapWidth,
            int bitmapHeight) {
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = sp.getString("USER_NAME", null);
        userName = "Nightonke";
        if (userName == null) {
            // user didn't set the user name
            // we just use the default picture
            return null;
        } else {
            String userId = sp.getString("USED_ID", null);
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            if (userId == null) {
                // we haven't got the user id
                try {
                    url = new URL("https://api.github.com/users/" + userName);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
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
                        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Write user basic data: "
                                + byteArrayOutputStream.toString());
                        Util.writeUserBasicData(context, byteArrayOutputStream.toString());
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                } finally{
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }
            }

            try {
                String urlString = "https://github.com/users/"
                        + userName + "/contributions";
                if (BuildConfig.DEBUG)
                    Log.d("GithubWidget", "Get user contributions: " + urlString);
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result == null) {
            if (BuildConfig.DEBUG) Log.d("GithubWidget", "Get user contributions failed");
        } else {
            if (BuildConfig.DEBUG)
                Log.d("GithubWidget", "Get user contributions successfully: " + result);
            int startWeekDay = SettingsManager.getStartWeekDay();
            int baseColor = SettingsManager.getBaseColor();
            int textColor = SettingsManager.getTextColor();
            if (is2D) {
                Bitmap bitmap = Util.get2DBitmap(context, result, startWeekDay,
                        baseColor, textColor, bitmapWidth, bitmapHeight);
                remoteViews.setImageViewBitmap(R.id.contributions, bitmap);
                remoteViews.setTextViewText(R.id.contributions_sum,
                        Util.getContributionsSum(result) + "");
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetId == -1) {
                appWidgetManager.updateAppWidget(componentName, remoteViews);
            } else {
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
        }
    }

}
