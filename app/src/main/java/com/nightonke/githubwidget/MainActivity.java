package com.nightonke.githubwidget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.image);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        imageView.setImageBitmap(get2DBitmap(getTestData(), 0, 0, Color.parseColor("#000000")));
    }

    private Bitmap get2DBitmap(
            ArrayList<Day> contributions,
            int startWeekDay,
            int baseColor,
            int textColor) {
        Bitmap bitmap;
        Canvas canvas;
        Paint blockPaint;
        Paint textPaint;

        int bitmapWidth = Util.getScreenWidth(this);
        int horizontalBlockNumber = Util.getContributionsColumnNumber(TEST_DATA);
        int verticalBlockNumber = 7;
        float ADJUST_VALUE = 0.8f;
        float blockWidth = bitmapWidth / (ADJUST_VALUE + horizontalBlockNumber) * (1.0F - 0.1F);
        float spaceWidth = bitmapWidth / (ADJUST_VALUE + horizontalBlockNumber) - blockWidth;
        float textHeight = blockWidth * 1F;
        float topMargin = 7f;
        int bitmapHeight = (int)(textHeight + verticalBlockNumber * (blockWidth + spaceWidth) + 7);

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textHeight);
        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));

        // draw the text for weekdays
        float textStartHeight = textHeight + topMargin + blockWidth + spaceWidth;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        float baseline = (
                textStartHeight + blockWidth +
                textStartHeight -
                fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 1) % 7),
                0, baseline, textPaint);
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 3) % 7),
                0, baseline + 2 * (blockWidth + spaceWidth), textPaint);
        canvas.drawText(Util.getWeekDayFirstLetter((startWeekDay + 5) % 7),
                0, baseline + 4 * (blockWidth + spaceWidth), textPaint);

        // draw the blocks
        int currentWeekDay = Util.getWeekDayFromDate(
                contributions.get(0).year,
                contributions.get(0).month,
                contributions.get(0).day);
        float x = textHeight + topMargin;
        float y = (currentWeekDay - startWeekDay + 7) % 7
                * (blockWidth + spaceWidth) + topMargin + textHeight;
        int lastMonth = contributions.get(0).month;
        for (Day day : contributions) {
            blockPaint.setColor(Util.getLevelColor(baseColor, day.level));
            canvas.drawRect(x, y, x + blockWidth, y + blockWidth, blockPaint);

            currentWeekDay = (currentWeekDay + 1) % 7;
            if (currentWeekDay == startWeekDay) {
                // another column
                x += blockWidth + spaceWidth;
                y = topMargin + textHeight;
                if (day.month != lastMonth) {
                    // judge whether we should draw the text of month
                    canvas.drawText(
                            Util.getMonthName(day.year, day.month, day.day),
                            x, textHeight, textPaint);
                    lastMonth = day.month;
                }
            } else {
                y += blockWidth + spaceWidth;
            }
        }

        return bitmap;
    }

    private ArrayList<Day> getTestData() {
        return Util.getContributionsFromString(TEST_DATA);
    }

    private final static String TEST_DATA = "\n" +
            "<svg width=\"721\" height=\"110\" class=\"js-calendar-graph-svg\">\n" +
            "  <g transform=\"translate(20, 20)\">\n" +
            "      <g transform=\"translate(0, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-25\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(13, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-04-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(26, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(39, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(52, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(65, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-30\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(78, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-05-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-06\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(91, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-13\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(104, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-20\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(117, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-27\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(130, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-06-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-04\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(143, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-11\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(156, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-18\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(169, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-25\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(182, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-07-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-01\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(195, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-08\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(208, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-15\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(221, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-22\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(234, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-29\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(247, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-08-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(260, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-09-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(273, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-09-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-09-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-09-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-09-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(286, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-09-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2015-09-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-09-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(299, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-09-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-09-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-09-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-09-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-10-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-03\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(312, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-10-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-10\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(325, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-10-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-10-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-10-17\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(338, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-24\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(351, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-10-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-10-31\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(364, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-11-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-07\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(377, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-14\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(390, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-11-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-21\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(403, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"27\" data-date=\"2015-11-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-11-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-11-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-11-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2015-11-28\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(416, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-11-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-12-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(429, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2015-12-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"13\" data-date=\"2015-12-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2015-12-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-12-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-12-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(442, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-12-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-12-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2015-12-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(455, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2015-12-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(468, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2015-12-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2015-12-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2015-12-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(481, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-01-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(494, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(507, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-01-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-01-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(520, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-01-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-01-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-30\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(533, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-01-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-02-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-02-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-02-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-02-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-02-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-06\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(546, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-13\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(559, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-02-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#eeeeee\" data-count=\"0\" data-date=\"2016-02-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"19\" data-date=\"2016-02-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-02-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-02-20\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(572, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-02-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-02-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-02-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-02-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-02-26\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-02-27\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(585, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-02-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-02-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-03-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-03-02\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-03-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-03-05\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(598, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-03-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"9\" data-date=\"2016-03-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#44a340\" data-count=\"22\" data-date=\"2016-03-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#44a340\" data-count=\"26\" data-date=\"2016-03-09\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-03-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-12\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(611, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-03-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"8\" data-date=\"2016-03-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-03-16\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-03-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#8cc665\" data-count=\"17\" data-date=\"2016-03-19\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(624, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-03-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-03-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#8cc665\" data-count=\"10\" data-date=\"2016-03-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#8cc665\" data-count=\"10\" data-date=\"2016-03-23\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#1e6823\" data-count=\"37\" data-date=\"2016-03-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#1e6823\" data-count=\"28\" data-date=\"2016-03-25\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-03-26\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(637, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"5\" data-date=\"2016-03-27\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-28\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-03-29\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-03-30\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-03-31\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#1e6823\" data-count=\"28\" data-date=\"2016-04-01\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-04-02\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(650, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-03\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-04\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-05\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"2\" data-date=\"2016-04-06\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#8cc665\" data-count=\"14\" data-date=\"2016-04-07\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-08\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-09\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(663, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-10\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-04-11\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"7\" data-date=\"2016-04-12\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-13\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-14\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-15\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#8cc665\" data-count=\"12\" data-date=\"2016-04-16\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(676, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-17\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-18\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"26\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-04-19\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"39\" fill=\"#d6e685\" data-count=\"6\" data-date=\"2016-04-20\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"52\" fill=\"#d6e685\" data-count=\"3\" data-date=\"2016-04-21\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"65\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-22\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"78\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-23\"/>\n" +
            "      </g>\n" +
            "      <g transform=\"translate(689, 0)\">\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"0\" fill=\"#d6e685\" data-count=\"4\" data-date=\"2016-04-24\"/>\n" +
            "          <rect class=\"day\" width=\"11\" height=\"11\" y=\"13\" fill=\"#d6e685\" data-count=\"1\" data-date=\"2016-04-25\"/>\n" +
            "      </g>\n" +
            "      <text x=\"26\" y=\"-5\" class=\"month\">May</text>\n" +
            "      <text x=\"91\" y=\"-5\" class=\"month\">Jun</text>\n" +
            "      <text x=\"143\" y=\"-5\" class=\"month\">Jul</text>\n" +
            "      <text x=\"195\" y=\"-5\" class=\"month\">Aug</text>\n" +
            "      <text x=\"260\" y=\"-5\" class=\"month\">Sep</text>\n" +
            "      <text x=\"312\" y=\"-5\" class=\"month\">Oct</text>\n" +
            "      <text x=\"364\" y=\"-5\" class=\"month\">Nov</text>\n" +
            "      <text x=\"429\" y=\"-5\" class=\"month\">Dec</text>\n" +
            "      <text x=\"481\" y=\"-5\" class=\"month\">Jan</text>\n" +
            "      <text x=\"546\" y=\"-5\" class=\"month\">Feb</text>\n" +
            "      <text x=\"598\" y=\"-5\" class=\"month\">Mar</text>\n" +
            "      <text x=\"650\" y=\"-5\" class=\"month\">Apr</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"9\" style=\"display: none;\">S</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"22\">M</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"35\" style=\"display: none;\">T</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"48\">W</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"61\" style=\"display: none;\">T</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"74\">F</text>\n" +
            "    <text text-anchor=\"middle\" class=\"wday\" dx=\"-10\" dy=\"87\" style=\"display: none;\">S</text>\n" +
            "  </g>\n" +
            "</svg>\n";
}
