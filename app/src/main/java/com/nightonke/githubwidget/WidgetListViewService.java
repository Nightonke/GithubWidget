package com.nightonke.githubwidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by Weiping on 2016/4/29.
 */

public class WidgetListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(getApplicationContext(), intent);
    }

    public static class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        public MyWidgetFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public int getCount() {
            if (SettingsManager.getListViewContents() == null) return 0;
            else return SettingsManager.getListViewContents().size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.list_view_content_item);
            views.setImageViewBitmap(R.id.image_view, Util.getLoadingBitmap());
            return views;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.list_view_content_item);

            if (SettingsManager.getListViewContent().equals(ListViewContent.EVENT)) {
                views.setImageViewBitmap(R.id.image_view, Util.getReceivedEventBitmap(position));
            } else {
                views.setImageViewBitmap(R.id.image_view, Util.getTrendingBitmap(position));
            }

            Bundle extras = new Bundle();
            extras.putInt(Actions.LIST_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.image_view, fillInIntent);

            return views;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }


    }

}
