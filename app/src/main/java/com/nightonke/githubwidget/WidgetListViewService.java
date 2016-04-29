package com.nightonke.githubwidget;

import android.content.Context;
import android.content.Intent;
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

        // 构造
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

        // 在调用getViewAt的过程中，显示一个LoadingView。
        // 如果return null，那么将会有一个默认的loadingView
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.list_view_content_item);
            views.setImageViewBitmap(R.id.image_view, Util.getTrendingBitmap(position));
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
