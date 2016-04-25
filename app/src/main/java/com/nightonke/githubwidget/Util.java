package com.nightonke.githubwidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by Weiping on 2016/4/25.
 */
public class Util {

    public static int getBaseColor(Context context) {

        return Color.parseColor("#d6e685");

    }

    public static int getScreenWidth(Context context) {
        Display localDisplay
                = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        localDisplay.getSize(point);
        return point.x;
    }

    public final static String WIDTH_STRING = "<svg width=\"";
    public final static int BLOCK_WIDTH = 13;
    public static int getContributionsColumnNumber(String string) {
        int width = Integer.valueOf(
                string.substring(WIDTH_STRING.length(),
                        string.indexOf("\"", WIDTH_STRING.length())));
        return width / BLOCK_WIDTH;
    }

    public final static String FILL_STRING = "fill=\"";
    public final static String DATA_STRING = "data-count=\"";
    public final static String DATE_STRING = "data-date=\"";
    public final static Day[] getContributionsFromString(String string) {
        ArrayList<Day> contributions = new ArrayList<>();
        int fillPos = 0;
        int dataPos = 0;
        int datePos = 0;
        while (true) {
            fillPos = string.indexOf(FILL_STRING, fillPos);
            dataPos = string.indexOf(DATA_STRING, dataPos);
            datePos = string.indexOf(DATE_STRING, datePos);

            int level = 0;
            String levelString
                    = string.substring(fillPos + FILL_STRING.length(),
                    fillPos + FILL_STRING.length() + 7);
            switch (levelString) {
                case "#eeeeee": level = 0; break;
                case "#d6e685": level = 1; break;
                case "#8cc665": level = 2; break;
                case "#44a340": level = 3; break;
                case "#1e6823": level = 4; break;
            }

            int data = 0;
            int dataEndPos = string.indexOf("\"", dataPos + DATA_STRING.length());
            String dataString = string.substring(dataPos + DATA_STRING.length(), dataEndPos);
            data = Integer.valueOf(dataString);

            String dateString
                    = string.substring(datePos + DATE_STRING.length(),
                    datePos + DATE_STRING.length() + 11);

            contributions.add(new Day(
                    Integer.valueOf(dateString.substring(0, 4)),
                    Integer.valueOf(dateString.substring(5, 7)),
                    Integer.valueOf(dateString.substring(8, 10)),
                    level,
                    data
                    ));
        }
    }





































    private static Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }
}
