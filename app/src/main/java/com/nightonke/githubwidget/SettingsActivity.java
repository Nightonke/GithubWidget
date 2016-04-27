package com.nightonke.githubwidget;

import android.content.Context;
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

    private String oldUserName;
    private boolean oldShowToast;

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
            case R.id.show_toast_layout:
                showToastCheckBox.toggle();
                SettingsManager.setShowToast(showToastCheckBox.isChecked());
                break;
            case R.id.show_toast_checkbox:
                SettingsManager.setShowToast(showToastCheckBox.isChecked());
                break;
            case R.id.user_name_layout:
                showSoftKeyboard(userNameEditText);
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
