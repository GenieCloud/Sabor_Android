// Gio Jones
// AD2 - C202606
// DatabaseHelper.java

package com.fullsail.android.host;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version configurations
    private static final String DATABASE_NAME = "bookshelf.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement to create the books table using HostContract constants
    private static final String CREATE_TABLE_BOOKS =
            "CREATE TABLE " + HostContract.TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HostContract.COLUMN_TITLE + " TEXT, " +
                    HostContract.COLUMN_DESCRIPTION + " TEXT, " +
                    HostContract.COLUMN_THUMBNAIL + " TEXT" +
                    ");";

    // Constructor needed to initialize the database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database table when the DB is first created
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + HostContract.TABLE_NAME);
        onCreate(db);
    }
}