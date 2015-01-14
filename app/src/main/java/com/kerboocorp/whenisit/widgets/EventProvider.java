package com.kerboocorp.whenisit.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.kerboocorp.whenisit.R;
import com.kerboocorp.whenisit.managers.DatabaseManager;
import com.kerboocorp.whenisit.model.Event;

/**
 * Created by chris on 14/01/15.
 */
public class EventProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";
    public static String ACTION_WIDGET_RECEIVER = "EVENT_RECEIVER";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_event_layout);

        Intent intent = new Intent(context, EventProvider.class);

        intent.setAction(ACTION_WIDGET_RECEIVER);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.event_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
        }

//        int eventId = intent.getIntExtra("event_id", -1);
//
//        if (eventId != -1) {
//            DatabaseManager db = new DatabaseManager(context);
//            Event event = db.getEvent(eventId);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_event_layout);
//            remoteViews.setTextViewText(R.id.nameTextView, event.getName());
//
//        }

        super.onReceive(context, intent);
    }
}
