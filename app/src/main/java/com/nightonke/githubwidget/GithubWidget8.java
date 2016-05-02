package com.nightonke.githubwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Weiping on 2016/4/26.
 */
public class GithubWidget8 extends AppWidgetProvider {

    private PendingIntent servicePendingIntent = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, this.getClass())).length == 0) return;

        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Receive in widget 8: " + intent.getAction());

        switch (intent.getAction()) {
            case Actions.UPDATE_FROM_SERVICE:
                if (BuildConfig.DEBUG)
                    Log.d("GithubWidget", "Receive in widget 8: Update from service");
                updateAll(context, -1);
                break;
            case Actions.CLICK_AVATAR:
                if (BuildConfig.DEBUG)
                    Log.d("GithubWidget", "Receive in widget 8: Update from manual");
                Util.showToast(R.string.refreshing);
                Util.addAlarmService(context, servicePendingIntent);
                updateAll(context, -1);
                break;
            case Actions.CLICK_LIST:
                int viewIndex = intent.getIntExtra(Actions.LIST_ITEM, 0);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(SettingsManager.getListViewContents().get(viewIndex).get("url")));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
                break;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAll(context, appWidgetId);
        }

        Util.addAlarmService(context, servicePendingIntent);
    }

    @Override
    public void onDisabled(Context context) {
        if (servicePendingIntent == null) return;
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        m.cancel(servicePendingIntent);
    }

    private void updateAll(Context context, int appWidgetId) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "Update all");
        RemoteViews remoteViews
                = new RemoteViews(context.getPackageName(), R.layout.github_widget_8);
        ComponentName componentName = new ComponentName(context, GithubWidget8.class);

        // update avatar
        new AvatarTask(remoteViews, context, componentName, appWidgetId)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        // update contributions and contributions sum
        new ContributionsTask(
                Widget.WIDGET_8, remoteViews,
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
            Intent serviceIntent = new Intent(context, GithubWidgetService.class);
            serviceIntent.setAction(Actions.CLICK_AVATAR);
            PendingIntent pendingServiceIntent = PendingIntent.getService(
                    context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.avatar, pendingServiceIntent);
        }

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_CONTRIBUTIONS);
        remoteViews.setOnClickPendingIntent(R.id.contributions,
                PendingIntent.getActivity(context, 0, intent, 0));

        intent = new Intent(context, SettingsActivity.class);
        intent.setAction(Actions.CLICK_ALL);
        remoteViews.setOnClickPendingIntent(R.id.base_layout,
                PendingIntent.getActivity(context, 0, intent, 0));

        // update contents of list view
        new ListViewContentTask(remoteViews, context, componentName,
                appWidgetId, Widget.WIDGET_8, this.getClass())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetId == -1) {
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        } else {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
