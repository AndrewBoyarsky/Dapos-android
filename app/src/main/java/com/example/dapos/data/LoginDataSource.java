package com.example.dapos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dapos.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            DbHelper instance = DbHelper.getInstance();
            SQLiteDatabase readableDatabase = instance.getReadableDatabase();
            Cursor cursor = readableDatabase.query("user", new String[]{"id", "email", "name", "password"}, "email=? OR name=?", new String[]{username, username}, null, null, null, "1");
            if (!cursor.moveToNext()) {
                return new Result.Error(new RuntimeException("Not found user"));
            }
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String pass = cursor.getString(cursor.getColumnIndex("password"));
            long rowid = cursor.getLong(cursor.getColumnIndex("id"));
            cursor.close();
            if (!password.equals(pass)) {
                return new Result.Error(new RuntimeException("Bad credentials"));
            }
            LoggedInUser loggedUser =
                    new LoggedInUser(String.valueOf(rowid) ,name);
            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
    public Result<LoggedInUser> signup(String username, String password) {

        try {
            DbHelper instance = DbHelper.getInstance();
            SQLiteDatabase readableDatabase = instance.getReadableDatabase();
            Cursor cursor = readableDatabase.query("user", new String[]{"id", "email", "name", "password"}, "email=? OR name=?", new String[]{username, username}, null, null, null, "1");
            if (cursor.moveToNext()) {
                return new Result.Error(new RuntimeException("Already exist"));
            }
            cursor.close();
            ContentValues values = new ContentValues(3);
            if (username.contains("@")) {
                values.put("email", username);
            }
            values.put("name", username);
            values.put("password", password);
            long id = instance.getWritableDatabase().insert("user", null, values);
            LoggedInUser loggedUser =
                    new LoggedInUser(String.valueOf(id) ,username);
            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error signup", e));
        }
    }


    public void logout() {
        // TODO: revoke authentication
    }
}
