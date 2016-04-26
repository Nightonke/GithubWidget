package com.nightonke.githubwidget;

import android.app.Application;
import android.content.Context;

/**
 * Created by Weiping on 2016/4/26.
 */
public class GithubWidgetApplication extends Application {

    private static Context mContext;

    @Override public void onCreate() {
        super.onCreate();

        GithubWidgetApplication.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return GithubWidgetApplication.mContext;
    }

}
