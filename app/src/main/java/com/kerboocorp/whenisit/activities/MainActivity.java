package com.kerboocorp.whenisit.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kerboocorp.whenisit.R;
import com.kerboocorp.whenisit.adapters.EventAdapter;
import com.kerboocorp.whenisit.managers.DatabaseManager;
import com.kerboocorp.whenisit.model.Event;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private RecyclerView eventListView;
    private LinearLayoutManager linearLayoutManager;
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        eventListView = (RecyclerView) findViewById(R.id.eventList);
        linearLayoutManager = new LinearLayoutManager(this);
        eventListView.setLayoutManager(linearLayoutManager);
        eventListView.setItemAnimator(new DefaultItemAnimator());

        eventAdapter = new EventAdapter(R.layout.list_card_event, this, getSupportFragmentManager());
        eventListView.setAdapter(eventAdapter);

        DatabaseManager db = new DatabaseManager(this);
        List<Event> eventList = db.getAllUpcomingEvents();
        eventAdapter.addEventList(eventList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new) {
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra("action", "create");
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Event event = (Event) data.getParcelableExtra("event");
                eventAdapter.addEvent(event);

            }
        }

    }

}
