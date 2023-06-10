package com.if4b.aplikasiabsensikeretaapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;


public class DBApp extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "db_absen";




    public DBApp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase dbricky) {
        final String SQL_CREATE_TABLE = "CREATE TABLE allusers (id INTEGER PRIMARY KEY autoincrement, username TEXT NOT NULL,  email TEXT NOT NULL, password TEXT NOT NULL, konfirmasi_password TEXT NOT NULL, alamat TEXT NOT NULL, jabatan TEXT NOT NULL, ho_hp TEXT NOT NULL)";
        dbricky.execSQL(SQL_CREATE_TABLE);

        final String CREATE_TABLE = "CREATE TABLE adminabsens (id INTEGER PRIMARY KEY autoincrement, nama TEXT NOT NULL, jabatan TEXT NOT NULL, tanggal TEXT NOT NULL, image blob, lokasi TEXT NOT NULL)";
        dbricky.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbricky, int oldVersion, int newVersion) {
        dbricky.execSQL("DROP TABLE IF EXISTS allusers");
        dbricky.execSQL("DROP TABLE IF EXISTS adminabsens");
        onCreate(dbricky);
    }


    public ArrayList<HashMap <String, String>> getAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM allusers";
        SQLiteDatabase dbricky = this.getWritableDatabase();
        Cursor cursor = dbricky.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("username", cursor.getString(1));
                map.put("email", cursor.getString(2));
                map.put("password", cursor.getString(3));
                map.put("konfirmasi_password", cursor.getString(4));
                map.put("alamat", cursor.getString(5));
                map.put("jabatan", cursor.getString(6));
                map.put("no_hp", cursor.getString(7));
                list.add(map);
            } while (cursor.moveToFirst());

        }
        cursor.close();
        return list;
    }

    public ArrayList<HashMap <String, String>> getAbsen() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM adminabsens";
        SQLiteDatabase dbricky = this.getWritableDatabase();
        Cursor cursor = dbricky.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("nama", cursor.getString(1));
                map.put("jabatan", cursor.getString(2));
                map.put("tanggal", cursor.getString(3));
                map.put("poto", cursor.getString(4));
                list.add(map);
            } while (cursor.moveToFirst());

        }
        cursor.close();
        return list;
    }

}


