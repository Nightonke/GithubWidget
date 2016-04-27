package com.nightonke.githubwidget;

/**
 * Created by Weiping on 2016/4/27.
 */
public enum State {

    SUCCESS(0),
    FAIL(1),
    FIRST_TIME(2);

    int v;

    State(int v) {
        this.v = v;
    }

}
