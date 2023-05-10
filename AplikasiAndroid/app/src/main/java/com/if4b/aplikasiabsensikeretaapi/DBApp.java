package com.if4b.aplikasiabsensikeretaapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBApp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_absen";


    public DBApp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase dbricky) {
        dbricky.execSQL("CREATE TABLE allusers(username Text primary key, password text, konfirmasi_password Text, alamat Text, jabatan Text, no_hp Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbricky, int oldVersion, int newVersion) {
        dbricky.execSQL("DROP TABLE IF EXISTS allusers");
    }


    public Boolean insertData(String username, String password, String konfirmasi, String alamat, String jabatan, String noHp) {
        SQLiteDatabase dbricky = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("konfirmasi_password", konfirmasi);
        contentValues.put("alamat", alamat);
        contentValues.put("jabatan", jabatan);
        contentValues.put("no_hp", noHp);

        long result = dbricky.insert("allusers", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean checkUsername(String username) {
        SQLiteDatabase dbricky = this.getWritableDatabase();

        Cursor cursor = dbricky.rawQuery("SELECT * FROM allusers where username = ?", new String[]{username});


        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPassword(String username, String password) {
        SQLiteDatabase dbricky = this.getWritableDatabase();

        Cursor cursor = dbricky.rawQuery("SELECT * FROM allusers where username = ? and password = ?", new String[]{username, password});


        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


}
