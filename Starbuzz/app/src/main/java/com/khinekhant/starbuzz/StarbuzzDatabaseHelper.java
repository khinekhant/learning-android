package com.khinekhant.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 0028,07/28/16.
 */
public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private final static String DBname="starbuzz";
    private final static int version=2;

    public StarbuzzDatabaseHelper(Context context){

        super (context,DBname,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
updateMydb(sqLiteDatabase,0,version);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldver, int newver) {
        updateMydb(sqLiteDatabase,oldver,newver);

    }
    public void updateMydb(SQLiteDatabase sqLiteDatabase,int oldver,int newver) {
        if (oldver < 1) {
            sqLiteDatabase.execSQL("CREATE TABLE DRINK(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"NAME TEXT,"
                    +"DESCRIPTION TEXT,"+
                    "IMAGE_RESOURCE_ID INTEGER);");
            insertData(sqLiteDatabase,"Latte","Strong expresso and milk",R.drawable.latte);
            insertData(sqLiteDatabase,"Cappachino","Expresso, hot milk",R.drawable.cappuccino);
            insertData(sqLiteDatabase,"Filter","Our best drip coffee",R.drawable.filter);

        }
        if (oldver<2){
            sqLiteDatabase.execSQL("ALTER TABLE DRINK ADD COLUMN FAVOURITE NUMERIC;");
        }
    }
    public void insertData(SQLiteDatabase sqLiteDatabase,String name,String des,int imgID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("DESCRIPTION", des);
        contentValues.put("IMAGE_RESOURCE_ID", imgID);
        sqLiteDatabase.insert("DRINK", null, contentValues);
    }


}
