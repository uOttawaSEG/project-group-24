package com.example.eams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
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
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error creating Events table: " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public boolean addEvent(String title, String description, String date, String startTime, String endTime, String location, boolean requiresApproval, String organizerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EVENT_NAME, title);
        values.put(KEY_EVENT_DESCRIPTION, description);
        values.put(KEY_EVENT_DATE, date);
        values.put(KEY_EVENT_TIME, startTime + "-" + endTime);
        values.put(KEY_EVENT_LOCATION, location);
        values.put(KEY_EVENT_ORGANIZER, organizerId);
        values.put(KEY_EVENT_REQUIRES_APPROVAL, requiresApproval ? 1 : 0);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return result != -1;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String[] timeRange = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-");
                    String startTime = timeRange[0];
                    String endTime = timeRange.length > 1 ? timeRange[1] : "";

                    Event event = new Event(
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
                    events.add(event);
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

    public Event getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event event = null;
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(eventId)});

            if (cursor != null && cursor.moveToFirst()) {
                String[] timeRange = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-");
                String startTime = timeRange[0];
                String endTime = timeRange.length > 1 ? timeRange[1] : "";

                event = new Event(
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
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching event by ID: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return event;
    }

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

    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EVENTS, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
        return rowsAffected > 0;
    }
}
