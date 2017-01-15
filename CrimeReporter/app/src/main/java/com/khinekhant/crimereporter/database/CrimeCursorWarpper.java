package com.khinekhant.crimereporter.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.khinekhant.crimereporter.Crime;

import java.util.Date;
import java.util.UUID;

import static com.khinekhant.crimereporter.database.CrimedbSchema.CrimeTable;

/**
 * Created by ${KhineKhant} on 0030,08/30/16.
 */
public class CrimeCursorWarpper extends CursorWrapper {
    public CrimeCursorWarpper(Cursor cursor) {
        super(cursor);
    }

    //In cursorwrapper , u can extarct value from cursor here everytime u need
    //now lets get crime from cursor
    public Crime getCrime(){
        String uuid=getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Long date=getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int solved=getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect=getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        Long contact_Id=getLong(getColumnIndex(CrimeTable.Cols.CONTACT_ID));

        Crime crime=new Crime(UUID.fromString(uuid));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setCrimeSolved(solved !=0);
        crime.setSuspect(suspect);
        crime.setContactid(contact_Id);

return crime;
    }
}
