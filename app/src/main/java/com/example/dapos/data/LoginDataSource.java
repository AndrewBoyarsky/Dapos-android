package com.example.dapos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dapos.AccountCreationResponse;
import com.example.dapos.HttpClient;
import com.example.dapos.data.model.LoggedInUser;
import com.example.dapos.ui.CreateAccRequest;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            DbHelper instance = DbHelper.getInstance();
            SQLiteDatabase readableDatabase = instance.getReadableDatabase();
            Cursor cursor = readableDatabase.query("user", new String[]{"id", "email", "accountId", "name", "password"}, "email=? OR name=?", new String[]{username, username}, null, null, null, "1");
            if (!cursor.moveToNext()) {
                return new Result.Error(new RuntimeException("Not found user"));
            }
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String pass = cursor.getString(cursor.getColumnIndex("password"));
            String accountId = cursor.getString(cursor.getColumnIndex("accountId"));
            long rowid = cursor.getLong(cursor.getColumnIndex("id"));
            cursor.close();
            if (!password.equals(pass)) {
                return new Result.Error(new RuntimeException("Bad credentials"));
            }
            LoggedInUser loggedUser =
                    new LoggedInUser(accountId ,name, password);
            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in: " + e.getMessage()));
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
            String s = HttpClient.getInstance().postBody(new CreateAccRequest(password), "/accounts");
            AccountCreationResponse response = HttpClient.mapper.readValue(s, AccountCreationResponse.class);
            if (!response.getPassword().equals(password)) {
                throw new RuntimeException("Incorrect password matching");
            }
            ContentValues values = new ContentValues(3);
            if (username.contains("@")) {
                values.put("email", username);
            }
            values.put("name", username);
            values.put("accountId", response.getAccount());
            values.put("password", password);
            long id = instance.getWritableDatabase().insert("user", null, values);
            LoggedInUser loggedUser =
                    new LoggedInUser(response.getAccount() ,username, password);
            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error signup:" +  e.getMessage()));
        }
    }


    public void logout() {
        // TODO: revoke authentication
    }
}
