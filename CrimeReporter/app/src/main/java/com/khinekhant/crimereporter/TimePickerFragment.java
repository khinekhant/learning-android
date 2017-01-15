package com.khinekhant.crimereporter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends CentralizedPickerFragment {

    private TimePicker mTimePicker;


    @Override
    protected View CreateViewForDialog() {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker,null);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);

        mTimePicker=(TimePicker) view.findViewById(R.id.crime_time_picker);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mTimePicker.setCurrentMinute(min);
            mTimePicker.setCurrentHour(hour);
        }else{
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(min);
        }
        return view;
    }

    @Override
    protected Date getUserDefinedDate() {
        int year, month, day, hour,min;

        year =calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day =calendar.get(Calendar.DAY_OF_MONTH);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            hour=mTimePicker.getCurrentHour();
            min=mTimePicker.getCurrentMinute();
        }else{
            hour=mTimePicker.getHour();
            min=mTimePicker.getMinute();

        }

        Date date=new GregorianCalendar(year,month,day,hour,min).getTime();

        return date;
    }



    public static TimePickerFragment newInstance(Date date) {

       TimePickerFragment timePickerFragment=new TimePickerFragment();
        timePickerFragment.setArguments(fragmentArgs(date));
        return timePickerFragment;
    }

    }
