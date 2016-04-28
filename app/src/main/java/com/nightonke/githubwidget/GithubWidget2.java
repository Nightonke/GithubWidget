package com.nightonke.githubwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Weiping on 2016/4/26.
 */
public class GithubWidget2 extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, GithubWidget2.class)).length == 0) return;

        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Receive in widget 2: " + intent.getAction());

        if (intent.getAction().equals(Actions.CLICK_AVATAR)
                || intent.getAction().equals(Actions.CLICK_CONTRIBUTIONS_SUM)) {
            // just update
            if (BuildConfig.DEBUG) Log.d("GithubWidget", "Just update");
            Util.showToast(R.string.refreshing);
            updateAll(context, -1);
        } else if (intent.getAction().equals(Actions.CLICK_CONTRIBUTIONS)) {
            if (BuildConfig.DEBUG) Log.d("GithubWidget", "Go to settings");
            // go to settings
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        context.startService(new Intent(context, GithubWidgetService.class));

        for (int appWidgetId : appWidgetIds) {
            updateAll(context, appWidgetId);
        }
    }

    private void updateAll(Context context, int appWidgetId) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Update all");
        RemoteViews remoteViews
                = new RemoteViews(context.getPackageName(), R.layout.github_widget_2);
        ComponentName componentName = new ComponentName(context, GithubWidget2.class);

        // update avatar
        new AvatarTask(remoteViews, context, componentName, appWidgetId)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        // update contributions and contributions sum
        new ContributionsTask(
                Widget.WIDGET_2, remoteViews,
                context, componentName, appWidgetId, false,
                Util.getScreenWidth(context), 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");


        // set click intent
        Intent intent;
        if (SettingsManager.getUserName() == null) {
            intent = new Intent(context, SettingsActivity.class);
            intent.setAction(Actions.CLICK_AVATAR);
            remoteViews.setOnClickPendingIntent(R.id.avatar,
                    PendingIntent.getActivity(context, 0, intent, 0));
        } else {
            intent = new Intent();
            intent.setAction(Actions.CLICK_AVATAR);
            remoteViews.setOnClickPendingIntent(R.id.avatar,
                    PendingIntent.getBroadcast(context, 0, intent, 0));
        }

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_CONTRIBUTIONS);
        remoteViews.setOnClickPendingIntent(R.id.contributions,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_MOTTO);
        remoteViews.setOnClickPendingIntent(R.id.motto,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_FOLLOWERS);
        remoteViews.setOnClickPendingIntent(R.id.followers,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_CONTRIBUTIONS_SUM);
        remoteViews.setOnClickPendingIntent(R.id.contributions_sum,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_CONTRIBUTIONS_TODAY);
        remoteViews.setOnClickPendingIntent(R.id.contributions_today,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_CURRENT_STREAK);
        remoteViews.setOnClickPendingIntent(R.id.current_streak,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_STARS_TODAY);
        remoteViews.setOnClickPendingIntent(R.id.stars_today,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_ALL);
        remoteViews.setOnClickPendingIntent(R.id.base_layout,
                PendingIntent.getActivity(context, 0, intent, 0));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetId == -1) {
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        } else {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
