package com.khinekhant.crimereporter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.khinekhant.crimereporter.database.CrimeCursorWarpper;
import com.khinekhant.crimereporter.database.CrimedBHelper;
import com.khinekhant.crimereporter.database.CrimedbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ${KhineKhant} on 0027,08/27/16.
 */
public class CrimeLab {
    private static CrimeLab ourInstance;
  //private   List<Crime> mCrimes;

private SQLiteDatabase mDatabase;
    private Context mContext;
//Singleton class only allowed to create its instance once
    public static CrimeLab getInstance(Context ctext) {
        if (ourInstance == null) {
            ourInstance = new CrimeLab(ctext);
        }
        return ourInstance;
    }
    
    private CrimeLab(Context context) {
       // mCrimes=new ArrayList<>();
        mContext=context.getApplicationContext();
        mDatabase=new CrimedBHelper(mContext).getWritableDatabase();

    }
    //to insert data into db need to put them into contentvalues first
private static ContentValues getContentValues(Crime crime){
    ContentValues values=new ContentValues();
    values.put(CrimeTable.Cols.UUID,crime.getId().toString());
    values.put(CrimeTable.Cols.TITLE,crime.getTitle());
    values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
    values.put(CrimeTable.Cols.SOLVED,crime.isCrimeSolved());
    values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
    values.put(CrimeTable.Cols.CONTACT_ID,crime.getContactid());

    return values;
}
//use values in contentvalues to insert into db
public void addCrime(Crime crime){
    // mCrimes.add(crime);
    ContentValues values=getContentValues(crime);
    mDatabase.insert(CrimeTable.NAME,null,values);
}
   // use values from content to update db
public void updateCrime(Crime crime){
String crimeIdString=crime.getId().toString();
    ContentValues values=getContentValues(crime);
    mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID +"=?",new String[] {crimeIdString});
}

    //read from db to get specific data so that u can update the UI
    //but everytime u want to extract values from cursor, need to extract
    // form each cols in cursor with col index,so not to repeat urself
    // create subclass of cursorwarpper that can take care of this in one place
    private CrimeCursorWarpper queryCrimes(String selectCluase, String[] selectArgs){
        Cursor cursor=mDatabase.query(
                CrimeTable.NAME,
                null,
                selectCluase,
                selectArgs,
                null,
                null,
                null);
        return new CrimeCursorWarpper(cursor);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimeList=new ArrayList<>();
       CrimeCursorWarpper cursor=queryCrimes(null,null);
      try{
            cursor.moveToFirst();
         while(!cursor.isAfterLast()){
             crimeList.add(cursor.getCrime());
             cursor.moveToNext();
         }
        }finally {
          cursor.close();
      }
        return crimeList;
    }


    public Crime getCrimeWithSameId(UUID id) {
        /** for(Crime crime:mCrimes){
         if (crime.getId().equals(id))
         return crime;
         }*/
        CrimeCursorWarpper cursorWarpper = queryCrimes(CrimeTable.Cols.UUID + " =?", new String[]{id.toString()});
        try {
            if (cursorWarpper.getCount() == 0) {
                return null;
            }
            cursorWarpper.moveToFirst();

            return cursorWarpper.getCrime();
        } finally {
            cursorWarpper.close();
        }
    }




public void deleteCrime(UUID crimeid) {
   /** for (int i = 0; i < mCrimes.size(); i++) {
        if (mCrimes.get(i).getId().equals(crimeid))
            mCrimes.remove(i);
    }*/
    String idString=crimeid.toString();
    mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID +"=?",new String[]{idString});
}



    public File getPhotoFile(Crime crime){
        //get a location to store file, passing string let you get specific file
        File externalDirFile=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalDirFile==null){
            return null;

        }
        return new File(externalDirFile,crime.getPhotoFilename());
    }
}
