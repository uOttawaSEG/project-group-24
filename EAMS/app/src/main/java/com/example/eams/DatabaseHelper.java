package com.example.eams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Users";

    private static final String TABLE_USERS = "Users";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PASS = "password";
    private static final String KEY_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROLE = "role";
    private static final String KEY_STATUS = "status";

    // Add these constants for the Events table
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_TITLE = "title";
    private static final String COLUMN_EVENT_DESCRIPTION = "description";
    private static final String COLUMN_EVENT_DATE = "date";
    private static final String COLUMN_EVENT_START_TIME = "start_time";
    private static final String COLUMN_EVENT_END_TIME = "end_time";
    private static final String COLUMN_EVENT_ADDRESS = "address";
    private static final String COLUMN_EVENT_APPROVAL_REQUIRED = "approval_required";
    private static final String COLUMN_EVENT_ORGANIZER_ID = "organizer_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement for creating the Users table
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PASS + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_NUMBER + " TEXT,"
                + KEY_ROLE + " TEXT,"
                + KEY_STATUS + " TEXT DEFAULT 'pending')";
        db.execSQL(CREATE_USERS_TABLE);

        // Add Events table creation
        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + "("
                + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EVENT_TITLE + " TEXT NOT NULL, "
                + COLUMN_EVENT_DESCRIPTION + " TEXT NOT NULL, "
                + COLUMN_EVENT_DATE + " TEXT NOT NULL, "
                + COLUMN_EVENT_START_TIME + " TEXT NOT NULL, "
                + COLUMN_EVENT_END_TIME + " TEXT NOT NULL, "
                + COLUMN_EVENT_ADDRESS + " TEXT NOT NULL, "
                + COLUMN_EVENT_APPROVAL_REQUIRED + " INTEGER NOT NULL, "
                + COLUMN_EVENT_ORGANIZER_ID + " TEXT NOT NULL"
                + ")";
        db.execSQL(createEventsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        // Create tables again
        onCreate(db);
    }

    public boolean addUser(String firstName, String lastName, String email, String password, String phoneNum, String address, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, firstName);
        values.put(KEY_LAST_NAME, lastName);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASS, password);
        values.put(KEY_NUMBER, phoneNum);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection

        // Check if insert was successful (result != -1 means success)
        return result != -1;
    }

    // Check if the email exists in the database
    public boolean isValidUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});
            exists = cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return exists;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String role = null;

        try {
            // SQL query to get the role of the user by email
            String query = "SELECT " + KEY_ROLE + " FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                // Use getColumnIndexOrThrow to ensure column exists
                role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));
                Log.d("DatabaseHelper", "Role found: " + role);
            } else {
                Log.e("DatabaseHelper", "No user found with email: " + email);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching role", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();  // Close the database connection
        }

        return role;
    }

    public String getKeyStatus(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String status = null;

        try {
            // SQL query to get the role of the user by email
            String query = "SELECT " + KEY_STATUS + " FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                // Use getColumnIndexOrThrow to ensure column exists
                status = cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS));
                Log.d("DatabaseHelper", "Status found: " + status);
            } else {
                Log.e("DatabaseHelper", "No user found with email: " + email);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching role", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();  // Close the database connection
        }

        return status;
    }

    public List<UserRegistration> connectToDatabase() {
        List<UserRegistration> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS));
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUMBER));
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS));
                    String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));

                    UserRegistration user = new UserRegistration(firstName, lastName, email, password, phoneNumber, address, role);
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reading user data", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userList;
    }


    // Method to get users with "pending" status
    public List<UserRegistration> getUserStatus(String status) {
        List<UserRegistration> pendingUsers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_STATUS + " = ?";
            cursor = db.rawQuery(query, new String[]{status});

            if (cursor.moveToFirst()) {
                do {
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS));
                    String phoneNum = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUMBER));
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS));
                    String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));

                    UserRegistration user = new UserRegistration(firstName, lastName, email, password, phoneNum, address, role);
                    pendingUsers.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching pending users", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return pendingUsers;
    }

    public boolean updateUserStatus(String email, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, newStatus); // Use the constant for the status column

        // Use the email to identify the user for the update
        int result = db.update(TABLE_USERS, values, KEY_EMAIL + " = ?", new String[]{email});
        db.close();
        return result > 0; // Returns true if at least one row was updated
    }

    // Add this method to save events
    public boolean addEvent(String title, String description, String date, 
                          String startTime, String endTime, String address, 
                          boolean approvalRequired, String organizerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_START_TIME, startTime);
        values.put(COLUMN_EVENT_END_TIME, endTime);
        values.put(COLUMN_EVENT_ADDRESS, address);
        values.put(COLUMN_EVENT_APPROVAL_REQUIRED, approvalRequired ? 1 : 0);
        values.put(COLUMN_EVENT_ORGANIZER_ID, organizerId);

        long result = db.insert(TABLE_EVENTS, null, values);
        return result != -1;
    }

}