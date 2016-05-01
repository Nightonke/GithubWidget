package com.nightonke.githubwidget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Weiping on 2016/5/1.
 */

public class LanguageDialog extends Dialog {

    private Context context;
    private OnLanguageSelectedListener onLanguageSelectedListener;

    private ListView listView;
    private TextView cancel;

    public LanguageDialog(Context context) {
        super(context, R.style.ListViewDialog);
        this.context = context;
        try {
            this.onLanguageSelectedListener = (OnLanguageSelectedListener) context;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_language);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (Util.getScreenWidth(context) * 0.8f);
        params.height = (int) (Util.getScreenHeight(context) * 0.8f);
        getWindow().setAttributes(params);

        listView = findView(R.id.list_view);
        cancel = findView(R.id.cancel);

        listView.setAdapter(new LanguageAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsManager.setLanguage(Language.values()[position]);
                if (onLanguageSelectedListener != null)
                    onLanguageSelectedListener.onLanguageSelected();
                LanguageDialog.this.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageDialog.this.dismiss();
            }
        });
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public interface OnLanguageSelectedListener {
        void onLanguageSelected();
    }
}
