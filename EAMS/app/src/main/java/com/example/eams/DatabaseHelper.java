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
    public boolean isValidUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean validUser = false;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ? AND " + KEY_PASS + " = ?";
            cursor = db.rawQuery(query, new String[]{email, password});

            // Check if the cursor has any result, meaning the email and password match
            validUser = cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return validUser;
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
    public UserRegistration getUserById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        UserRegistration user = null;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{userId});

            if (cursor != null && cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));

                // Create the UserRegistration object without the status
                user = new UserRegistration(firstName, lastName, email, password, phoneNumber, address, role);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching user by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
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
    public UserRegistration getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        UserRegistration user = null;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME));
                String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));

                // Create the UserRegistration object
                user = new UserRegistration(firstName, lastName, userEmail, password, phoneNumber, address, role);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching user by email", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
    }



}