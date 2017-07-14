package com.sa.walletsa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WalletSAMain";
    private String TABLE_NAME;
    static final String COLUMN_DESCRIPTION = "column_description";
    static final String COLUMN_DATE = "column_date";
    static final String COLUMN_AMOUNT = "column_amount";

    DatabaseHelper(Context context, String table_name)
    {
        super(context, DATABASE_NAME + "_" + table_name + ".db", null, 1);
        TABLE_NAME = table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + TABLE_NAME + "(column_description varchar(20), column_date date, column_amount text)");
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

    boolean insertData(String dscp, String dt, String amt) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("column_description", dscp);
            contentValues.put("column_date", dt);
            contentValues.put("column_amount", amt);
            db.insert(TABLE_NAME, null, contentValues);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    Cursor getData() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from " + TABLE_NAME + " where date(column_date) = date('now') order by date(column_date) desc, rowid desc", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Cursor getDataAll() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from " + TABLE_NAME + " order by date(column_date) desc, rowid desc", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Cursor getData(String dtFrom, String dtTo) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("select * from " + TABLE_NAME + " where date(column_date) between '" + dtFrom + "' and '" + dtTo + "' order by date(column_date) desc, rowid desc", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void deleteData(String desc, String dt, String amt) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, COLUMN_DESCRIPTION + " = ? and " + COLUMN_DATE + " = ? and " + COLUMN_AMOUNT + " = ?", new String[]{desc, dt, amt});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
