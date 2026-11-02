// Gio Jones
// AD2 - C202606
// EmployeeDatabaseHelper.java

package com.example.jonesgio15.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_EMPLOYEES = "employees";

    // SQLite columns
    public static final String COLUMN_ID = "_id"; // Required for CursorAdapters
    public static final String COLUMN_FIRST_NAME = "first_name";   // TEXT
    public static final String COLUMN_LAST_NAME = "last_name";     // TEXT
    public static final String COLUMN_EMP_NUMBER = "employee_number"; // INTEGER
    public static final String COLUMN_HIRE_DATE = "hire_date";     // DATETIME string
    public static final String COLUMN_STATUS = "employment_status"; // TEXT

    // SQL statement to create the table structure
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_EMP_NUMBER + " INTEGER, " +
                    COLUMN_HIRE_DATE + " TEXT, " +
                    COLUMN_STATUS + " TEXT" +
                    ");";

    public EmployeeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executes the table creation SQL
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops the old table if it exists and rebuilds it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }
}