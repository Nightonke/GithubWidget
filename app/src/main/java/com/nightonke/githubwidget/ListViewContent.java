package com.nightonke.githubwidget;

/**
 * Created by Weiping on 2016/4/30.
 */
public enum ListViewContent {

    TRENDING_DAILY(0),
    TRENDING_WEEKLY(1),
    EVENT(2);

    int v;

    ListViewContent(int v) {
        this.v = v;
    }

}
