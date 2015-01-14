package com.kerboocorp.whenisit.activities;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kerboocorp.whenisit.R;
import com.kerboocorp.whenisit.adapters.EventAdapter;
import com.kerboocorp.whenisit.adapters.SelectEventAdapter;
import com.kerboocorp.whenisit.managers.DatabaseManager;
import com.kerboocorp.whenisit.model.Event;

import java.util.List;

/**
 * Created by chris on 14/01/15.
 */
public class ConfigureWidgetActivity extends ActionBarActivity {

    private int widgetId;

    private Toolbar toolbar;

    private RecyclerView eventListView;
    private LinearLayoutManager linearLayoutManager;
    private SelectEventAdapter selectEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.select_event);
        setSupportActionBar(toolbar);

        eventListView = (RecyclerView) findViewById(R.id.eventList);
        linearLayoutManager = new LinearLayoutManager(this);
        eventListView.setLayoutManager(linearLayoutManager);
        eventListView.setItemAnimator(new DefaultItemAnimator());

        selectEventAdapter = new SelectEventAdapter(R.layout.list_card_event, this, getSupportFragmentManager(), this, widgetId);
        eventListView.setAdapter(selectEventAdapter);

        DatabaseManager db = new DatabaseManager(this);
        List<Event> eventList = db.getAllUpcomingEvents();
        selectEventAdapter.addEventList(eventList);

    }
}
