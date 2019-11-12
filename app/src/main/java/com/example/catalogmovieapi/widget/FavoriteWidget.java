package com.example.catalogmovieapi.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.catalogmovieapi.R;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "com.example.catalogmovieapi.EXTRA_ITEM";
    private static final String REFRESH_ACTION = "com.example.catalogmovieapi.appwidget.action.REFRESH";
    private static final String TOAST_ACTION = "com.example.catalogmovieapi.TOAST_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        context.startService(intent);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(FavoriteWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);


    }
    public static void sendRefreshBroadcast(Context context){
        Intent intent = new Intent(REFRESH_ACTION);
        intent.setComponent(new ComponentName(context, FavoriteWidget.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null){
            if (intent.getAction().equals(TOAST_ACTION)){
                int index = intent.getIntExtra(EXTRA_ITEM, 0 ) + 1;
                Toast.makeText(context, "Movie" + index, Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals(REFRESH_ACTION)){
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                ComponentName componentName = new ComponentName(context, FavoriteWidget.class);
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.stack_view);
            }
        }
        super.onReceive(context, intent);
    }
}

