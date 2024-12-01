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
    private static final String TABLE_EVENT_ATTENDEES = "event_attendees"; // New table for attendees
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT_DATE = "event_date";
    private static final String KEY_EVENT_TIME = "event_time";
    private static final String KEY_EVENT_LOCATION = "event_location";
    private static final String KEY_EVENT_DESCRIPTION = "event_description";
    private static final String KEY_EVENT_ORGANIZER = "event_organizer";
    private static final String KEY_EVENT_REQUIRES_APPROVAL = "requires_approval";

    private static final String KEY_ATTENDEE_EMAIL = "attendee_email";
    private static final String KEY_ATTENDEE_STATUS = "status"; // 'pending' or 'approved'

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

            // Create event_attendees table
            String CREATE_ATTENDEES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_ATTENDEES + " ("
                    + KEY_EVENT_ID + " INTEGER,"
                    + KEY_ATTENDEE_EMAIL + " TEXT NOT NULL,"
                    + KEY_ATTENDEE_STATUS + " TEXT NOT NULL,"
                    + "PRIMARY KEY (" + KEY_EVENT_ID + ", " + KEY_ATTENDEE_EMAIL + "),"
                    + "FOREIGN KEY(" + KEY_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + KEY_EVENT_ID + "),"
                    + "FOREIGN KEY(" + KEY_ATTENDEE_EMAIL + ") REFERENCES Users(email))";
            db.execSQL(CREATE_ATTENDEES_TABLE);
            Log.d("DatabaseHelperForEvent", "Event attendees table created successfully");

            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys = ON;");
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error creating tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_ATTENDEES);
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

    // Method to check if an attendee is registered for an event
    public boolean isAttendeeRegistered(int eventId, String attendeeEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENT_ATTENDEES, null,
                KEY_EVENT_ID + " = ? AND " + KEY_ATTENDEE_EMAIL + " = ?",
                new String[]{String.valueOf(eventId), attendeeEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;  // Attendee is already registered
        }
        if (cursor != null) cursor.close();
        return false;  // Attendee is not registered
    }

    // Method to add an attendee to an event
    public boolean addEventAttendee(int eventId, String attendeeEmail, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the attendee is already added to the event
        if (isAttendeeRegistered(eventId, attendeeEmail)) {
            return false;  // Attendee is already registered, no need to add again
        }

        // Insert the attendee into the event_attendees table
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_ID, eventId);
        values.put(KEY_ATTENDEE_EMAIL, attendeeEmail);
        values.put(KEY_ATTENDEE_STATUS, status);  // 'pending' or 'approved'

        long result = db.insert(TABLE_EVENT_ATTENDEES, null, values);
        return result != -1;  // Return true if insert was successful, false otherwise
    }

    // Method to get all attendees for a specific event
    public List<String> getAttendees(int eventId) {
        List<String> attendees = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ATTENDEE_EMAIL + " FROM " + TABLE_EVENT_ATTENDEES + " WHERE " + KEY_EVENT_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(eventId)});

        try {
            if (cursor.moveToFirst()) {
                do {
                    attendees.add(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ATTENDEE_EMAIL)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching attendees: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return attendees;
    }

    // Helper method to get the current date in "YYYY-MM-DD" format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // Method to delete an event by its ID
    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EVENTS, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
        return rowsAffected > 0;
    }
    public List<Event> getEventsForAttendee(String attendeeEmail) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // First, get the event_ids that the attendee is associated with
            String selectEventIdsQuery = "SELECT " + KEY_EVENT_ID + " FROM " + TABLE_EVENT_ATTENDEES +
                    " WHERE " + KEY_ATTENDEE_EMAIL + " = ?";

            cursor = db.rawQuery(selectEventIdsQuery, new String[]{attendeeEmail});

            List<Integer> eventIds = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    eventIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_ID)));
                } while (cursor.moveToNext());
            }
            cursor.close();

            // If no events found for this attendee, return an empty list
            if (eventIds.isEmpty()) {
                return events;
            }

            // Dynamically build the query based on the eventIds size
            StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_ID + " IN (");
            for (int i = 0; i < eventIds.size(); i++) {
                query.append("?");
                if (i < eventIds.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(")");

            // Convert eventIds list to a String array for the rawQuery
            String[] idArray = new String[eventIds.size()];
            for (int i = 0; i < eventIds.size(); i++) {
                idArray[i] = String.valueOf(eventIds.get(i));
            }

            // Now, execute the query to fetch the event details
            cursor = db.rawQuery(query.toString(), idArray);

            // Iterate through the cursor to construct the Event objects
            if (cursor.moveToFirst()) {
                do {
                    Event event = new Event(
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-")[0],  // Assuming time is stored as "start-end"
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-").length > 1 ? cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TIME)).split("-")[1] : "",
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_LOCATION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_REQUIRES_APPROVAL)) > 0,  // Convert int to boolean
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_ORGANIZER))
                    );
                    events.add(event);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Method to get the status of an attendee for a specific event
    public String getAttendeeStatusForEvent(int eventId, String attendeeEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String status = null; // Default value if no status is found
        Cursor cursor = null;

        try {
            // Query to fetch the status from event_attendees table
            String query = "SELECT " + KEY_ATTENDEE_STATUS + " FROM " + TABLE_EVENT_ATTENDEES +
                    " WHERE " + KEY_EVENT_ID + " = ? AND " + KEY_ATTENDEE_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(eventId), attendeeEmail});

            // If a result is found, retrieve the status
            if (cursor.moveToFirst()) {
                status = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ATTENDEE_STATUS));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelperForEvent", "Error fetching attendee status: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor to free resources
            }
            db.close(); // Close the database
        }

        return status; // Return the status (or null if not found)
    }





}
