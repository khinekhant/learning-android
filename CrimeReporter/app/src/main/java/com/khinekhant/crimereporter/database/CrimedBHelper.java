package com.khinekhant.crimereporter.database;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.khinekhant.crimereporter.database.CrimedbSchema.*;

/**
 * Created by ${KhineKhant} on 0030,08/30/16.
 */
public class CrimedBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="crimeDatabase";
    public static final int DB_VERSION=1;
    public CrimedBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ CrimeTable.NAME
                +"("
        +"_id integer primary key autoincrement,"
        +CrimeTable.Cols.UUID+","
        +CrimeTable.Cols.TITLE+","
        +CrimeTable.Cols.DATE+","
        +CrimeTable.Cols.SOLVED+","
        +CrimeTable.Cols.SUSPECT+","
        +CrimeTable.Cols.CONTACT_ID
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
