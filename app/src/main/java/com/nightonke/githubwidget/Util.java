package com.nightonke.githubwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
     * Get the color of a certain level.
     *
     * @param baseColor The base color.
     * @param level The level, from 0 to 4.
     * @return The color of the input level.
     */
    public static int getLevelColor(int baseColor, int level) {
        switch (level) {
            case 0: return Color.parseColor("#eeeeee");
            case 1: return Color.parseColor("#d6e685");
            case 2: return Color.parseColor("#8cc665");
            case 3: return Color.parseColor("#44a340");
            case 4: return Color.parseColor("#1e6823");
            default: return Color.parseColor("#eeeeee");
        }
    }

    /**
     * Get the month name for a certain date.
     *
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return The name of the month.
     */
    public static String getMonthName(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.SECOND, 0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.US);
        return month_date.format(calendar.getTime());
    }

    /**
     * Get the first letter of a weekday.
     *
     * @param weekDay Integer from 0 to 6 for weekday.
     * @return The first letter for the weekday.
     */
    public static String getWeekDayFirstLetter(int weekDay) {
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
            editor.putInt("USER_ID", jsonObject.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    /**
     * Get the contributions bitmap.
     *
     * @param data The data from xml.
     * @param startWeekDay Start week day.
     * @param baseColor Base color for block.
     * @param textColor Text color.
     * @param bitmapWidth Bitmap width.
     * @param bitmapHeight Bitmap height.
     * @return Bitmap.
     */
    public static Bitmap get2DBitmap(
            Context context,
            String data,
            int startWeekDay,
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
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 1) % 7),
                0, baseline, weekDayTextPaint);
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 3) % 7),
                0, baseline + 2 * (blockWidth + spaceWidth), weekDayTextPaint);
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 5) % 7),
                0, baseline + 4 * (blockWidth + spaceWidth), weekDayTextPaint);

        // draw the blocks
        int currentWeekDay = Util.getWeekDayFromDate(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day);
        float x = weekTextHeight + topMargin;
        float y = (currentWeekDay - startWeekDay + 7) % 7
                * (blockWidth + spaceWidth) + topMargin + monthTextHeight;
        int lastMonth = contributions.get(0).month;
        for (Day day : contributions) {
            blockPaint.setColor(Util.getLevelColor(baseColor, day.level));
            canvas.drawRect(x, y, x + blockWidth, y + blockWidth, blockPaint);

            currentWeekDay = (currentWeekDay + 1) % 7;
            if (currentWeekDay == startWeekDay) {
                // another column
                x += blockWidth + spaceWidth;
                y = topMargin + monthTextHeight;
                if (day.month != lastMonth) {
                    // judge whether we should draw the text of month
                    canvas.drawText(
                            Util.getMonthName(day.year, day.month, day.day),
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
































    private static Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }
}
