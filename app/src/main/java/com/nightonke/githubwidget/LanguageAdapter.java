package com.nightonke.githubwidget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Weiping on 2016/5/1.
 */

public class LanguageAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return Language.values().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_language_item, null);

        String text = Language.values()[position].v;

        TextView textView = (TextView) convertView.findViewById(R.id.text_view);
        textView.setText(text);

        return convertView;
    }

}
