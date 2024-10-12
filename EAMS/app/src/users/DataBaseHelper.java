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

    // Books table name
    private static final String TABLE_USERS = "Users";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PASS = "password";
    private static final String KEY_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "adress";

    // Books Table Columns names


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PASS + " TEXT," +
                + KEY_ADDRESS + " TEXT,"
                + KEY_NUMBER + " INTEGER" ")";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }

    public List<User> findBooks(String emailFilter, String firstNameFilter, String lastNameFilter, String passwordFilter, Integer numberFilter, String adressFilter) {
        List<User> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE 1=1 "; // 1=1 so we can use AND
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
            query += " AND " + KEY_PASSWORD + " = ?";
            args.add(passwordFilter);
        }
        if (!numberFilter.isEmpty()) {
            query += " AND " + KEY_NUMBER + " = ?";
            args.add(numberFilter);
        }
        if (!adressFilter.isEmpty()) {
            query += " AND " + KEY_ADRESS + " = ?";
            args.add(adressFilter);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                User book = new User(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)
                );
                result.add(User);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    // Adding new book
    public void addUser(String firstName, String lastName, String email, String password, Integer phoneNum, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.firstName);
        values.put(KEY_LAST_NAME, user.lastName);
        values.put(KEY_EMAIL, user.email);
        values.put(KEY_PASSWORD, user.password);
        values.put(KEY_NUMBER, user.phoneNum);
        values.put(KEY_ADDRESS, user.address);

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }

    public void addUser(User user)


    }
}