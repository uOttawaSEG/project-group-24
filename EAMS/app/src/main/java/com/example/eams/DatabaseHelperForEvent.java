package com.example.eams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelperForEvent extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Events";

    private static final String TABLE_EVENTS = "Events";
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT_DATE = "event_date";
    private static final String KEY_EVENT_TIME = "event_time";
    private static final String KEY_EVENT_LOCATION = "event_location";
    private static final String KEY_EVENT_DESCRIPTION = "event_description";
    private static final String KEY_EVENT_ORGANIZER = "event_organizer";
    private static final String KEY_EVENT_REQUIRES_APPROVAL = "requires_approval";

    public DatabaseHelperForEvent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " ("
                    + KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_EVENT_NAME + " TEXT NOT NULL,"
                    + KEY_EVENT_DATE + " TEXT NOT NULL,"
                    + KEY_EVENT_TIME + " TEXT NOT NULL,"
                    + KEY_EVENT_LOCATION + " TEXT NOT NULL,"
                    + KEY_EVENT_DESCRIPTION + " TEXT,"
                    + KEY_EVENT_ORGANIZER + " TEXT NOT NULL,"
                    + KEY_EVENT_REQUIRES_APPROVAL + " INTEGER NOT NULL,"
                    + "FOREIGN KEY(" + KEY_EVENT_ORGANIZER + ") REFERENCES Users(" + "email" + "))";
            db.execSQL(CREATE_EVENTS_TABLE);
            Log.d("DatabaseHelperForEvent", "Events table created successfully");

            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys = ON;");
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error creating Events table: " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public boolean addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT_DESCRIPTION, event.getEventDescription());
        values.put(KEY_EVENT_DATE, event.getEventDate());
        values.put(KEY_EVENT_TIME, event.getStartTime() + "-" + event.getEndTime());
        values.put(KEY_EVENT_LOCATION, event.getEventLocation());
        values.put(KEY_EVENT_REQUIRES_APPROVAL, event.isRequiresApproval() ? 1 : 0);
        values.put(KEY_EVENT_ORGANIZER, event.getEventOrganizer());

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();

        return result != -1;
    }

    // Helper method to convert cursor to Event
    private Event cursorToEvent(Cursor cursor) {
        String[] timeRange = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-");
        String startTime = timeRange[0];
        String endTime = timeRange.length > 1 ? timeRange[1] : "";

        return new Event(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_DATE)),
                startTime,
                endTime,
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_LOCATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_REQUIRES_APPROVAL)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_ORGANIZER))
        );
    }

    // Method to get upcoming events for a specific organizer
    public List<Event> getUpcomingEvents(String organizerEmail) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_ORGANIZER + " = ? AND " + KEY_EVENT_DATE + " >= ?";
        String currentDate = getCurrentDate(); // Get the current date
        Cursor cursor = db.rawQuery(selectQuery, new String[]{organizerEmail, currentDate});

        try {
            if (cursor.moveToFirst()) {
                do {
                    events.add(cursorToEvent(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching upcoming events: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Method to get past events for a specific organizer
    public List<Event> getPastEvents(String organizerEmail) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_ORGANIZER + " = ? AND " + KEY_EVENT_DATE + " < ?";
        String currentDate = getCurrentDate(); // Get the current date
        Cursor cursor = db.rawQuery(selectQuery, new String[]{organizerEmail, currentDate});

        try {
            if (cursor.moveToFirst()) {
                do {
                    events.add(cursorToEvent(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching past events: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Method to get all events
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    events.add(cursorToEvent(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching events: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Method to get a single event by its ID
    public Event getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Event event = cursorToEvent(cursor);
            cursor.close();
            return event;
        }
        return null; // No event found for this ID
    }

    // Method to update an event
    public boolean updateEvent(int eventId, String title, String description, String date, String startTime, String endTime, String location, boolean requiresApproval, String organizerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EVENT_NAME, title);
        values.put(KEY_EVENT_DESCRIPTION, description);
        values.put(KEY_EVENT_DATE, date);
        values.put(KEY_EVENT_TIME, startTime + "-" + endTime);
        values.put(KEY_EVENT_LOCATION, location);
        values.put(KEY_EVENT_ORGANIZER, organizerId);
        values.put(KEY_EVENT_REQUIRES_APPROVAL, requiresApproval ? 1 : 0);

        int rowsAffected = db.update(TABLE_EVENTS, values, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
        return rowsAffected > 0;
    }

    // Method to delete an event
    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EVENTS, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
        return rowsAffected > 0;
    }

    // Helper method to get the current date in "YYYY-MM-DD" format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
