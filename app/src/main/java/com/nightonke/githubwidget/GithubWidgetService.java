package com.nightonke.githubwidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Weiping on 2016/4/27.
 */
public class GithubWidgetService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Service on start");
        if (SettingsManager.getUpdateTime() == Integer.MAX_VALUE) {
            if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                    "Auto-update set to false in settings, just return");
            return super.onStartCommand(intent, flags, startId);
        }

        if (intent != null && intent.getAction() != null
                && intent.getAction().equals(Actions.CLICK_AVATAR)) updateWidgetManually();
        else updateWidgetAutomatically();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Service on create");
    }

    private void updateWidgetAutomatically() {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                "Send broadcast in service automatically");

        Intent intent = new Intent();
        intent.setAction(Actions.UPDATE_FROM_SERVICE);
        sendBroadcast(intent);
    }

    private void updateWidgetManually() {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                "Send broadcast in service manually");

        Intent intent = new Intent();
        intent.setAction(Actions.CLICK_AVATAR);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                "Service destroy");
    }
}
