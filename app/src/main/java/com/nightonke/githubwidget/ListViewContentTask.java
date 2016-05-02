package com.nightonke.githubwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Weiping on 2016/4/26.
 */

public class ListViewContentTask extends AsyncTask<String, Void, String> {

    private RemoteViews remoteViews;
    private Context context;
    private ComponentName componentName;
    private int appWidgetId;
    private Widget widget;
    private Class<?> c;

    public ListViewContentTask(
            RemoteViews remoteViews,
            Context context,
            ComponentName componentName,
            int appWidgetId,
            Widget widget,
            Class<?> c) {
        this.remoteViews = remoteViews;
        this.context = context;
        this.componentName = componentName;
        this.appWidgetId = appWidgetId;
        this.widget = widget;
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Execute ListViewContentTask");
        // check whether the user id is got
        String userName = SettingsManager.getUserName();
        String result = null;
        if (userName == null) {
            // user didn't set the user name
            // but we can still get the trending
            result = getTrending();
            if (result != null) SettingsManager.setListViewContents(result);
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
                    if(httpURLConnection.getResponseCode() == 200) {
                        InputStream in = httpURLConnection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while((len = in.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, len);
                        }
                        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Write user basic data: "
                                + byteArrayOutputStream.toString());
                        Util.writeUserBasicData(context, byteArrayOutputStream.toString());
                    } else {
                        return null;
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                } finally{
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }
            }
            if (SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_DAILY)
                    || SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_WEEKLY)
                    || SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_MONTHLY))
                result = getTrending();
                if (result != null) SettingsManager.setListViewContents(result);
            if (SettingsManager.getListViewContent().equals(ListViewContent.EVENT)) {
                result = getEvent();
                try {
                    if (result != null) SettingsManager.setListViewContents(new JSONArray(result));
                } catch (JSONException j) {
                    result = null;
                }
            }

        }
        return result;
    }

    private String getTrending() {
        if (SettingsManager.getListViewContent().equals(ListViewContent.EVENT)) return null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            String urlString = "";
            if (SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_DAILY))
                urlString = "https://github.com/trending/"
                        + SettingsManager.getLanguage().v + "?since=daily";
            if (SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_WEEKLY))
                urlString = "https://github.com/trending/"
                        + SettingsManager.getLanguage().v + "?since=weekly";
            if (SettingsManager.getListViewContent().equals(ListViewContent.TRENDING_MONTHLY))
                urlString = "https://github.com/trending/"
                        + SettingsManager.getLanguage().v + "?since=monthly";
            urlString = urlString.replace(" ", "%20");
            if (BuildConfig.DEBUG)
                Log.d("GithubWidget", "Get trending: " + urlString);
            url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == 200) {
                InputStream in = httpURLConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = in.read(buffer)) != -1) {
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
        return null;
    }

    private String getEvent() {
        if (SettingsManager.getUserId() == -1) return null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            String urlString = "https://api.github.com/users/" + SettingsManager.getUserName()
                    + "/received_events?per_page=" + SettingsManager.getReceivedEventPerPage();
            if (BuildConfig.DEBUG)
                Log.d("GithubWidget", "Get user received event: " + urlString);
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
                while((len = in.read(buffer)) != -1) {
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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result == null) {
            // do nothing
            Util.log("Get trending fail");
        } else {
            Intent intent = new Intent(context, WidgetListViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(R.id.list_view, intent);

            // Todo set empty view

            Intent toastIntent = new Intent(context, GithubWidget6.class);
            toastIntent.setAction(Actions.CLICK_LIST);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);

            if (appWidgetId == -1) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                switch (widget) {
                    case WIDGET_6:
                    case WIDGET_7:
                        int[] appWidgetIds =
                                appWidgetManager.getAppWidgetIds(new ComponentName(context, c));
                        for (int id : appWidgetIds) {
                            AppWidgetManager.getInstance(context).updateAppWidget(id, null);
                            AppWidgetManager.getInstance(context).updateAppWidget(id, remoteViews);
                            AppWidgetManager.getInstance(context)
                                    .notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
                        }
                        break;
                }
                appWidgetManager.updateAppWidget(componentName, remoteViews);
            } else {
                AppWidgetManager.getInstance(context)
                        .notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
                AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
            }
        }
    }

}
