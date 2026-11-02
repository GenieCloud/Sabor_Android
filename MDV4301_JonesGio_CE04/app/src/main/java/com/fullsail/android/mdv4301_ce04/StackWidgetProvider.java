// Gio Jones
// MDV4301 C202607
// StackWidgetProvider.java

package com.fullsail.android.mdv4301_ce04;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class StackWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW_IMAGE = "com.fullsail.android.mdv4301_ce04.ACTION_VIEW_IMAGE";
    public static final String EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stack);

            Intent serviceIntent = new Intent(context, StackWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.stackView, serviceIntent);

            views.setEmptyView(R.id.stackView, R.id.emptyView);

            Intent clickIntent = new Intent(context, StackWidgetProvider.class);
            clickIntent.setAction(ACTION_VIEW_IMAGE);

            int flags = PendingIntent.FLAG_UPDATE_CURRENT;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_MUTABLE;
            }

            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, flags);
            views.setPendingIntentTemplate(R.id.stackView, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ACTION_VIEW_IMAGE.equals(intent.getAction())) {
            String uriString = intent.getStringExtra(EXTRA_IMAGE_URI);

            if (uriString != null) {

                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(Uri.parse(uriString), "image/*");
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

                if (viewIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(viewIntent);
                } else {
                    Toast.makeText(context, "No image viewer app available", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onReceive(context, intent);
    }
}