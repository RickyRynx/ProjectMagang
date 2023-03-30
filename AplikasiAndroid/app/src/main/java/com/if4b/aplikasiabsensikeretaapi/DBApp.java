package com.if4b.aplikasiabsensikeretaapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBApp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DB_ricky";
    public static final String TABLE_NAME = "user";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "KONFIRMASI_PASSWORD";
    public static final String COL_5 = "ALAMAT";
    public static final String COL_6 = "JABATAN";
    public static final String COL_7 = "NOMOR_HP";

    public DBApp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, KONFIRMASI_PASSWORD TEXT, ALAMAT TEXT, JABATAN TEXT, NOMOR_HP TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, String password, String konfirmasi_password, String alamat, String jabatan, String nomor_hp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ContentValues = new ContentValues();
        ContentValues.put(COL_2, username);
        ContentValues.put(COL_3, password);
        ContentValues.put(COL_4, konfirmasi_password);
        ContentValues.put(COL_5, alamat);
        ContentValues.put(COL_6, jabatan);
        ContentValues.put(COL_7, nomor_hp);
        long result = db.insert(TABLE_NAME, null, ContentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor login_user(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" Select * from " + TABLE_NAME + " where USERNAME= '" + username + "' and PASSWORD= '" + password + "'", null);
        return res;
    }
}
