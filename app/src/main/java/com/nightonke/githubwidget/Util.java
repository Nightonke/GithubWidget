package com.nightonke.githubwidget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.RemoteViews;

import com.github.johnpersano.supertoasts.SuperToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Weiping on 2016/4/25.
 */

public class Util {

    public static final int HALF_AN_HOUR = 30 * 60 * 1000;
    public static final float WIDGET_2_NUMBER_HEIGHT = 50f;
    public static final float WIDGET_2_LETTER_HEIGHT = 25f;
    public static final float WIDGET_2_LETTER_PADDING_BOTTOM = 20f;

    public static final String TAG = "GithubWidget";

    public static final String LIST_VIEW_CONTENTS_URL_PREFIX = "https://github.com/";

    public static int getScreenWidth(Context context) {
        Display localDisplay
                = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        localDisplay.getSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        Display localDisplay
                = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        localDisplay.getSize(point);
        return point.y;
    }

    public final static String WIDTH_STRING = "<svg width=\"";
    public final static int BLOCK_WIDTH = 13;
    public static int getContributionsColumnNumber(String string) {
        int width = Integer.valueOf(
                string.substring(
                        string.indexOf(WIDTH_STRING) + WIDTH_STRING.length(),
                        string.indexOf("\"", WIDTH_STRING.length() + 1)));
        return width / BLOCK_WIDTH;
    }

    public final static String FILL_STRING = "fill=\"";
    public final static String DATA_STRING = "data-count=\"";
    public final static String DATE_STRING = "data-date=\"";
    public static ArrayList<Day> getContributionsFromString(String string) {
        ArrayList<Day> contributions = new ArrayList<>();
        int fillPos = -1;
        int dataPos = -1;
        int datePos = -1;
        while (true) {
            fillPos = string.indexOf(FILL_STRING, fillPos + 1);
            dataPos = string.indexOf(DATA_STRING, dataPos + 1);
            datePos = string.indexOf(DATE_STRING, datePos + 1);

            if (fillPos == -1) break;

            int level = 0;
            String levelString
                    = string.substring(fillPos + FILL_STRING.length(),
                    fillPos + FILL_STRING.length() + 7);
            switch (levelString) {
                case "#ebedf0": level = 0; break;
                case "#c6e48b": level = 1; break;
                case "#7bc96f": level = 2; break;
                case "#239a3b": level = 3; break;
                case "#196127": level = 4; break;
            }

            int dataEndPos = string.indexOf("\"", dataPos + DATA_STRING.length());
            String dataString = string.substring(dataPos + DATA_STRING.length(), dataEndPos);
            int data = Integer.valueOf(dataString);

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

        return contributions;
    }

    /**
     * Get the day of week from a date.
     * 0 for SUN.
     * 1 for MON.
     * .
     * .
     * .
     * 6 for SAT.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of month of the date.
     * @return Integer to determine the day of week.
     */
    public static int getWeekDayFromDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * Get the short month name for a certain date.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return The short name of the month.
     */
    public static String getShortMonthName(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.US);
        return month_date.format(calendar.getTime());
    }

