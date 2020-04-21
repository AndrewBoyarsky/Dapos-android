package com.example.dapos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static DbHelper instance;

    public static DbHelper getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new DbHelper(context);
    }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dapos.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, email VARCHAR(50),name VARCHAR(50), accountId VARCHAR(100), password VARCHAR(100), publicKey VARCHAR(200), privateKey VARCHAR(200))";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS user";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase writableDatabase = getWritableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}