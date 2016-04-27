package com.nightonke.githubwidget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout userNameLayout;
    private EditText userNameEditText;

    private LinearLayout showToastLayout;
    private CheckBox showToastCheckBox;
    
    private LinearLayout showMonthDashIn3DLayout;
    private CheckBox showMonthDashIn3DCheckBox;
    
    private LinearLayout showWeekdayDashIn3DLayout;
    private CheckBox showWeekdayDashIn3DCheckBox;

    private String oldUserName;
    private boolean oldShowToast;
    private boolean oldShowMonthDashIn3D;
    private boolean oldShowWeekdayDashIn3D;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_layout:
                showSoftKeyboard(userNameEditText);
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
        }
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        oldUserName = SettingsManager.getUserName();
        oldShowToast = SettingsManager.getShowToast();
        oldShowMonthDashIn3D = SettingsManager.getShowMonthDashIn3D();
        oldShowWeekdayDashIn3D = SettingsManager.getShowWeekdayDashIn3D();
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean changed = false;
        if (oldUserName == null) {
            if (SettingsManager.getUserName() != null) changed = true;
        } else {
            if (!oldUserName.equals(SettingsManager.getUserName())) changed = true;
        }
        if (oldShowToast != SettingsManager.getShowToast()) changed = true;
        if (oldShowMonthDashIn3D != SettingsManager.getShowMonthDashIn3D()) changed = true;
        if (oldShowWeekdayDashIn3D != SettingsManager.getShowWeekdayDashIn3D()) changed = true;

        if (changed) {
            Util.showToast(R.string.refreshing);
            Intent intent = new Intent();
            intent.setAction(Actions.CLICK_AVATAR);
            sendBroadcast(intent);
        }

        finish();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

}
