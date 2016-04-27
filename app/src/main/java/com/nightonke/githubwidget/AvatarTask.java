package com.nightonke.githubwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

public class AvatarTask extends AsyncTask<String, Void, Boolean> {

    private RemoteViews remoteViews;
    private Context context;
    private ComponentName componentName;
    private int appWidgetId;
    private Bitmap bitmap;

    public AvatarTask(
            RemoteViews remoteViews,
            Context context,
            ComponentName componentName,
            int appWidgetId) {
        this.remoteViews = remoteViews;
        this.context = context;
        this.componentName = componentName;
        this.appWidgetId = appWidgetId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Execute AvatarTask");
        // check whether the user id is got
        String userName = SettingsManager.getUserName();
        if (userName == null) {
            // user didn't set the user name
            // we just use the default picture
            return false;
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
                String urlString = "https://avatars.githubusercontent.com/u/"
                        + SettingsManager.getUserId() + "?s=" + Util.dp2px(context.getResources().getDimension(
                        R.dimen.github_widget_0_avator_size));
                if (BuildConfig.DEBUG) Log.d("GithubWidget", "Get avatar bitmap: " + urlString);
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                if (BuildConfig.DEBUG) Log.d("GithubWidget", "Get the avatar bitmap");
                bitmap = BitmapFactory.decodeStream(in);
                return true;
            } catch (IOException i) {
                i.printStackTrace();
            } finally{
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean ok) {
        super.onPostExecute(ok);
        if (ok) remoteViews.setImageViewBitmap(R.id.avatar,
                Util.getRoundBitmap(bitmap));
        else remoteViews.setImageViewBitmap(R.id.avatar,
                Util.getRoundBitmap(
                        Util.decodeSampledBitmapFromResource(
                                context.getResources(),
                                R.drawable.default_avatar,
                                Util.dp2px(context.getResources().getDimension(
                                        R.dimen.github_widget_0_avator_size)),
                                Util.dp2px(context.getResources().getDimension(
                                        R.dimen.github_widget_0_avator_size)))));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetId == -1) {
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        } else {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

}
