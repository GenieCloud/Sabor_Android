package com.fullsail.android.mdv4301_ce04;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class FlipperWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flipper);

        Intent serviceIntent = new Intent(context, FlipperWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.flipperView, serviceIntent);
        views.setEmptyView(R.id.flipperView, R.id.flipperEmptyView);

        Intent clickIntent = new Intent(context, StackWidgetProvider.class);
        clickIntent.setAction(StackWidgetProvider.ACTION_VIEW_IMAGE);


        appWidgetManager.updateAppWidget(appWidgetId, views);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.flipperView);
    }
}