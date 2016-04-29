package com.nightonke.githubwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Created by Weiping on 2016/4/27.
 */
public class GithubWidgetService extends Service {
    private static final int ALARM_DURATION  = 60 * 60 * 1000;

    private static final int UPDATE_MESSAGE  = 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Service on start");

        updateWidget();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Service on create");
    }

    private void updateWidget() {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                "Send broadcast in service");

        Intent intent = new Intent();
        intent.setAction(Actions.UPDATE_FROM_SERVICE);
        sendBroadcast(intent);

//        long lastUpdateFromServiceTime = PreferenceManager
//                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext())
//                .getLong("LAST_UPDATE_FROM_SERVICE_TIME", -1);
//
//        long nowTime = Calendar.getInstance().getTime().getTime();
//
//        if (lastUpdateFromServiceTime != -1
//                && nowTime - lastUpdateFromServiceTime < SettingsManager.getUpdateTime()) {
//            if (BuildConfig.DEBUG) Log.d("GithubWidget", "Wait for sending broadcast in service");
//        } else {
//            Intent intent = new Intent();
//            intent.setAction(Actions.UPDATE_FROM_SERVICE);
//            sendBroadcast(intent);
//
//            SharedPreferences.Editor editor = PreferenceManager
//                    .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
//            editor.putLong("LAST_UPDATE_FROM_SERVICE_TIME", nowTime);
//            editor.commit();
//        }
    }
}
