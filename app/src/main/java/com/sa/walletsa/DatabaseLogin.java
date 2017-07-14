package com.sa.walletsa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseLogin extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WalletSALogin.db";
    private String TABLE_NAME = "LoginActivity";
    static final String column_name = "column_name";
    private static final String column_username = "column_username";
    private static final String column_password = "column_password";

    DatabaseLogin(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + TABLE_NAME + "(column_name varchar(20), column_username varchar(20), column_password varchar(20))");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean insertData(String name, String username, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(column_name, name);
            contentValues.put(column_username, username);
            contentValues.put(column_password, password);
            db.insert(TABLE_NAME, null, contentValues);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    Cursor getDataByUsername(String username) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from " + TABLE_NAME + " where column_username = '" + username + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Cursor getDataByUsernamePassword(String username, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from " + TABLE_NAME + " where column_username = '" + username + "' and column_password = '" + password + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void deleteData(String name, String username, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, column_name + " = ? and " + column_username + " = ? and " + column_password + " = ?", new String[]{name, username, password});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
