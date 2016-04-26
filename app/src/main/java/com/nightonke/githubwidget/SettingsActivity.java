package com.nightonke.githubwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout showToastLayout;
    private CheckBox showToastCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        showToastLayout = findView(R.id.show_toast_layout);
        showToastCheckBox = findView(R.id.show_toast_checkbox);
        showToastLayout.setOnClickListener(this);
        showToastCheckBox.setOnClickListener(this);
        showToastCheckBox.setChecked(SettingsManager.getShowToast());
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
        }
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
