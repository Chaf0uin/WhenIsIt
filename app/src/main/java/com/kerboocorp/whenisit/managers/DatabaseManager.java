package com.kerboocorp.whenisit.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kerboocorp.whenisit.model.Event;

public class
        DatabaseManager extends SQLiteOpenHelper {
 
    private static final int DATABASE_VERSION = 2;
 
    private static final String DATABASE_NAME = "eventManager";
 
    private static final String TABLE_EVENT = "event";
 
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EXPIRATION_DATE = "expiration_date";
 
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_EXPIRATION_DATE + " DATETIME"
                + ")";
        db.execSQL(CREATE_EVENT_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d("TEST", "ON UPDATE");
    	
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
 
        onCreate(db);
    }
    
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_EXPIRATION_DATE, getDateTime(event.getExpirationDate())); 
     
        // Inserting Row
        db.insert(TABLE_EVENT, null, values);
        db.close(); // Closing database connection
    }
    
    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    
    private Date getDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new Date();
		}
    }
    
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_EVENT, 
        		new String[] { KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_EXPIRATION_DATE }, 
        		KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Event event = new Event();
        event.setId(Integer.parseInt(cursor.getString(0)));
        event.setName(cursor.getString(1));
        event.setDescription(cursor.getString(2));
        event.setExpirationDate(getDateTime(cursor.getString(3)));
        return event;
    }
    
    public List<Event> getAllUpcomingEvents() {
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE " + KEY_EXPIRATION_DATE + " > date('now') ORDER BY " + KEY_EXPIRATION_DATE + " ASC";
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        if (cursor.moveToFirst()) {
            do {
            	Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setExpirationDate(getDateTime(cursor.getString(3)));

                if (event.getExpirationDate().after(new Date())) {
                    eventList.add(event);
                }

            } while (cursor.moveToNext());
        }
     
        return eventList;
    }

    public List<Event> getAllExpiredEvents() {
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE " + KEY_EXPIRATION_DATE + " < date('now') ORDER BY " + KEY_EXPIRATION_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setExpirationDate(getDateTime(cursor.getString(3)));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        return eventList;
    }
    
    public int getEventCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        return cursor.getCount();
    }
    
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_EXPIRATION_DATE, getDateTime(event.getExpirationDate()));
     
        // updating row
        return db.update(TABLE_EVENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }
    
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
}
