package com.khinekhant.crimereporter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ${KhineKhant} on 0029,08/29/16.
 */
public abstract class CentralizedPickerFragment extends DialogFragment{
    protected abstract View CreateViewForDialog();
    protected abstract Date getUserDefinedDate();
    protected  Calendar calendar;

    private static final String ARG_FOR_DATE="crime date";
    public static final String EXTRA_DATE="send date back to crimefragment";



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date crimeDate=(Date)getArguments().getSerializable(ARG_FOR_DATE);
      calendar=Calendar.getInstance();
        calendar.setTime(crimeDate);

        View view=CreateViewForDialog();



        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Pick the Crime Date")
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date date=getUserDefinedDate();
                        sendBackDate(Activity.RESULT_OK,date);

                    }
                })
                .create();

    }




    public static Bundle fragmentArgs(Date crimeDate){
        Bundle fragmentArg=new Bundle();
        fragmentArg.putSerializable(ARG_FOR_DATE,crimeDate);
return fragmentArg;
    }


    public void sendBackDate(int resultcode,Date date){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultcode,intent);
    }
}
