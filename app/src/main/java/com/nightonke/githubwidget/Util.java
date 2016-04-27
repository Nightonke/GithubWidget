package com.nightonke.githubwidget;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.github.johnpersano.supertoasts.SuperToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Weiping on 2016/4/25.
 */

public class Util {

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
                case "#eeeeee": level = 0; break;
                case "#d6e685": level = 1; break;
                case "#8cc665": level = 2; break;
                case "#44a340": level = 3; break;
                case "#1e6823": level = 4; break;
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
     * Todo
     *
     * @param context
     * @param data
     * @param startWeekDay
     * @param baseColor
     * @param textColor
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap get2DBitmap(
            Context context,
            String data,
            Weekday startWeekDay,
            int baseColor,
            int textColor,
            int bitmapWidth,
            int bitmapHeight) {
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
        float topMargin = 7f;
        bitmapHeight = (int)(monthTextHeight + verticalBlockNumber * (blockWidth + spaceWidth) + 7);

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
        float textStartHeight = monthTextHeight + topMargin + blockWidth + spaceWidth;
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
                * (blockWidth + spaceWidth) + topMargin + monthTextHeight;
        int lastMonth = contributions.get(0).month;
        for (Day day : contributions) {
            blockPaint.setColor(Util.calculateLevelColor(baseColor, day.level));
            canvas.drawRect(x, y, x + blockWidth, y + blockWidth, blockPaint);

            currentWeekDay = (currentWeekDay + 1) % 7;
            if (currentWeekDay == startWeekDay.v) {
                // another column
                x += blockWidth + spaceWidth;
                y = topMargin + monthTextHeight;
                if (day.month != lastMonth) {
                    // judge whether we should draw the text of month
                    canvas.drawText(
                            Util.getShortMonthName(day.year, day.month, day.day),
                            x, monthTextHeight, monthTextPaint);
                    lastMonth = day.month;
                }
            } else {
                y += blockWidth + spaceWidth;
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
                    longestEnd = i - 1;
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
                currentStreakStart = i;
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

        String remarkString = Util.getFullMonthDayName(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day)
                + getString(R.string.remark_to)
                + Util.getFullMonthDayName(
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

    public static Bitmap get3DBitmap(
            Context context,
            String data,
            Weekday startWeekday,
            int baseColor,
            int textColor,
            boolean drawMonthText,
            boolean drawWeekdayText) {
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
        int bitmapHeight = bitmapWidth * 3 / 4;
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
        float avatarWidth = bitmapWidth / 4.5f;

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




























    private static Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }
}