    /**
     * Get the full month name for a certain date.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return The full name of the month.
     */
    public static String getFullMonthName(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.US);
        return month_date.format(calendar.getTime());
    }

    /**
     * Get the full month and day name for a certain date.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return The full name of the month and day.
     */
    public static String getFullMonthDayName(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.US);
        return month_date.format(calendar.getTime()) + " " + day;
    }

    /**
     * Get the short year, month and day name for a certain date.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return The short name of the year, month and day.
     */
    public static String getShortDateName(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.US);
        return month_date.format(calendar.getTime()) + " " + day + ", " + year;
    }

    /**
     * Get the first letter of a weekday.
     *
     * @param weekDay Integer from 0 to 6 for weekday.
     * @return The first letter for the weekday.
     */
    public static String getWeekdayFirstLetter(int weekDay) {
        switch (weekDay) {
            case 0: return "S";
            case 1: return "M";
            case 2: return "T";
            case 3: return "W";
            case 4: return "T";
            case 5: return "F";
            case 6: return "S";
            default: return "";
        }
    }

    /**
     * Dp to px.
     *
     * @param dp Value of dp.
     * @return Value of px.
     */
    public static int dp2px(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * Decode a resource to bitmap width sample size.
     *
     * @param resources Resources.
     * @param resId The id of the drawable.
     * @param reqWidth Width we need, in px.
     * @param reqHeight Height we need, in px.
     * @return The bitmap we loaded.
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources resources,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * Calculate the sample size.
     *
     * @param options Bitmap options.
     * @param reqWidth Width we need, in px.
     * @param reqHeight Height we need, in px.
     * @return Sample size.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Write the basic data of user.
     *
     * @param context Context.
     * @param string Data.
     */
    @SuppressLint("CommitPrefEdits")
    public static void writeUserBasicData(Context context, String string) {
        SharedPreferences.Editor editor
                = PreferenceManager.getDefaultSharedPreferences(context).edit();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(string);
            SettingsManager.setUserId(jsonObject.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    /**
     * Get a 2D contributions bitmap.
     *
     * @param context Context.
     * @param data Data, from http get.
     * @param startWeekDay Start weekday.
     * @param baseColor Base color.
     * @param textColor Text color.
     * @param bitmapWidth Bitmap width.
     * @param bitmapHeight Bitmap height, this is useless. Because we calculate the height by width.
     * @param monthBelow Whether the months text is below the blocks.
     * @return The bitmap.
     */
    public static Bitmap get2DBitmap(
            Context context,
            String data,
            Weekday startWeekDay,
            int baseColor,
            int textColor,
            int bitmapWidth,
            int bitmapHeight,
            boolean monthBelow) {
        Bitmap bitmap;
        Canvas canvas;
        Paint blockPaint;
        Paint monthTextPaint;
        Paint weekDayTextPaint;

        ArrayList<Day> contributions = Util.getContributionsFromString(data);
        int horizontalBlockNumber = Util.getContributionsColumnNumber(data);
        int verticalBlockNumber = 7;
        float ADJUST_VALUE = 0.8f;
        float blockWidth = bitmapWidth / (ADJUST_VALUE + horizontalBlockNumber) * (1.0F - 0.1F);
        float spaceWidth = bitmapWidth / (ADJUST_VALUE + horizontalBlockNumber) - blockWidth;
        float monthTextHeight = blockWidth * 1.5F;
        float weekTextHeight = blockWidth;
        float topMargin = monthBelow ? 15f : 7f;
        bitmapHeight = (int)(monthTextHeight + topMargin
                + verticalBlockNumber * (blockWidth + spaceWidth));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.FILL);

        monthTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        monthTextPaint.setTextSize(monthTextHeight);
        monthTextPaint.setColor(textColor);
        monthTextPaint.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        weekDayTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekDayTextPaint.setTextSize(weekTextHeight);
        weekDayTextPaint.setColor(textColor);
        weekDayTextPaint.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        // draw the text for weekdays
        float textStartHeight = (monthBelow ? 0 : monthTextHeight + topMargin)
                + blockWidth + spaceWidth;
        Paint.FontMetricsInt fontMetrics = monthTextPaint.getFontMetricsInt();
        float baseline = (
                textStartHeight + blockWidth +
                        textStartHeight -
                        fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(Util.getWeekdayFirstLetter((startWeekDay.v + 1) % 7),
                0, baseline, weekDayTextPaint);
        canvas.drawText(Util.getWeekdayFirstLetter((startWeekDay.v + 3) % 7),
                0, baseline + 2 * (blockWidth + spaceWidth), weekDayTextPaint);
        canvas.drawText(Util.getWeekdayFirstLetter((startWeekDay.v + 5) % 7),
                0, baseline + 4 * (blockWidth + spaceWidth), weekDayTextPaint);

        // draw the blocks
        int currentWeekDay = Util.getWeekDayFromDate(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day);
        float x = weekTextHeight + topMargin;
        float y = (currentWeekDay - startWeekDay.v + 7) % 7
                * (blockWidth + spaceWidth)
                + (monthBelow ? 0 : topMargin + monthTextHeight);
        int lastMonth = contributions.get(0).month - 1;
        for (Day day : contributions) {
            blockPaint.setColor(Util.calculateLevelColor(baseColor, day.level));
            canvas.drawRect(x, y, x + blockWidth, y + blockWidth, blockPaint);

            currentWeekDay = (currentWeekDay + 1) % 7;
            if (currentWeekDay == startWeekDay.v) {
                // another column
                x += blockWidth + spaceWidth;
                y = monthBelow ? 0 : topMargin + monthTextHeight;
                if (!monthBelow && day.month != lastMonth) {
                    // judge whether we should draw the text of month
                    canvas.drawText(
                            Util.getShortMonthName(day.year, day.month, day.day),
                            x, monthTextHeight, monthTextPaint);
                    lastMonth = day.month;
                }
            } else {
                y += blockWidth + spaceWidth;
                if (monthBelow && currentWeekDay == (startWeekDay.v + 6) % 7
                        && day.month != lastMonth) {
                    // judge whether we should draw the text of month
                    canvas.drawText(
                            Util.getShortMonthName(day.year, day.month, day.day),
                            x, y + monthTextHeight + topMargin, monthTextPaint);
                    lastMonth = day.month;
                }
            }
        }

        return bitmap;
    }

    /**
     * Calculate the sum of contributions from xml.
     *
     * @param data The string of data.
     * @return The sum of contributions.
     */
    public static int getContributionsSum(String data) {
        int sum = 0;
        int dataPos = -1;
        while (true) {
            dataPos = data.indexOf(DATA_STRING, dataPos + 1);

            if (dataPos == -1) break;

            int dataEndPos = data.indexOf("\"", dataPos + DATA_STRING.length());
            String dataString = data.substring(dataPos + DATA_STRING.length(), dataEndPos);
            sum += Integer.valueOf(dataString);
        }
        return sum;
    }

    /**
     * Get round bitmap from normal bitmap.
     *
     * @param bitmap Normal bitmap.
     * @return Round bitmap.
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        int size = Math.min((bitmap.getWidth()), (bitmap.getHeight()));

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, size, size);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static int lastToast = -1;

    /**
     * Show toast.
     *
     * @param text Resource id of the text.
     */
    public static void showToast(int text) {
        if (!SettingsManager.getShowToast() || text == lastToast) return;
        lastToast = text;
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(GithubWidgetApplication.getAppContext());
        superToast.setAnimations(SuperToast.Animations.FLYIN);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(GithubWidgetApplication.getAppContext().getResources().getString(text));
        superToast.setBackground(SuperToast.Background.RED);
        superToast.setOnDismissListener(new SuperToast.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                lastToast = -1;
            }
        });
        superToast.show();
    }

    /**
     * Get the number of weekdays in the first week.
     *
     * @param contributions Days.
     * @param startWeekday Start from this weekday.
     * @return The number of weekdays in the first week.
     */
    public static int getFirstWeekDaysNumber(ArrayList<Day> contributions, Weekday startWeekday) {
        int sum = 0;
        for (Day day : contributions) {
            sum++;
            if (getWeekDayFromDate(day.year, day.month, day.day) == startWeekday.v && sum != 0)
                break;
        }
        return sum;
    }

    /**
     * Get the number of weekdays in the last week.
     *
     * @param contributions Days.
     * @param startWeekday Start from this weekday.
     * @return The number of weekdays in the last week.
     */
    public static int getLastWeekDaysNumber(ArrayList<Day> contributions, Weekday startWeekday) {
        int sum = 0;
        for (int i = contributions.size() - 1; i >= 0; i--) {
            Day day = contributions.get(i);
            sum++;
            if (getWeekDayFromDate(day.year, day.month, day.day) == startWeekday.v && sum != 0)
                break;
        }
        return sum;
    }

    /**
     * Calculate the red value for different level.
     *
     * @param baseR Red value of base color.
     * @param level Level.
     * @return The red value for the level of the base color.
     */
    public static int calculateR(int baseR, int level) {
        switch (level) {
            case 0: return 238;
            case 1: return baseR;
            case 2: return (int) (baseR * (9 + 46 + 15) / (37f + 9 + 46 + 15));
            case 3: return (int) (baseR * (46 + 15) / (37f + 9 + 46 + 15));
            case 4: return (int) (baseR * (15) / (37f + 9 + 46 + 15));
            default: return 238;
        }
    }

    /**
     * Calculate the green value for different level.
     *
     * @param baseG Green value of base color.
     * @param level Level.
     * @return The green value for the level of the base color.
     */
    public static int calculateG(int baseG, int level) {
        switch (level) {
            case 0: return 238;
            case 1: return baseG;
            case 2: return (int) (baseG * (35 + 59 + 104) / (32f + 35 + 59 + 104));
            case 3: return (int) (baseG * (59 + 104) / (32f + 35 + 59 + 104));
            case 4: return (int) (baseG * (104) / (32f + 35 + 59 + 104));
            default: return 238;
        }
    }

    /**
     * Calculate the blue value for different level.
     *
     * @param baseB Blue value of base color.
     * @param level Level.
     * @return The blue value for the level of the base color.
     */
    public static int calculateB(int baseB, int level) {
        switch (level) {
            case 0: return 238;
            case 1: return baseB;
            case 2: return (int) (baseB * (37 + 29 + 35) / (32f + 37 + 29 + 35));
            case 3: return (int) (baseB * (29 + 35) / (32f + 37 + 29 + 35));
            case 4: return (int) (baseB * (35) / (32f + 37 + 29 + 35));
            default: return 238;
        }
    }

    /**
     * Calculate the value for different color.
     *
     * @param baseColor Value of base color.
     * @param level Level.
     * @return The value for the level of the base color.
     */
    public static int calculateLevelColor(int baseColor, int level) {
        return Color.rgb(
                calculateR(Color.red(baseColor), level),
                calculateG(Color.green(baseColor), level),
                calculateB(Color.blue(baseColor), level));
    }

    /**
     * Calculate the shadow color face left-bottom corner.
     *
     * @param baseColor The base color.
     * @return The shadow color.
     */
    public static int calculateShadowColorLeftBottom(int baseColor) {
        return Color.rgb(
                (int)(Color.red(baseColor) * 173f / 214),
                (int)(Color.green(baseColor) * 209f / 230),
                (int)(Color.blue(baseColor) * 113f / 133));
    }

    /**
     * Calculate the shadow color face right-bottom corner.
     *
     * @param baseColor The base color.
     * @return The shadow color.
     */
    public static int calculateShadowColorRightBottom(int baseColor) {
        return Color.rgb(
                (int)(Color.red(baseColor) * 193f / 214),
                (int)(Color.green(baseColor) * 219f / 230),
                (int)(Color.blue(baseColor) * 93f / 133));
    }

    /**
     * Get the text width from its text paint and itself.
     *
     * @param textPaint The text paint of the text.
     * @param text The text.
     * @return Width, in px.
     */
    public static int getTextWidth(Paint textPaint, String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * Get the text height from its text paint and itself.
     *
     * @param textPaint The text paint of the text.
     * @param text The text.
     * @return Height, in px.
     */
    public static int getTextHeight(Paint textPaint, String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * Build a text paint.
     *
     * @param textSize Text size.
     * @param textColor The color of the text.
     * @param typeface Typeface of the text.
     * @return The paint.
     */
    public static Paint getTextPaint(float textSize, int textColor, Typeface typeface) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTypeface(typeface);
        return paint;
    }

    /**
     * Get string from resource id of string.
     *
     * @param resId Resource id.
     * @return The string.
     */
    public static String getString(int resId) {
        return GithubWidgetApplication.getAppContext().getResources().getString(resId);
    }

    /**
     * Get the longest streak in contributions.
     *
     * @param contributions The contributions array.
     * @return Longest streak, start date and end date, day(s).
     */
    public static String[] getLongestStreak(ArrayList<Day> contributions) {
        int streak = 0;
        int longestStreak = 0;
        int longestStart = 0;
        int longestEnd = 0;
        for (int i = 0; i < contributions.size(); i++) {
            Day day = contributions.get(i);
            if (day.level != 0) streak++;
            if (day.level == 0 || i == contributions.size() - 1) {
                if (streak > longestStreak) {
                    longestStreak = streak;
                    longestEnd = day.level == 0 ? i - 1 : i;
                    longestStart = longestEnd - longestStreak + 1;
                }
                streak = 0;
            }
        }

        String remarkString = getString(R.string.no_contributions_this_year);
        if (longestStreak != 0) {
            remarkString = Util.getFullMonthDayName(
                    contributions.get(longestStart).year,
                    contributions.get(longestStart).month,
                    contributions.get(longestStart).day)
                    + getString(R.string.remark_to)
                    + Util.getFullMonthDayName(
                    contributions.get(longestEnd).year,
                    contributions.get(longestEnd).month,
                    contributions.get(longestEnd).day);
        }
        String days = getString(longestStreak == 1 ? R.string.day : R.string.days);

        return new String[]{longestStreak + "", remarkString, days};
    }

    /**
     * Get current streak.
     *
     * @param contributions The contributions array.
     * @return Current streak, start date and end date, day(s).
     */
    public static String[] getCurrentStreak(ArrayList<Day> contributions) {
        int streak = 0;
        int currentStreakStart = 0;
        int lastStreak = -1;
        for (int i = contributions.size() - 1; i >= 0; i--) {
            if (contributions.get(i).level != 0) {
                streak++;
            } else {
                currentStreakStart = i + 1;
                break;
            }
        }
        for (int i = currentStreakStart - 1; i >= 0; i--) {
            if (contributions.get(i).level != 0) {
                lastStreak = i;
            }
        }

        String remarkString = getString(R.string.no_contributions_this_year);
        if (streak != 0) {
            remarkString = Util.getFullMonthDayName(
                    contributions.get(currentStreakStart).year,
                    contributions.get(currentStreakStart).month,
                    contributions.get(currentStreakStart).day)
                    + getString(R.string.remark_to)
                    + Util.getFullMonthDayName(
                    contributions.get(contributions.size() - 1).year,
                    contributions.get(contributions.size() - 1).month,
                    contributions.get(contributions.size() - 1).day);
        } else if (lastStreak != -1) {
            int days = contributions.size() - lastStreak + 1;
            remarkString
                    = getString(R.string.last_contributed) + days + getString(R.string.days_ago);
        }
        String days = getString(streak == 1 ? R.string.day : R.string.days);

        return new String[]{streak + "", remarkString, days};
    }

    /**
     * Get one year total.
     *
     * @param contributions The contributions array.
     * @return One year total, start date and end date of this year, contribution(s).
     */
    public static String[] getOneYearTotal(ArrayList<Day> contributions) {
        int total = 0;
        for (Day day : contributions) total += day.data;

        String remarkString = Util.getShortDateName(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day)
                + getString(R.string.remark_to)
                + Util.getShortDateName(
                contributions.get(contributions.size() - 1).year,
                contributions.get(contributions.size() - 1).month,
                contributions.get(contributions.size() - 1).day);
        String contributionsString
                = getString(total == 1 ? R.string.contribution : R.string.contributions);

        return new String[]{total + "", remarkString, contributionsString};
    }

    /**
     * Get busiest day.
     *
     * @param contributions The contributions array.
     * @return Contributions on the busiest day, the date of it, contribution(s).
     */
    public static String[] getBusiestDay(ArrayList<Day> contributions) {
        int max = 0;
        int maxDay = 0;
        for (int i = 0; i < contributions.size(); i++) {
            if (contributions.get(i).data > max) {
                max = contributions.get(i).data;
                maxDay = i;
            }
        }

        String remarkString = getString(R.string.no_contributions_this_year);
        if (max != 0) {
            remarkString = Util.getFullMonthDayName(
                    contributions.get(maxDay).year,
                    contributions.get(maxDay).month,
                    contributions.get(maxDay).day);
        }
        String contributionsString
                = getString(max == 1 ? R.string.contribution : R.string.contributions);

        return new String[]{max + "", remarkString, contributionsString};
    }

    /**
     * Get a 3D bitmap for contributions.
     *
     * @param context Context.
     * @param data Data, from http get.
     * @param startWeekday Start weekday.
     * @param baseColor Base color.
     * @param textColor Text color.
     * @param drawMonthText Whether draw the dashes and text of months.
     * @param drawWeekdayText Whether draw the dashes and text for weekdays.
     * @param containsAvatar Whether contain an avatar. Notice that this method does NOT draw an
     *                       avatar, but just leave space for it.
     * @return The bitmap.
     */
    public static Bitmap get3DBitmap(
            Context context,
            String data,
            Weekday startWeekday,
            int baseColor,
            int textColor,
            boolean drawMonthText,
            boolean drawWeekdayText,
            boolean containsAvatar) {
        Bitmap bitmap;
        Canvas canvas;
        Paint blockPaint;
        Paint monthTextPaint;
        Paint weekdayTextPaint;
        Paint titlePaint;
        Paint numberPaint;
        Paint unitPaint;
        Paint remarkPaint;
        Paint dash;
        ArrayList<Day> contributions = getContributionsFromString(data);

        int bitmapWidth = Util.getScreenWidth(context);
        // Todo calculate the height
        int bitmapHeight = (int) (bitmapWidth * 0.75f);
        int columnNumber = Util.getContributionsColumnNumber(data);
        int n = columnNumber - 2;
        float paddingLeft = 10f;
        float paddingRight = 10f;
        float paddingTop = 10f;
        float paddingBottom = 10f;
        float emptyHeight = 5f;
        float a1 = 38f;
        float a2 = 22f;
        float rate = 1 / 6f;
        float cosa1 = (float)Math.cos(Math.toRadians(a1));
        float cosa2 = (float)Math.cos(Math.toRadians(a2));
        float ls = (bitmapWidth - paddingLeft - paddingRight)
                /
                ((1 / (1 + rate)) + 7 * cosa1 + (n + 1) * cosa2);
        float lscosa1 = (float)Math.cos(Math.toRadians(a1)) * (ls);
        float lscosa2 = (float)Math.cos(Math.toRadians(a2)) * (ls);
        float lssina1 = (float)Math.sin(Math.toRadians(a1)) * (ls);
        float lssina2 = (float)Math.sin(Math.toRadians(a2)) * (ls);
        float l = ls / (1 + rate);
        float lcosa1 = (float)Math.cos(Math.toRadians(a1)) * l;
        float lcosa2 = (float)Math.cos(Math.toRadians(a2)) * l;
        float lsina1 = (float)Math.sin(Math.toRadians(a1)) * l;
        float lsina2 = (float)Math.sin(Math.toRadians(a2)) * l;
        float s = l * rate;
        float topFaceheight = lsina1 + lsina2;
        float x6 = paddingLeft + lcosa2;
        float x0 = x6 + 7 * lscosa1;
        float monthDashLength = 45f;
        float dashTextHeight = (drawMonthText || drawWeekdayText) ? monthDashLength : 0;
        float maxHeight = bitmapHeight - paddingTop - topFaceheight - lssina2 * (n + 1)
                - 6 * lssina1 - dashTextHeight - paddingBottom;
        float y0 = paddingTop + topFaceheight + maxHeight + 2 * emptyHeight;
        int currentWeekDay = Util.getWeekDayFromDate(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day);
        float x = x0 - ((currentWeekDay - startWeekday.v + 7) % 7) * lscosa1;
        float y = y0 + ((currentWeekDay - startWeekday.v + 7) % 7) * lssina1;
        float textHeight = l;
        float maxData = 0;
        for (Day day : contributions) maxData = Math.max(maxData, day.data);
        int endWeekday = (startWeekday.v - 1 + 7) % 7;
        float monthDashAndTextPadding = 5f;
        float weekdayDashAndTextPadding = 5f;
        float lastMonthDashEndX = 0;
        float lastMonthDashEndY = 0;
        float maxY = 0;

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.FILL);

        monthTextPaint = Util.getTextPaint(textHeight, textColor,
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        weekdayTextPaint = Util.getTextPaint(textHeight, textColor,
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        titlePaint = Util.getTextPaint(1.5f * textHeight, textColor,
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        numberPaint = Util.getTextPaint(4 * textHeight, Util.calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        unitPaint = Util.getTextPaint(1.5f * textHeight, textColor,
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        remarkPaint = Util.getTextPaint(1.5f * textHeight, textColor,
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        dash = new Paint(Paint.ANTI_ALIAS_FLAG);
        dash.setARGB(255, 0, 0,0);
        dash.setStyle(Paint.Style.STROKE);
        dash.setColor(textColor);
        dash.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));

        int lastMonth = contributions.get(0).month;
        for (int i = 0; i < contributions.size(); i++) {
            Day day = contributions.get(i);
            float height;
            if (day.data == 0) height = emptyHeight;
            else height = maxHeight * day.data / maxData + 2 * emptyHeight;

            // face right-bottom corner
            blockPaint.setColor(
                    Util.calculateShadowColorRightBottom(
                            Util.calculateLevelColor(baseColor, day.level)));
            Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + lcosa1, y - lsina1);
            path.lineTo(x + lcosa1, y - lsina1 - height);
            path.lineTo(x, y - height);
            path.lineTo(x, y);
            canvas.drawPath(path, blockPaint);

            // face left-bottom corner
            blockPaint.setColor(
                    Util.calculateShadowColorLeftBottom(
                            Util.calculateLevelColor(baseColor, day.level)));
            path = new Path();
            path.moveTo(x, y);
            path.lineTo(x - lcosa2, y - lsina2);
            path.lineTo(x - lcosa2, y - lsina2 - height);
            path.lineTo(x, y - height);
            path.lineTo(x, y);
            canvas.drawPath(path, blockPaint);

            // face top
            blockPaint.setColor(Util.calculateLevelColor(baseColor, day.level));
            path = new Path();
            path.moveTo(x, y - height);
            path.lineTo(x + lcosa1, y - lsina1 - height);
            path.lineTo(x + lcosa1 - lcosa2, y - lsina1 - height - lsina2);
            path.lineTo(x - lcosa2, y - lsina2 - height);
            path.lineTo(x, y - height);
            canvas.drawPath(path, blockPaint);

            if (i == contributions.size() - 1) break;

            currentWeekDay = (currentWeekDay + 1) % 7;
            if (currentWeekDay == startWeekday.v) {
                // another column
                x = x + 6 * lscosa1 + lscosa2;
                y = y - 6 * lssina1 + lssina2;
            } else {
                x -= lscosa1;
                y += lssina1;
                lastMonthDashEndX = x - lcosa2;
                lastMonthDashEndY = y - lsina2 + monthDashLength;
                if (currentWeekDay == endWeekday && day.month != lastMonth && drawMonthText) {
                    canvas.drawLine(x - lcosa2, y - lsina2,
                            lastMonthDashEndX, lastMonthDashEndY, dash);
                    canvas.drawText(
                            Util.getShortMonthName(day.year, day.month, day.day),
                            lastMonthDashEndX + monthDashAndTextPadding,
                            lastMonthDashEndY,
                            monthTextPaint);
                    lastMonth = day.month;
                }
            }

            maxY = Math.max(y, maxY);
        }
        if (drawMonthText || drawWeekdayText) maxY = lastMonthDashEndY;

        if (drawWeekdayText) {
            int lastWeekdaysNumber = Util.getLastWeekDaysNumber(contributions, startWeekday);
            x += (lastWeekdaysNumber - 1) * lscosa1;
            y -= (lastWeekdaysNumber - 1) * lssina1;
            float dashLength = lastMonthDashEndY - (y + 4 * lssina1);
            for (int i = 2; i >= 0; i--) {
                int drewWeekday = (startWeekday.v + 1 + 2 * i) % 7;
                String weekdayString = Util.getWeekdayFirstLetter(drewWeekday);
                canvas.drawText(
                        weekdayString,
                        x - 2 * i * lscosa1
                                - Util.getTextWidth(weekdayTextPaint, weekdayString)
                                - weekdayDashAndTextPadding,
                        y + (1 + 2 * i) * lssina1 + dashLength,
                        weekdayTextPaint);
                if (2 * i >= lastWeekdaysNumber) {
                    Path path = new Path();
                    path.moveTo(
                            x - 2 * i * lscosa1,
                            y + (1 + 2 * i) * lssina1 + dashLength);
                    path.lineTo(x - 2 * i * lscosa1, y + 2 * i * lssina1);
//                    path.lineTo(
//                            x - 2 * i * lscosa1 - lscosa2,
//                            y + 2 * i * lssina1 - lssina2);
                    canvas.drawPath(path, dash);
                } else {
                    canvas.drawLine(
                            x - 2 * i * lscosa1,
                            y + (1 + 2 * i) * lssina1 + dashLength,
                            x - 2 * i * lscosa1,
                            y + 2 * i * lssina1, dash);
                }
            }
        }

        float partPadding = 2 * textHeight;
        float textLeftPadding = 20f;
        float titleAndNumberPadding = 8f;
        float numberAndRemarkPadding = 10f;
        float unitAndRemarkPadding = 5f;
        float avatarWidth = containsAvatar ? bitmapWidth / 4.5f : 0;

        // draw current streak text
        String[] currentStreaks = Util.getCurrentStreak(contributions);
        float numberHeight = Util.getTextHeight(numberPaint, currentStreaks[0]);
        float numberWidth = Util.getTextWidth(numberPaint, currentStreaks[0]);
        float titleWidth = Util.getTextWidth(titlePaint, Util.getString(R.string.current_streak));
        float remarkHeight = Util.getTextHeight(remarkPaint, currentStreaks[1]);
        canvas.drawText(Util.getString(R.string.current_streak),
                textLeftPadding,
                maxY - numberHeight - titleAndNumberPadding,
                titlePaint);
        canvas.drawText(currentStreaks[0],
                textLeftPadding + titleWidth - numberWidth,
                maxY,
                numberPaint);
        canvas.drawText(currentStreaks[1],
                textLeftPadding + titleWidth + numberAndRemarkPadding,
                maxY,
                remarkPaint);
        canvas.drawText(currentStreaks[2],
                textLeftPadding + titleWidth + numberAndRemarkPadding,
                maxY - remarkHeight - unitAndRemarkPadding,
                unitPaint);

        // draw longest streak text
        String[] longestStreaks = Util.getLongestStreak(contributions);
        numberHeight = Util.getTextHeight(numberPaint, longestStreaks[0]);
        numberWidth = Util.getTextWidth(numberPaint, longestStreaks[0]);
        titleWidth = Util.getTextWidth(titlePaint, Util.getString(R.string.longest_streak));
        remarkHeight = Util.getTextHeight(remarkPaint, longestStreaks[1]);
        canvas.drawText(Util.getString(R.string.longest_streak),
                textLeftPadding,
                maxY - 2 * numberHeight - 2 * titleAndNumberPadding - partPadding,
                titlePaint);
        canvas.drawText(longestStreaks[0],
                textLeftPadding + titleWidth - numberWidth,
                maxY - numberHeight - titleAndNumberPadding - partPadding,
                numberPaint);
        canvas.drawText(longestStreaks[1],
                textLeftPadding + titleWidth + numberAndRemarkPadding,
                maxY - numberHeight - titleAndNumberPadding - partPadding,
                remarkPaint);
        canvas.drawText(longestStreaks[2],
                textLeftPadding + titleWidth + numberAndRemarkPadding,
                maxY - remarkHeight - unitAndRemarkPadding
                        - numberHeight - titleAndNumberPadding - partPadding,
                unitPaint);

        // draw total text
        String[] totalStreaks = Util.getOneYearTotal(contributions);
        String[] busiestStreaks = Util.getBusiestDay(contributions);
        numberHeight = Util.getTextHeight(numberPaint, totalStreaks[0]);
        numberWidth = Util.getTextWidth(numberPaint, totalStreaks[0]);
        int titleHeight = Util.getTextHeight(titlePaint, Util.getString(R.string.one_year_total));
        titleWidth = Util.getTextWidth(titlePaint, Util.getString(R.string.one_year_total));
        remarkHeight = Util.getTextHeight(remarkPaint, totalStreaks[1]);
        int unitAndRemarkWidth = Util.getTextWidth(remarkPaint, totalStreaks[1]);
        unitAndRemarkWidth
                = Math.max(unitAndRemarkWidth, Util.getTextWidth(unitPaint, totalStreaks[2]));
        unitAndRemarkWidth
                = Math.max(unitAndRemarkWidth, Util.getTextWidth(remarkPaint, busiestStreaks[1]));
        unitAndRemarkWidth
                = Math.max(unitAndRemarkWidth, Util.getTextWidth(unitPaint, busiestStreaks[2]));
        canvas.drawText(Util.getString(R.string.one_year_total),
                bitmapWidth - paddingRight - unitAndRemarkWidth
                        - numberAndRemarkPadding - titleWidth - avatarWidth,
                paddingTop + titleHeight,
                titlePaint);
        canvas.drawText(totalStreaks[0],
                bitmapWidth - paddingRight - unitAndRemarkWidth
                        - numberAndRemarkPadding - numberWidth - avatarWidth,
                paddingTop + titleHeight + titleAndNumberPadding + numberHeight,
                numberPaint);
        canvas.drawText(totalStreaks[1],
                bitmapWidth - paddingRight - unitAndRemarkWidth - avatarWidth,
                paddingTop + titleHeight + titleAndNumberPadding + numberHeight,
                remarkPaint);
        canvas.drawText(totalStreaks[2],
                bitmapWidth - paddingRight - unitAndRemarkWidth - avatarWidth,
                paddingTop + titleHeight + titleAndNumberPadding + numberHeight
                        - remarkHeight - unitAndRemarkPadding,
                remarkPaint);

        // draw busiest day
        numberHeight = Util.getTextHeight(numberPaint, busiestStreaks[0]);
        numberWidth = Util.getTextWidth(numberPaint, busiestStreaks[0]);
        titleHeight = Util.getTextHeight(titlePaint, Util.getString(R.string.busiest_day));
        titleWidth = Util.getTextWidth(titlePaint, Util.getString(R.string.busiest_day));
        remarkHeight = Util.getTextHeight(remarkPaint, busiestStreaks[1]);
        canvas.drawText(Util.getString(R.string.busiest_day),
                bitmapWidth - paddingRight - unitAndRemarkWidth
                        - numberAndRemarkPadding - titleWidth - avatarWidth,
                paddingTop + titleHeight + titleAndNumberPadding + numberHeight + partPadding,
                titlePaint);
        canvas.drawText(busiestStreaks[0],
                bitmapWidth - paddingRight - unitAndRemarkWidth
                        - numberAndRemarkPadding - numberWidth - avatarWidth,
                paddingTop + titleHeight + 2 * titleAndNumberPadding
                        + 2 * numberHeight + partPadding,
                numberPaint);
        canvas.drawText(busiestStreaks[1],
                bitmapWidth - paddingRight - unitAndRemarkWidth - avatarWidth,
                paddingTop + titleHeight + 2 * titleAndNumberPadding
                        + 2 * numberHeight + partPadding,
                remarkPaint);
        canvas.drawText(busiestStreaks[2],
                bitmapWidth - paddingRight - unitAndRemarkWidth - avatarWidth,
                paddingTop + titleHeight + 2 * titleAndNumberPadding + 2 * numberHeight
                        - remarkHeight - unitAndRemarkPadding + partPadding,
                remarkPaint);

        return bitmap;
    }

    /**
     * Get a bitmap to guide user to set their user name.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @return The bitmap.
     */
    public static Bitmap getInputUserNameBitmap(Context context, int baseColor, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint paint = getTextPaint(40f, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        int bitmapWidth = getScreenWidth(context);

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(paint, getString(R.string.click_to_set)) / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(getString(R.string.click_to_set), xPos, yPos, paint);

        return bitmap;
    }

    /**
     * Get the bitmap of contributions sum.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param sum Sum.
     * @return The bitmap.
     */
    public static Bitmap getContributionsSumBitmap(Context context, int baseColor, int sum) {
        Bitmap bitmap;
        Canvas canvas;
        Paint paint = getTextPaint(30f, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        int bitmapWidth = dp2px(60);
        int bitmapHeight = dp2px(20);

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(paint, sum + "") / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(sum + "", xPos, yPos, paint);

        return bitmap;
    }

    /**
     * Decimal to hex.
     *
     * @param dec Decimal value.
     * @return The hex value.
     */
    public static String decToHex(int dec) {
        String hex = Integer.toHexString(dec);
        return hex.length() == 1 ? "0" + hex : hex;
    }

    /**
     * Create a bitmap where there are sum and english.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param sum Sum.
     * @param bitmapWidth Width.
     * @param bitmapHeight Height.
     * @return
     */
    public static Bitmap getContributionsSumWithLetterBitmap(
            Context context, int baseColor, int sum, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint mainPaint = getTextPaint(WIDGET_2_NUMBER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        Paint subPaint = getTextPaint(WIDGET_2_LETTER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(mainPaint, sum + "") / 2;
        int yPos = getTextHeight(mainPaint, sum + "");
        canvas.drawText(sum + "", xPos, yPos, mainPaint);

        xPos = (canvas.getWidth() / 2)
                - getTextWidth(subPaint, getString(R.string.one_year)) / 2;
        canvas.drawText(getString(R.string.one_year),
                xPos, canvas.getHeight() - WIDGET_2_LETTER_PADDING_BOTTOM, subPaint);

        return bitmap;
    }

    /**
     * Get target dimen in px.
     *
     * @param resId Resource id.
     * @return Px.
     */
    public static float getDimen(int resId) {
        return GithubWidgetApplication.getAppContext().getResources().getDimensionPixelSize(resId);
    }

    /**
     * Get today contributions from string.
     *
     * @param data Data in string.
     * @return Contributions of today.
     */
    public static int getContributionsToday(String data) {
        int index = data.lastIndexOf(DATA_STRING);
        if (index != -1) {
            int dataEndPos = data.indexOf("\"", index + DATA_STRING.length());
            String dataString = data.substring(index + DATA_STRING.length(), dataEndPos);
            return Integer.valueOf(dataString);
        }
        return 0;
    }

    /**
     * Create a bitmap where there are contributions of today and english.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param today Sum.
     * @param bitmapWidth Width.
     * @param bitmapHeight Height.
     * @return The bitmap.
     */
    public static Bitmap getContributionsTodayWithLetterBitmap(
            Context context, int baseColor, int today, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint mainPaint = getTextPaint(WIDGET_2_NUMBER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        Paint subPaint = getTextPaint(WIDGET_2_LETTER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(mainPaint, today + "") / 2;
        int yPos = getTextHeight(mainPaint, today + "");
        canvas.drawText(today + "", xPos, yPos, mainPaint);

        xPos = (canvas.getWidth() / 2)
                - getTextWidth(subPaint, getString(R.string.today)) / 2;
        canvas.drawText(getString(R.string.today),
                xPos, canvas.getHeight() - WIDGET_2_LETTER_PADDING_BOTTOM, subPaint);

        return bitmap;
    }

    /**
     * Create a bitmap where there are current streak and english.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param days Sum.
     * @param bitmapWidth Width.
     * @param bitmapHeight Height.
     * @return The bitmap.
     */
    public static Bitmap getCurrentStreakWithLetterBitmap(
            Context context, int baseColor, int days, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint mainPaint = getTextPaint(WIDGET_2_NUMBER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        Paint subPaint = getTextPaint(WIDGET_2_LETTER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(mainPaint, days + "") / 2;
        int yPos = getTextHeight(mainPaint, days + "");
        canvas.drawText(days + "", xPos, yPos, mainPaint);

        xPos = (canvas.getWidth() / 2)
                - getTextWidth(subPaint, getString(R.string.current)) / 2;
        canvas.drawText(getString(R.string.current),
                xPos, canvas.getHeight() - WIDGET_2_LETTER_PADDING_BOTTOM,
                subPaint);

        return bitmap;
    }

    /**
     * Get the motto bitmap.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param bitmapWidth Width of bitmap.
     * @param bitmapHeight Height of bitmap.
     * @return The bitmap.
     */
    public static Bitmap getMottoBitmap(
            Context context, int baseColor, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        float textHeight = 40f;
        float textPadding = 5f;
        float xPos = 0;

        Paint mainPaint = getTextPaint(textHeight, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        String content = SettingsManager.getMotto();
        if ("".equals(content) && SettingsManager.getUserName() != null)
            content = SettingsManager.getUserName();
        boolean isTwoLines = content.contains("\n");
        if (isTwoLines) {
            canvas.drawText(
                    content.substring(0, content.indexOf("\n")),
                    xPos, canvas.getHeight() / 2 - textPadding, mainPaint);
            canvas.drawText(
                    content.substring(content.indexOf("\n") + 1, content.length()),
                    xPos, canvas.getHeight() / 2 + textPadding + textHeight, mainPaint);
        } else {
            canvas.drawText(
                    content,
                    xPos, canvas.getHeight() / 2 + textHeight / 2, mainPaint);
        }

        return bitmap;
    }

    /**
     * At first, I wanna calculate the diff of followers increase a day.
     * But I leave it to Todo.
     *
     * @param result The result from https://api.github.com/users/username.
     * @return The string of followers.
     */
    public static String writeFollowers(String result) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);

            int lastFollowers = SettingsManager.getFollowers();
            long lastUpdateFollowersTime = SettingsManager.getLastUpdateFollowersTime();
            int nowFollowers = jsonObject.getInt("followers");

            if (lastUpdateFollowersTime == -1 || lastFollowers == -1) {
                // first time
                SettingsManager.setFollowers(nowFollowers);
                SettingsManager.setLastUpdateFollowersTime(
                        Calendar.getInstance().getTime().getTime());
                return nowFollowers + "";
            } else {
                int diff = 0;
                if (lastFollowers != -1) diff = nowFollowers - lastFollowers;
                if (diff > 0) {
                    return shortNumber(nowFollowers) + "+" + diff;
                } else if (diff < 0) {
                    return shortNumber(nowFollowers) + "-" + (-diff);
                } else {
                    return shortNumber(nowFollowers) + "";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            int lastFollowers = SettingsManager.getFollowers();
            if (lastFollowers != -1) return shortNumber(lastFollowers) + "";
            else return "";
        } finally {
            editor.commit();
        }
    }

    /**
     * 1000 -> 1k, 1100->1.1k
     *
     * @param number The input number.
     * @return The string.
     */
    public static String shortNumber(int number) {
        if (number >= 1000) {
            int hundred = number % 1000 / 100;
            if (hundred != 0) return String.format("%.1f", number / 1000) + "k";
            else return (number / 1000) + "k";
        }
        else return number + "";
    }

    /**
     * Create a bitmap where there are followers and english.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param followers String of followers.
     * @param bitmapWidth Width.
     * @param bitmapHeight Height.
     * @return The bitmap.
     */
    public static Bitmap getFollowersWithLetterBitmap(
            Context context, int baseColor, String followers, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint mainPaint = getTextPaint(WIDGET_2_NUMBER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        Paint subPaint = getTextPaint(WIDGET_2_LETTER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        // I decide not to draw this
        if (followers.contains("+")) followers = followers.substring(0, followers.indexOf("+"));
        if (followers.contains("-")) followers = followers.substring(0, followers.indexOf("-"));

        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(mainPaint, followers + "") / 2;
        int yPos = getTextHeight(mainPaint, followers + "");
        canvas.drawText(followers + "", xPos, yPos, mainPaint);

        xPos = (canvas.getWidth() / 2)
                - getTextWidth(subPaint, getString(R.string.followers)) / 2;
        canvas.drawText(getString(R.string.followers),
                xPos, canvas.getHeight() - WIDGET_2_LETTER_PADDING_BOTTOM,
                subPaint);

        return bitmap;
    }

    /**
     * Create a bitmap where there are stars and english.
     *
     * @param context Context.
     * @param baseColor Base color.
     * @param stars String of stars.
     * @param bitmapWidth Width.
     * @param bitmapHeight Height.
     * @return The bitmap.
     */
    public static Bitmap getStarsWithLetterBitmap(
            Context context, int baseColor, String stars, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap;
        Canvas canvas;
        Paint mainPaint = getTextPaint(WIDGET_2_NUMBER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));
        Paint subPaint = getTextPaint(WIDGET_2_LETTER_HEIGHT, calculateLevelColor(baseColor, 4),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf"));

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        int xPos = (canvas.getWidth() / 2)
                - getTextWidth(mainPaint, stars + "") / 2;
        int yPos = getTextHeight(mainPaint, stars + "");
        canvas.drawText(stars + "", xPos, yPos, mainPaint);

        xPos = (canvas.getWidth() / 2)
                - getTextWidth(subPaint, getString(R.string.stars)) / 2;
        canvas.drawText(getString(R.string.stars),
                xPos, canvas.getHeight() - WIDGET_2_LETTER_PADDING_BOTTOM,
                subPaint);

        return bitmap;
    }

    /**
     * Write the star update time and get the string of stars.
     *
     * @param result The string of result.
     * @return The string of stars.
     */
    public static String writeStars(String result) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(GithubWidgetApplication.getAppContext()).edit();
        JSONArray jsonArray = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = format.format(Calendar.getInstance().getTime());
        boolean thisIsANewDay = false;
        if (SettingsManager.getLastUpdateStarsDate() == null
                || !SettingsManager.getLastUpdateStarsDate().equals(nowDateString)) {
            thisIsANewDay = true;
        }
        if (thisIsANewDay) SettingsManager.setTodayStars(0);

        try {
            jsonArray = new JSONArray(result);
            String lastId = SettingsManager.getLastUpdateStarsId();
            int stars = SettingsManager.getTodayStars();
            boolean first = true;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject == null) continue;

                // if we found the record we already deal
                if (jsonObject.has("id") && jsonObject.getString("id").equals(lastId)) break;

                // record the first id we already deal
                if (first) {
                    if (jsonObject.has("id")) {
                        SettingsManager.setLastUpdateStarsId(jsonObject.getString("id"));
                        SettingsManager.setLastUpdateStarsDate(
                                jsonObject.getString("created_at").substring(0, 10));
                        first = false;
                    }
                }

                // whether this event is created today
                if (nowDateString.equals(jsonObject.getString("created_at").substring(0, 10))) {
                    // whether this is a watcher event
                    if (jsonObject.has("type") && jsonObject.getString("type").equals("WatchEvent")) {
                        JSONObject payload = jsonObject.getJSONObject("payload");
                        // whether this is a star event
                        if (payload.has("action") && payload.getString("action").equals("started")) {
                            JSONObject repo = jsonObject.getJSONObject("repo");
                            // whether the repository starred is belong to user
                            if (repo != null && repo.has("name")) {
                                String repoName = repo.getString("name");
                                if (repoName != null) repoName = repoName.toLowerCase();
                                String userName = SettingsManager.getUserName();
                                if (userName != null) userName = userName.toLowerCase();
                                if (repoName != null && userName != null) {
                                    if (repoName.indexOf(userName) == 0) {
                                        stars++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SettingsManager.setTodayStars(stars);

            return "+" + stars;
        } catch (JSONException e) {
            e.printStackTrace();
            return "+" + 0;
        } finally {
            editor.commit();
        }
    }

    /**
     * Update a motto on a widget.
     *
     * @param c The class of the widget.
     * @param remoteViewsId The layout resourece id of the widget.
     * @param context Context.
     * @param bitmapWidth Motto width.
     * @param bitmapHeight Motto height.
     */
    public static void updateMotto(Class c, int remoteViewsId, Context context,
                                   int bitmapWidth, int bitmapHeight) {
        RemoteViews remoteViews
                = new RemoteViews(context.getPackageName(), remoteViewsId);
        remoteViews.setImageViewBitmap(R.id.motto,
                Util.getMottoBitmap(context, SettingsManager.getBaseColor(),
                        bitmapWidth, bitmapHeight));
        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, c), remoteViews);
    }

    /**
     * Set a alarm to wake the service.
     *
     * @param context Context.
     * @param servicePendingIntent PendingIntent for service.
     */
    public static void addAlarmService(Context context, PendingIntent servicePendingIntent) {
        if (BuildConfig.DEBUG) Log.d("GithubWidget", "----------------------------------------" +
                "Add alarm service");

        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Calendar current = Calendar.getInstance();
        current.add(Calendar.MILLISECOND, SettingsManager.getUpdateTime());

        final Intent i = new Intent(context, GithubWidgetService.class);

        if (servicePendingIntent == null) {
            servicePendingIntent = PendingIntent.getService(context, 0, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        }

        m.setRepeating(AlarmManager.RTC, current.getTime().getTime(),
                SettingsManager.getUpdateTime(), servicePendingIntent);
    }

    /**
     * Check whether a pendingIntent is register in alarmManager.
     * There may be something wrong in this method.
     *
     * @param context The context.
     * @param servicePendingIntent The pendingIntent;
     * @return Result.
     */
    public static boolean checkAlarmService(Context context, PendingIntent servicePendingIntent) {
        final Intent i = new Intent(context, GithubWidgetService.class);
        return PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE) != null;
    }

    /**
     * Get the login cookie.
     *
     * @return The login cookie.
     */
    public static String getLoginCookie() {
        return CookieManager.getInstance().getCookie(Util.getString(R.string.login_url));
    }

    /**
     * Whether the user has already signed in.
     *
     * @return Result.
     */
    public static boolean getLoggedIn() {
        String cookie = getLoginCookie();
        try {
            return cookie != null && cookie.split(";")[0].equals("logged_in=yes");
        } catch (PatternSyntaxException p) {
            p.printStackTrace();
            return false;
        }
    }

    /**
     * Clean the cookies.
     */
    @SuppressWarnings("deprecation")
    public static void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager localCookieSyncManager
                    = CookieSyncManager.createInstance(GithubWidgetApplication.getAppContext());
            localCookieSyncManager.startSync();
            CookieManager localCookieManager = CookieManager.getInstance();
            localCookieManager.removeAllCookie();
            localCookieManager.removeSessionCookie();
            localCookieSyncManager.stopSync();
            localCookieSyncManager.sync();
        }

    }

    /**
     * Log.
     *
     * @param content The content.
     */
    public static void log(String content) {
        if (BuildConfig.DEBUG) Log.d(TAG, content);
    }

    /**
     * Get the bitmap of the trending sub view in list view in widget.
     *
     * @param index The index of the trending.
     * @return The bitmap.
     */
    public static Bitmap getTrendingBitmap(int index) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (dp2px(280)),
                (int) getDimen(R.dimen.list_view_content_item_height),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint ownerPaint = getTextPaint(25f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint repoPaint = getTextPaint(25f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        repoPaint.setFakeBoldText(true);
        Paint contentPaint = getTextPaint(18f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint cornerPaint = getTextPaint(18f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint cornerNumberPaint = getTextPaint(18f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
//        cornerNumberPaint.setFakeBoldText(true);
        Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(ContextCompat.getColor(
                GithubWidgetApplication.getAppContext(), R.color.divider_color));
        canvas.drawLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), dividerPaint);

        if (SettingsManager.getListViewContents() == null) {
            // illegal
            return bitmap;
        }

        float titlePaddingTop = 10f;
        float titlePaddingLeft = 10f;
        float titlePaddingRight = 10f;
        float contentPaddingTop = 10f;
        float contentPaddingLeft = 10f;
        float contentPaddingRight = 10f;
        float contentPaddingBottom = 10f;
        float cornerPaddingTop = 10f;
        float cornerPaddingRight = 10f;
        float titleCenterY = (int) ((canvas.getHeight() / 2)
                - ((repoPaint.descent() + repoPaint.ascent()) / 2));

        HashMap<String, String> content = SettingsManager.getListViewContents().get(index);
        String ownerString = content.get("title").substring(0, content.get("title").indexOf("/") + 1);
        String originRepoString = content.get("title").substring(content.get("title").indexOf("/") + 1,
                content.get("title").length());
        int i = 1;
        String showRepoString = originRepoString;

        if (titlePaddingLeft + titlePaddingRight
                + getTextWidth(ownerPaint, ownerString)
                + getTextWidth(repoPaint, showRepoString) > dp2px(280)) {
            showRepoString = originRepoString.substring(0, originRepoString.length() - i) + getString(R.string.dots);;
            while (titlePaddingLeft + titlePaddingRight
                    + getTextWidth(ownerPaint, ownerString)
                    + getTextWidth(repoPaint, showRepoString) > dp2px(280)) {
                i++;
                showRepoString = originRepoString.substring(0, originRepoString.length() - i) + getString(R.string.dots);
                if (i == originRepoString.length()) break;
            }
        }

        canvas.drawText(ownerString, titlePaddingLeft, titleCenterY, ownerPaint);

        canvas.drawText(showRepoString,
                getTextWidth(ownerPaint, ownerString) + titlePaddingLeft,
                titleCenterY,
                repoPaint);

        String fullContentString = content.get("content");
        String contentString = "";
        boolean enough = false;
        i = 0;

        while (contentPaddingLeft + contentPaddingRight
                + getTextWidth(contentPaint, contentString + getString(R.string.dots)) < dp2px(280)) {
            if (i >= fullContentString.length()) {
                enough = true;
                break;
            }
            contentString += fullContentString.charAt(i);
            i++;
        }
        if (!enough) contentString = contentString.substring(0, contentString.length() - 1)
                + getString(R.string.dots);

        canvas.drawText(contentString,
                contentPaddingLeft,
                canvas.getHeight() - contentPaddingBottom,
                contentPaint);

        String cornerString = content.get("corner");
        canvas.drawText(cornerString,
                canvas.getWidth() - cornerPaddingRight
                        - getTextWidth(cornerPaint, cornerString),
                cornerPaddingTop + getTextHeight(cornerPaint, cornerString),
                cornerPaint);

        return bitmap;
    }

    /**
     * Get the bitmap of received-event in list view in widget.
     *
     * @param index The index of the received-events.
     * @return The bitmap.
     */
    public static Bitmap getReceivedEventBitmap(int index) {

        Bitmap bitmap = Bitmap.createBitmap(
                (int) (dp2px(280)),
                (int) getDimen(R.dimen.list_view_content_item_height),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint titlePaint = getTextPaint(25f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint contentPaint = getTextPaint(18f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint cornerPaint = getTextPaint(18f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));
        Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(ContextCompat.getColor(
                GithubWidgetApplication.getAppContext(), R.color.divider_color));
        canvas.drawLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), dividerPaint);

        if (SettingsManager.getListViewContents() == null) {
            // illegal
            return bitmap;
        }

        float titlePaddingTop = 10f;
        float titlePaddingLeft = 10f;
        float titlePaddingRight = 10f;
        float contentPaddingTop = 10f;
        float contentPaddingLeft = 10f;
        float contentPaddingRight = 10f;
        float contentPaddingBottom = 10f;
        float cornerPaddingTop = 10f;
        float cornerPaddingRight = 10f;
        float titleCenterY = (int) ((canvas.getHeight() / 2)
                - ((titlePaint.descent() + titlePaint.ascent()) / 2));

        HashMap<String, String> content = SettingsManager.getListViewContents().get(index);

        String originTitleString = content.get("title");
        int i = 1;
        String showTitleString = originTitleString;

        if (titlePaddingLeft + titlePaddingRight
                + getTextWidth(titlePaint, showTitleString) > dp2px(280)) {
            showTitleString = originTitleString.substring(0, originTitleString.length() - i) + getString(R.string.dots);;
            while (titlePaddingLeft + titlePaddingRight
                    + getTextWidth(titlePaint, showTitleString) > dp2px(280)) {
                i++;
                showTitleString = originTitleString.substring(0, originTitleString.length() - i) + getString(R.string.dots);
                if (i == originTitleString.length()) break;
            }
        }

        canvas.drawText(showTitleString, titlePaddingLeft, titleCenterY, titlePaint);

        String originContentString = content.get("content");
        i = 1;
        String showContentString = originContentString;

        if (titlePaddingLeft + titlePaddingRight
                + getTextWidth(titlePaint, showContentString) > dp2px(280)) {
            showContentString = originContentString.substring(0, originContentString.length() - i) + getString(R.string.dots);;
            while (titlePaddingLeft + titlePaddingRight
                    + getTextWidth(titlePaint, showContentString) > dp2px(280)) {
                i++;
                showContentString = originContentString.substring(0, originContentString.length() - i) + getString(R.string.dots);
                if (i == originContentString.length()) break;
            }
        }

        canvas.drawText(showContentString,
                contentPaddingLeft,
                canvas.getHeight() - contentPaddingBottom,
                contentPaint);

        String cornerString = content.get("corner");
        canvas.drawText(cornerString,
                canvas.getWidth() - cornerPaddingRight
                        - getTextWidth(cornerPaint, cornerString),
                cornerPaddingTop + getTextHeight(cornerPaint, cornerString),
                cornerPaint);

        return bitmap;
    }

    /**
     * Get the loading view in the list view of widget.
     *
     * @return The bitmap.
     */
    public static Bitmap getLoadingBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (dp2px(280)),
                (int) getDimen(R.dimen.list_view_content_item_height),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint loadingPaint = getTextPaint(25f, calculateLevelColor(SettingsManager.getBaseColor(), 4),
                Typeface.createFromAsset(GithubWidgetApplication.getAppContext().getAssets(),
                        "fonts/Lato-Light.ttf"));

        if (SettingsManager.getListViewContents() == null) {
            // illegal
            return bitmap;
        }

        float titleCenterY = (int) ((canvas.getHeight() / 2)
                - ((loadingPaint.descent() + loadingPaint.ascent()) / 2));

        canvas.drawText(getString(R.string.loading),
                canvas.getWidth() / 2 - getTextWidth(loadingPaint, getString(R.string.loading)) / 2,
                titleCenterY,
                loadingPaint);

        return bitmap;
    }

    /**
     * Transform the UTC time to the local time zone.
     *
     * @param string Input time.
     * @return Output time.
     */
    public static String getTime(String string) {
        string = string.replace("T", " ");
        string = string.replace("Z", "");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(string);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return getShortDateName(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                    calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                    (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
            return string;
        }
    }

    public static final String[] keyWordsStart = new String[]{"<a href", "</a>", "<img"};
    public static final String[] keyWordsEnd = new String[]{">", ">", ">"};
    public static String deleteUselessString(String input) {
        StringBuilder builder = new StringBuilder(input);
        for (int i = 0; i < keyWordsStart.length; i++) {
            while (true) {
                int index = builder.indexOf(keyWordsStart[i]);
                if (index != -1) {
                    int end = builder.indexOf(keyWordsEnd[i], index);
                    builder.delete(index, end + 1);
                } else {
                    break;
                }
            }
        }
        return builder.toString();
    }






















    public final static String SHOW_DATA = "\n" +
            "<svg width=\"721\" height=\"110\" class=\"js-calendar-graph-svg\">\n" +
            "  <g transform=\"translate(20, 20)\">\n" +
            "      <g transform=\"translate(0, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#1e6823\" data-count=\"46\" data-date=\"2015-04-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-04-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-04-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-04-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-04-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-05-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-05-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(13, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-05-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-05-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-05-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-05-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-05-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-05-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(26, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-05-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-05-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(39, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-05-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-05-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-05-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-05-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-05-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-05-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(52, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-05-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"21\" data-date=\"2015-05-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-05-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-05-30\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(65, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-06-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-06-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-06-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-06-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-06-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-06-06\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(78, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-06-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-06-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"20\" data-date=\"2015-06-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-06-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-06-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-13\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(91, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-06-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2015-06-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2015-06-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-06-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-06-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"17\" data-date=\"2015-06-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-06-20\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(104, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-06-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-06-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-06-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-06-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-06-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#1e6823\" data-count=\"34\" data-date=\"2015-06-27\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(117, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"20\" data-date=\"2015-06-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-06-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#1e6823\" data-count=\"33\" data-date=\"2015-06-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-07-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-07-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-07-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-07-04\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(130, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"18\" data-date=\"2015-07-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"26\" data-date=\"2015-07-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-07-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-07-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-07-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-11\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(143, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-07-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-07-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"24\" data-date=\"2015-07-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-07-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-07-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-07-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-07-18\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(156, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-07-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-07-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-07-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-07-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"20\" data-date=\"2015-07-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-07-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-07-25\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(169, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-07-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-07-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-07-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-07-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-07-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-08-01\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(182, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#1e6823\" data-count=\"45\" data-date=\"2015-08-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"18\" data-date=\"2015-08-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-08-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#1e6823\" data-count=\"33\" data-date=\"2015-08-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-08-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-08-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-08-08\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(195, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#1e6823\" data-count=\"43\" data-date=\"2015-08-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2015-08-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-08-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-08-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-08-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"20\" data-date=\"2015-08-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-08-15\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(208, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-08-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-08-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-08-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-08-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-08-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-08-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"27\" data-date=\"2015-08-22\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(221, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#44a340\" data-count=\"22\" data-date=\"2015-08-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-08-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-08-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-08-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-08-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-08-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-08-29\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(234, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#44a340\" data-count=\"22\" data-date=\"2015-08-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"21\" data-date=\"2015-08-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#44a340\" data-count=\"22\" data-date=\"2015-09-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-09-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-09-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-09-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(247, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-09-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"28\" data-date=\"2015-09-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2015-09-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-09-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-09-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(260, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-09-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-09-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-09-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2015-09-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"22\" data-date=\"2015-09-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(273, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#1e6823\" data-count=\"33\" data-date=\"2015-09-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-09-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-09-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-09-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"17\" data-date=\"2015-09-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"28\" data-date=\"2015-09-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(286, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#44a340\" data-count=\"30\" data-date=\"2015-09-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2015-09-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-09-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"21\" data-date=\"2015-09-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2015-10-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-10-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-10-03\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(299, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-10-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2015-10-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#1e6823\" data-count=\"40\" data-date=\"2015-10-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-10-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#1e6823\" data-count=\"35\" data-date=\"2015-10-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-10-10\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(312, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-10-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-10-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-10-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-10-17\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(325, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-10-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-10-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-10-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#1e6823\" data-count=\"42\" data-date=\"2015-10-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-10-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-24\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(338, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-10-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-31\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(351, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2015-11-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-07\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(364, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-11-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-11-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-11-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-11-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2015-11-14\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(377, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-11-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"20\" data-date=\"2015-11-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-11-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-11-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-21\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(390, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2015-11-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2015-11-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-11-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-11-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-28\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(403, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#44a340\" data-count=\"27\" data-date=\"2015-12-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-12-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(416, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-12-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#44a340\" data-count=\"26\" data-date=\"2015-12-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-12-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-12-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(429, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2015-12-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-12-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2015-12-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-12-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2015-12-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-12-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(442, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"27\" data-date=\"2015-12-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"28\" data-date=\"2015-12-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-12-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2015-12-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-12-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2015-12-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(455, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#44a340\" data-count=\"25\" data-date=\"2015-12-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"23\" data-date=\"2015-12-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"28\" data-date=\"2015-12-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"17\" data-date=\"2015-12-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-01-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"32\" data-date=\"2016-01-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(468, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2016-01-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#44a340\" data-count=\"26\" data-date=\"2016-01-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"25\" data-date=\"2016-01-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-01-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-01-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(481, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-01-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2016-01-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-01-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(494, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-01-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-01-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-01-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2016-01-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(507, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2016-01-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"24\" data-date=\"2016-01-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"21\" data-date=\"2016-01-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"16\" data-date=\"2016-01-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2016-01-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-01-30\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(520, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-02-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-02-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"15\" data-date=\"2016-02-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#44a340\" data-count=\"32\" data-date=\"2016-02-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-02-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-06\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(533, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-02-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2016-02-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"27\" data-date=\"2016-02-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-02-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#1e6823\" data-count=\"33\" data-date=\"2016-02-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#1e6823\" data-count=\"46\" data-date=\"2016-02-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-02-13\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(546, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-02-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-02-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"25\" data-date=\"2016-02-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-02-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2016-02-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"27\" data-date=\"2016-02-20\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(559, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-02-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-02-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-02-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2016-02-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-02-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-27\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(572, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2016-02-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"17\" data-date=\"2016-02-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"29\" data-date=\"2016-03-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-03-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2016-03-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(585, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-03-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2016-03-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-03-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2016-03-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-03-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-03-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-03-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(598, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2016-03-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-03-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2016-03-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-03-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-03-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(611, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-03-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2016-03-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-03-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-03-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(624, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-03-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-03-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"18\" data-date=\"2016-03-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2016-03-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-04-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(637, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2016-04-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2016-04-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-04-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-04-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-04-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(650, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-04-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"10\" data-date=\"2016-04-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"19\" data-date=\"2016-04-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(663, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-04-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-04-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"11\" data-date=\"2016-04-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2016-04-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#44a340\" data-count=\"29\" data-date=\"2016-04-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(676, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-04-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2016-04-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-04-26\"/>\n" +
            "      </g>\n" +
            "      <text x=\"13\" y=\"-5\" class=\"month\">May</text>\n" +
            "      <text x=\"78\" y=\"-5\" class=\"month\">Jun</text>\n" +
            "      <text x=\"130\" y=\"-5\" class=\"month\">Jul</text>\n" +
            "      <text x=\"182\" y=\"-5\" class=\"month\">Aug</text>\n" +
            "      <text x=\"247\" y=\"-5\" class=\"month\">Sep</text>\n" +
            "      <text x=\"299\" y=\"-5\" class=\"month\">Oct</text>\n" +
            "      <text x=\"351\" y=\"-5\" class=\"month\">Nov</text>\n" +
            "      <text x=\"416\" y=\"-5\" class=\"month\">Dec</text>\n" +
            "      <text x=\"468\" y=\"-5\" class=\"month\">Jan</text>\n" +
            "      <text x=\"533\" y=\"-5\" class=\"month\">Feb</text>\n" +
            "      <text x=\"585\" y=\"-5\" class=\"month\">Mar</text>\n" +
            "      <text x=\"637\" y=\"-5\" class=\"month\">Apr</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"9\" style=\"display: none;\">S</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"22\">M</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"35\" style=\"display: none;\">T</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"48\">W</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"61\" style=\"display: none;\">T</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"74\">F</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"87\" style=\"display: none;\">S</text>\n" +
            "  </g>\n" +
            "</svg>\n";

    private static Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }
}
