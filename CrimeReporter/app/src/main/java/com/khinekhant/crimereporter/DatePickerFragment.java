package com.khinekhant.crimereporter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends CentralizedPickerFragment {
    private DatePicker mDatePicker;


    public static final String EXTRA_DATE = "send date back to crimefragment";


    @Override
    protected View CreateViewForDialog() {
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.crime_date_picker);
        mDatePicker.init(year, month, day,null);
        return view;
    }

    @Override
    protected Date getUserDefinedDate() {
        int year, month, day, hour, min;
                year = mDatePicker.getYear();
                month = mDatePicker.getMonth();
                day =mDatePicker.getDayOfMonth();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
        return new GregorianCalendar(year, month, day, hour, min).getTime();
    }




    public static DatePickerFragment newInstance(Date date){
        DatePickerFragment datePickerFragment=new DatePickerFragment();
       datePickerFragment.setArguments(fragmentArgs(date));
        return datePickerFragment;

    }
}





