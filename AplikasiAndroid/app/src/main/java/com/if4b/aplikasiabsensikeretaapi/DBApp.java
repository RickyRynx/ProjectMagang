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
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_KONFIRMASI_PASSWORD = "konfirmasi_password";
    public static final String COLUMN_ALAMAT = "alamat";
    public static final String COLUMN_JABATAN = "jabatan";
    public static final String COLUMN_NOMOR_HP = "nomor_hp";

    private SQLiteDatabase dbricky;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_KONFIRMASI_PASSWORD + " TEXT,"
            + COLUMN_ALAMAT + " TEXT,"
            + COLUMN_JABATAN + " TEXT,"
            + COLUMN_NOMOR_HP + " TEXT"
            + ")";

    public DBApp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase dbricky) {
        dbricky.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbricky, int oldVersion, int newVersion) {
        dbricky.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(dbricky);
    }


    public void insertData(ContentValues values){
        dbricky.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String username, String password, String konfirmasi_password, String alamat, String jabatan, String noHp) {
        SQLiteDatabase dbricky = this.getReadableDatabase();

        Cursor cursor = dbricky.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_KONFIRMASI_PASSWORD + " = ? AND " + COLUMN_ALAMAT + " = ? AND " + COLUMN_JABATAN + " = ? AND " + COLUMN_NOMOR_HP + " =?", new String[]{username, password, konfirmasi_password, alamat, jabatan, noHp});

        int count = cursor.getCount();

        cursor.close();
        dbricky.close();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


}
