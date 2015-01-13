package com.kerboocorp.whenisit.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.kerboocorp.whenisit.R;
import com.kerboocorp.whenisit.managers.DatabaseManager;
import com.kerboocorp.whenisit.model.Event;

public class EventActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;

	private EditText nameEditText;
	private EditText descriptionEditText;

    private static final String TIME_PATTERN = "HH:mm";

    private TextView lblDate;
    private TextView lblTime;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private Event currentEvent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        lblDate = (TextView) findViewById(R.id.lblDate);
        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance(EventActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });


        lblTime = (TextView) findViewById(R.id.lblTime);
        lblTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(EventActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

            }
        });

        if (getIntent().getStringExtra("action").equals("update")) {
            currentEvent = getIntent().getParcelableExtra("event");
            nameEditText.setText(currentEvent.getName());
            descriptionEditText.setText(currentEvent.getDescription());
            lblDate.setText(dateFormat.format(currentEvent.getExpirationDate()));
            lblTime.setText(timeFormat.format(currentEvent.getExpirationDate()));
        } else {
            update();
        }

		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

		if (id == R.id.action_save) {
			
			DatabaseManager db = new DatabaseManager(this);
            Event event = new Event(nameEditText.getText().toString(), descriptionEditText.getText().toString(), calendar.getTime());
			db.addEvent(event);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("event",(Parcelable) event);
            setResult(RESULT_OK, returnIntent);
            finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    private void update() {
        lblDate.setText(dateFormat.format(calendar.getTime()));
        lblTime.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }
}
