package com.example.booklibrarysqlite;

import static android.accounts.AccountManager.KEY_PASSWORD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Users";

    // Users table name
    private static final String TABLE_USERS = "Users";

    // Table columns
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
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PASS + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_NUMBER + " INTEGER,"
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

    public List<User> findUsers(String emailFilter, String firstNameFilter, String lastNameFilter, String passwordFilter, Integer numberFilter, String addressFilter, String roleFilter) {
        List<User> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE 1=1 ";
        List<String> args = new ArrayList<>();

        if (!emailFilter.isEmpty()) {
            query += " AND " + KEY_EMAIL + " LIKE ?";
            args.add("%" + emailFilter + "%");
        }
        if (!firstNameFilter.isEmpty()) {
            query += " AND " + KEY_FIRST_NAME + " LIKE ?";
            args.add("%" + firstNameFilter + "%");
        }
        if (!lastNameFilter.isEmpty()) {
            query += " AND " + KEY_LAST_NAME + " = ?";
            args.add(lastNameFilter);
        }
        if (!passwordFilter.isEmpty()) {
            query += " AND " + KEY_PASS + " = ?";
            args.add(passwordFilter);
        }
        if (numberFilter != null) {
            query += " AND " + KEY_NUMBER + " = ?";
            args.add(String.valueOf(numberFilter));
        }
        if (!addressFilter.isEmpty()) {
            query += " AND " + KEY_ADDRESS + " = ?";
            args.add(addressFilter);
        }
        if (!roleFilter.isEmpty()) {
            query += " AND " + KEY_ROLE + " = ?";
            args.add(roleFilter);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(0),  // First Name
                        cursor.getString(1),  // Last Name
                        cursor.getString(2),  // Email
                        cursor.getString(3),  // Password
                        cursor.getInt(4),     // Phone Number
                        cursor.getString(5),  // Address
                        cursor.getString(6)   // Role
                );
                result.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public void addUser(String firstName, String lastName, String email, String password, Integer phoneNum, String address, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, firstName);
        values.put(KEY_LAST_NAME, lastName);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASS, password);
        values.put(KEY_NUMBER, phoneNum);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_ROLE, role);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }
}
