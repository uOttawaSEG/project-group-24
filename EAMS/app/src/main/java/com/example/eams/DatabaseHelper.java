package com.example.eams;
import static android.accounts.AccountManager.KEY_PASSWORD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                + KEY_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

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
}