package com.nightonke.githubwidget;

/**
 * Created by Weiping on 2016/4/25.
 */
public class Day {

    public int year = -1;
    public int month = -1;
    public int day = -1;

    // Level is used to record the color of the block
    public int level = -1;
    // Data is used to calculated the height of the pillar
    public int data = -1;

    public Day(int year, int month, int day, int level, int data) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.level = level;
        this.data = data;
    }
}
