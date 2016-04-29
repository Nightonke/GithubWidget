package com.nightonke.githubwidget;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
        implements
        View.OnClickListener {

    private LinearLayout userNameLayout;
    private EditText userNameEditText;

    private RelativeLayout mottoLayout;
    private LimitedEditText mottoEditText;

    private ImageView imageView3D;
    private ImageView imageView2D;
    private ImageView colorImageView;
    private TextView colorText;
    private TextView seekBarMinA;
    private TextView seekBarMaxA;
    private MySeekBar seekBarA;
    private TextView seekBarMinR;
    private TextView seekBarMaxR;
    private MySeekBar seekBarR;
    private TextView seekBarMinG;
    private TextView seekBarMaxG;
    private MySeekBar seekBarG;
    private TextView seekBarMinB;
    private TextView seekBarMaxB;
    private MySeekBar seekBarB;
    private Button resetBaseColorButton;
    
    private LinearLayout showToastLayout;
    private CheckBox showToastCheckBox;
    
    private LinearLayout showMonthDashIn3DLayout;
    private CheckBox showMonthDashIn3DCheckBox;
    
    private LinearLayout showWeekdayDashIn3DLayout;
    private CheckBox showWeekdayDashIn3DCheckBox;

    private Button loginButton;

    private LinearLayout startFromLayout;
    private RadioButton startFromSunday;
    private RadioButton startFromMonday;

    private SeekBar updateTimeSeekBar;
    private TextView updateTimeText;

    private SeekBar receivedEventPerPageSeekBar;
    private TextView receivedEventPerPageTextView;

    private String oldUserName;
    private String oldMotto;
    private int oldBaseColor;
    private boolean oldUserLogin;
    private boolean oldShowToast;
    private boolean oldShowMonthDashIn3D;
    private boolean oldShowWeekdayDashIn3D;
    private Weekday oldWeekday;
    private int oldUpdateTime;
    private int oldReceivedEventPerPage;

    private int newBaseColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userNameLayout = findView(R.id.user_name_layout);
        userNameEditText = findView(R.id.user_name_title);
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsManager.setUserName(userNameEditText.getText().toString());
                SettingsManager.setUserId(-1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userNameLayout.setOnClickListener(this);

        mottoLayout = findView(R.id.motto_layout);
        mottoEditText = findView(R.id.motto_title);
        mottoEditText.setMaxLines(2);
        mottoEditText.setOnTextChangedListener(new LimitedEditText.OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsManager.setMotto(mottoEditText.getText().toString());
            }
        });
        mottoLayout.setOnClickListener(this);
        
        imageView3D = findView(R.id.three_d);
        imageView2D = findView(R.id.two_d);
        colorImageView = findView(R.id.color);
        colorText = findView(R.id.color_edit_text);
        seekBarMinA = findView(R.id.seek_bar_min_a);
        seekBarMaxA = findView(R.id.seek_bar_max_a);
        seekBarA = findView(R.id.seek_bar_a);
        seekBarA.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarMinR = findView(R.id.seek_bar_min_r);
        seekBarMaxR = findView(R.id.seek_bar_max_r);
        seekBarR = findView(R.id.seek_bar_r);
        seekBarR.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarMinG = findView(R.id.seek_bar_min_g);
        seekBarMaxG = findView(R.id.seek_bar_max_g);
        seekBarG = findView(R.id.seek_bar_g);
        seekBarG.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarMinB = findView(R.id.seek_bar_min_b);
        seekBarMaxB = findView(R.id.seek_bar_max_b);
        seekBarB = findView(R.id.seek_bar_b);
        seekBarB.setOnSeekBarChangeListener(onSeekBarChangeListener);
        resetBaseColorButton = findView(R.id.reset_base_color);
        resetBaseColorButton.setOnClickListener(this);

        loginButton = findView(R.id.login);

        showToastLayout = findView(R.id.show_toast_layout);
        showToastCheckBox = findView(R.id.show_toast_checkbox);
        showToastLayout.setOnClickListener(this);
        showToastCheckBox.setOnClickListener(this);
        showToastCheckBox.setChecked(SettingsManager.getShowToast());
        
        showMonthDashIn3DLayout = findView(R.id.show_month_dash_in_3d_layout);
        showMonthDashIn3DCheckBox = findView(R.id.show_month_dash_in_3d_checkbox);
        showMonthDashIn3DLayout.setOnClickListener(this);
        showMonthDashIn3DCheckBox.setOnClickListener(this);
        showMonthDashIn3DCheckBox.setChecked(SettingsManager.getShowMonthDashIn3D());

        showWeekdayDashIn3DLayout = findView(R.id.show_weekday_dash_in_3d_layout);
        showWeekdayDashIn3DCheckBox = findView(R.id.show_weekday_dash_in_3d_checkbox);
        showWeekdayDashIn3DLayout.setOnClickListener(this);
        showWeekdayDashIn3DCheckBox.setOnClickListener(this);
        showWeekdayDashIn3DCheckBox.setChecked(SettingsManager.getShowWeekdayDashIn3D());

        startFromLayout = findView(R.id.start_from_layout);
        startFromSunday = findView(R.id.start_from_sunday);
        startFromMonday = findView(R.id.start_from_monday);
        startFromLayout.setOnClickListener(this);
        startFromSunday.setOnClickListener(this);
        startFromMonday.setOnClickListener(this);
        startFromSunday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.SUN));
        startFromMonday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.MON));

        updateTimeSeekBar = findView(R.id.seek_bar_update_time);
        updateTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress == updateTimeSeekBar.getMax()) {
                        SettingsManager.setUpdateTime(Integer.MAX_VALUE);
                    } else {
                        SettingsManager.setUpdateTime(
                                Util.HALF_AN_HOUR * (updateTimeSeekBar.getProgress() + 1));
                    }
                    setUpdateText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (SettingsManager.getUpdateTime() == Integer.MAX_VALUE) {
            updateTimeSeekBar.setProgress(updateTimeSeekBar.getMax());
        } else {
            updateTimeSeekBar.setProgress(SettingsManager.getUpdateTime() / Util.HALF_AN_HOUR - 1);
        }
        updateTimeText = findView(R.id.update_time_text);
        setUpdateText();

        receivedEventPerPageSeekBar = findView(R.id.seek_bar_received_event_per_page);
        receivedEventPerPageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    SettingsManager.setReceivedEventPerPage(progress + 1);
                    setReceivedEventPerPageText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        receivedEventPerPageSeekBar.setProgress(SettingsManager.getReceivedEventPerPage() - 1);
        receivedEventPerPageTextView = findView(R.id.received_event_per_page_text);
        setReceivedEventPerPageText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setLoginButtonText() {
        if (!Util.getLoggedIn()) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent().setClassName(SettingsActivity.this, "com.nightonke.githubwidget.LoginActivity");
                    startActivity(intent);
                }
            });
            loginButton.setText(R.string.login);
        } else {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.clearCookies();
                    loginButton.setText(R.string.login);
                }
            });
            loginButton.setText(R.string.logout);
        }
    }

    private void setUpdateText() {
        if (updateTimeSeekBar.getProgress() == updateTimeSeekBar.getMax()) {
            updateTimeText.setText(R.string.dont_update);
        } else {
            updateTimeText.setText(
                    Util.getString(R.string.update_every)
                            + String.format("%.1f",
                            (updateTimeSeekBar.getProgress() / 2f + 0.5f)) + "h"
                            + Util.getString(R.string.dot));
        }
    }

    private void setReceivedEventPerPageText() {
        if (receivedEventPerPageSeekBar.getProgress() == 0) {
            receivedEventPerPageTextView.setText("1" + Util.getString(R.string.event_per_page));
        } else {
            receivedEventPerPageTextView.setText((receivedEventPerPageSeekBar.getProgress() + 1)
                    + Util.getString(R.string.events_per_page));
        }
    }

    private void setColor() {
        newBaseColor = Color.argb(
                seekBarA.getProgress(), seekBarR.getProgress(),
                seekBarG.getProgress(), seekBarB.getProgress());
        imageView3D.setImageBitmap(
                Util.get3DBitmap(this, Util.SHOW_DATA, Weekday.SUN,
                        newBaseColor, Color.parseColor("#000000"), false, false, true));
        imageView2D.setImageBitmap(
                Util.get2DBitmap(this, Util.SHOW_DATA, Weekday.SUN,
                        newBaseColor, Color.parseColor("#000000"),
                        Util.getScreenWidth(this), 0, false));
        ((GradientDrawable)colorImageView.getBackground()).setColor(newBaseColor);
        colorText.setText(""
                + Util.decToHex(seekBarA.getProgress())
                + Util.decToHex(seekBarR.getProgress())
                + Util.decToHex(seekBarG.getProgress())
                + Util.decToHex(seekBarB.getProgress()));
        setSeekBarColor(seekBarA, newBaseColor);
        setSeekBarColor(seekBarR, newBaseColor);
        setSeekBarColor(seekBarG, newBaseColor);
        setSeekBarColor(seekBarB, newBaseColor);
    }

    public void setSeekBarColor(MySeekBar seekBar, int newColor){
        seekBar.getProgressDrawable().setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        seekBar.getSeekBarThumb().setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (SettingsManager.getUserName() != null) {
            hideSoftKeyboard();
            userNameEditText.setText(SettingsManager.getUserName());
            userNameEditText.setSelection(userNameEditText.getText().toString().length());
        } else {
            showSoftKeyboard(userNameEditText);
        }
        mottoEditText.setText(SettingsManager.getMotto());
        mottoEditText.setSelection(mottoEditText.getText().toString().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_layout:
                showSoftKeyboard(userNameEditText);
                break;
            case R.id.motto_layout:
                showSoftKeyboard(mottoEditText);
                break;
            case R.id.reset_base_color:
                newBaseColor = SettingsManager.getDefaultBaseColor();
                seekBarA.setProgress(Color.alpha(newBaseColor));
                seekBarR.setProgress(Color.red(newBaseColor));
                seekBarG.setProgress(Color.green(newBaseColor));
                seekBarB.setProgress(Color.blue(newBaseColor));
                setColor();
                break;
            case R.id.show_toast_layout:
                showToastCheckBox.toggle();
                SettingsManager.setShowToast(showToastCheckBox.isChecked());
                break;
            case R.id.show_toast_checkbox:
                SettingsManager.setShowToast(showToastCheckBox.isChecked());
                break;
            case R.id.show_month_dash_in_3d_layout:
                showMonthDashIn3DCheckBox.toggle();
                SettingsManager.setShowMonthDashIn3D(showMonthDashIn3DCheckBox.isChecked());
                break;
            case R.id.show_month_dash_in_3d_checkbox:
                SettingsManager.setShowMonthDashIn3D(showMonthDashIn3DCheckBox.isChecked());
                break;
            case R.id.show_weekday_dash_in_3d_layout:
                showWeekdayDashIn3DCheckBox.toggle();
                SettingsManager.setShowWeekdayDashIn3D(showWeekdayDashIn3DCheckBox.isChecked());
                break;
            case R.id.show_weekday_dash_in_3d_checkbox:
                SettingsManager.setShowWeekdayDashIn3D(showWeekdayDashIn3DCheckBox.isChecked());
                break;
            case R.id.start_from_layout:
                SettingsManager.setStartWeekDay(1 - SettingsManager.getStartWeekDay().v);
                startFromSunday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.SUN));
                startFromMonday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.MON));
                break;
            case R.id.start_from_sunday:
                SettingsManager.setStartWeekDay(Weekday.SUN.v);
                startFromSunday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.SUN));
                startFromMonday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.MON));
                break;
            case R.id.start_from_monday:
                SettingsManager.setStartWeekDay(Weekday.MON.v);
                startFromSunday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.SUN));
                startFromMonday.setChecked(SettingsManager.getStartWeekDay().equals(Weekday.MON));
                break;
        }
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        oldUserName = SettingsManager.getUserName();
        oldMotto =  SettingsManager.getMotto();
        oldBaseColor = SettingsManager.getBaseColor();
        oldUserLogin = Util.getLoggedIn();
        oldShowToast = SettingsManager.getShowToast();
        oldShowMonthDashIn3D = SettingsManager.getShowMonthDashIn3D();
        oldShowWeekdayDashIn3D = SettingsManager.getShowWeekdayDashIn3D();
        oldWeekday = SettingsManager.getStartWeekDay();
        oldUpdateTime = SettingsManager.getUpdateTime();
        oldReceivedEventPerPage = SettingsManager.getReceivedEventPerPage();

        userNameLayout.post(new Runnable() {
            @Override
            public void run() {
                newBaseColor = oldBaseColor;
                seekBarA.setProgress(Color.alpha(newBaseColor));
                seekBarR.setProgress(Color.red(newBaseColor));
                seekBarG.setProgress(Color.green(newBaseColor));
                seekBarB.setProgress(Color.blue(newBaseColor));
                setColor();
            }
        });

        setLoginButtonText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean changed = false;
        boolean mottoChanged = false;
        boolean receivedEventPerPageChanged = false;
        if (oldUserName == null) {
            if (SettingsManager.getUserName() != null) changed = true;
        } else {
            if (!oldUserName.equals(SettingsManager.getUserName())) changed = true;
        }
        if (oldMotto == null) {
            if (SettingsManager.getMotto() != null) mottoChanged = true;
        } else {
            if (!oldMotto.equals(SettingsManager.getMotto())) mottoChanged = true;
        }
        if (oldBaseColor != newBaseColor) {
            SettingsManager.setBaseColor(newBaseColor);
            changed = true;
        }
        if (oldShowToast != SettingsManager.getShowToast()) changed = true;
        if (oldShowMonthDashIn3D != SettingsManager.getShowMonthDashIn3D()) changed = true;
        if (oldShowWeekdayDashIn3D != SettingsManager.getShowWeekdayDashIn3D()) changed = true;
        if (!oldWeekday.equals(SettingsManager.getStartWeekDay())) changed = true;
        if (oldUpdateTime != SettingsManager.getUpdateTime()) changed = true;
        if (oldReceivedEventPerPage != SettingsManager.getReceivedEventPerPage())
            receivedEventPerPageChanged = true;

        if (receivedEventPerPageChanged) {
            SettingsManager.setLastUpdateStarsDate(null);
            SettingsManager.setLastUpdateStarsId(null);
            changed = true;
        }

        if (changed) {
            Util.showToast(R.string.refreshing);
            Intent intent = new Intent();
            intent.setAction(Actions.CLICK_AVATAR);
            sendBroadcast(intent);
        } else {
            if (mottoChanged) {
                Util.showToast(R.string.refreshing_motto);
                Intent intent = new Intent();
                intent.setAction(Actions.UPDATE_MOTTO);
                sendBroadcast(intent);
            }
        }

        finish();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) setColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
