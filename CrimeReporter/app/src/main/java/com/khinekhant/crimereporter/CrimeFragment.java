package com.khinekhant.crimereporter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.UUID;



/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {



    private EditText mCrimeTitle;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mIsCrimeSolved;
    private Button mReportCrime;
    private Button mChooseSuspect;
    private Button mCallButton;
private ImageButton mCrimeImageButton;
    private ImageView mCrimeImageView;


   private FragmentManager manager;
    private Crime mCrime;
    private File mPhotoFile;
    private Bitmap bitmap;
    private Point setSize;
    private PackageManager packageManager;
    private Callbacks mCallbacks;


    private static final String ARG_CRIME_ID="crimeId";
    private static final String DIALOG_DATE="datepicker";
    private static final String DIALOG_TIME="timepicker";
   private static final String ZOOM_IN_IMAGE="zoominimage";
    private static final int REQUEST_Date=0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;


    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks)context;
    }

    //newInstance is created in order to store retrieved crime id instead of retrieving directly from Activity
    //for the sake of fragment felxibility
    public static CrimeFragment newInstance(UUID id){
        Bundle fragmentargument=new Bundle();
        fragmentargument.putSerializable(ARG_CRIME_ID,id);

        CrimeFragment crimeFragment=new CrimeFragment();
        crimeFragment.setArguments(fragmentargument);

    return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //reterive crime id from newInstance method that mainactivity called and use it to get crime from Crimelab
       UUID crimeid=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimeLab.getInstance(getActivity()).getCrimeWithSameId(crimeid);

        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        manager = getFragmentManager();

        mDateButton = (Button) view.findViewById(R.id.date_button);
        mTimeButton = (Button) view.findViewById(R.id.time_button);
        mIsCrimeSolved = (CheckBox) view.findViewById(R.id.crime_checkbox);
        mCrimeTitle = (EditText) view.findViewById(R.id.crime_title);
        mReportCrime = (Button) view.findViewById(R.id.crime_report_button);
        mChooseSuspect = (Button) view.findViewById(R.id.choose_suspect_button);
        mCallButton = (Button) view.findViewById(R.id.call_suspect_button);
        mCrimeImageButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
        mCrimeImageView = (ImageView) view.findViewById(R.id.crime_imageView);
        setCrimeImageView();


        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                updateCrime();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        setCrimeDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_Date);
                datePickerFragment.show(manager, DIALOG_DATE);
            }

        });


        setCrimeTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager, DIALOG_TIME);
            }
        });


        mIsCrimeSolved.setChecked(mCrime.isCrimeSolved());
        mIsCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setCrimeSolved(b);
                mIsCrimeSolved.setChecked(mCrime.isCrimeSolved());
                updateCrime();
            }


        });


        mReportCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = intent.createChooser(intent, getString(R.string.chooser_text));
                startActivity(intent);


                //create intent with sharecompact.intentbuilder which able to build Action_send
                //and Action_send_multiple
                /**  ShareCompat.IntentBuilder.from(getActivity())
                 .setType("text/plain")
                 .setText(getCrimeReport())
                 .setChooserTitle(getString(R.string.chooser_text))
                 .setSubject(getString(R.string.crime_report_subject))
                 .startChooser();*/
            }
        });


        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mChooseSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);

            }
        });

        if (mCrime.getSuspect() != null) {
            mChooseSuspect.setText(mCrime.getSuspect());
        }



        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get location of the table that stored ph numbers
                Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//select the field to read
                String[] queryfield = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                String[] selectionArgs = new String[]{mCrime.getContactid().toString()};

                Cursor readPhDb = getActivity().getContentResolver().query(contactUri, queryfield, selection, selectionArgs, null);
                if (readPhDb.getCount() != 0) {
                    try {
                        readPhDb.moveToFirst();
                        String phNum = readPhDb.getString(0);
                        Uri phNumber = Uri.parse("Ph: " + phNum);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, phNumber);
                        packageManager = getActivity().getPackageManager();
                        if (packageManager.resolveActivity(callIntent, packageManager.MATCH_DEFAULT_ONLY) == null) {
                            mCallButton.setEnabled(false);
                        }
                        startActivity(callIntent);

                    } finally {
                        readPhDb.close();
                    }

                }


            }

        });

//guard against no contact app to prevent the crash
       packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, packageManager.MATCH_DEFAULT_ONLY) == null) {
            mChooseSuspect.setEnabled(false);
        }


        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);

        //intent to launch camera for photo
        final Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //make sure file to store in photo exist and device camera app exist
        boolean canTakePhoto = mPhotoFile != null && takePhoto.resolveActivity(packageManager)
                != null;
        //save the file on filesystem in a specified location by uri
        if (canTakePhoto) {
            Uri photoFileLocation = Uri.fromFile(mPhotoFile);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoFileLocation);
        }

        mCrimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(takePhoto, REQUEST_PHOTO);
            }
        });

        mCrimeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagedialogFragment imagedialogFragment =ImagedialogFragment.newInstance(getActivity(),mPhotoFile);
                imagedialogFragment.show(manager, ZOOM_IN_IMAGE);
            }
        });


//use viewtreeObserver.addongloballayoutlistener to fire an event when a layout pass happen
        //so u can know the actual size of the view not the estimated one
        final ViewTreeObserver mCrimeImageViewObserver = mCrimeImageView.getViewTreeObserver();
        if (mCrimeImageViewObserver != null && mCrimeImageViewObserver.isAlive()) {
            mCrimeImageViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    boolean isfirsttimepass = (mCrimeImageView == null);
                    setSize = new Point();
                    setSize.set(mCrimeImageView.getWidth(), mCrimeImageView.getHeight());

                    if (isfirsttimepass) {
                        setCrimeImageView();
                    }
                    //beware of removing part, u will get error if u dont
                    mCrimeImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        }
            return view;
        }




    private void setCrimeImageView(){
        if(mPhotoFile==null || !mPhotoFile.exists()){
            mCrimeImageView.setImageDrawable(null);
            mCrimeImageView.setClickable(false);

        }else{
         bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),setSize.x,setSize.y);
            mCrimeImageView.setImageBitmap(bitmap);
            mCrimeImageView.setClickable(true);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

           if(requestCode==REQUEST_CONTACT){

           Uri contactUri=data.getData();
            String[] queryField=new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};

            Cursor readFromDeviceDb=getActivity().getContentResolver().query(contactUri,queryField,null,null,null);

            try{
                if(readFromDeviceDb.getCount()==0) {
                    return;
                }
                //after making sure that the cursor is not null then get value from cursor
                readFromDeviceDb.moveToFirst();
                String suspect=readFromDeviceDb.getString(0);
               Long suspectContactId=readFromDeviceDb.getLong(1);
                mCrime.setSuspect(suspect);
                mCrime.setContactid(suspectContactId);
                updateCrime();
                mChooseSuspect.setText(mCrime.getSuspect());


            }finally {
                readFromDeviceDb.close();
            }

        }else if (requestCode==REQUEST_PHOTO){
               updateCrime();
               setCrimeImageView();

           }



        explicitActivityResult(requestCode,resultCode,data);
    }


    public void explicitActivityResult(int reqcode,int rescode,Intent dataforexplicit){
if(reqcode==REQUEST_Date || reqcode==REQUEST_TIME) {
    Date crimedate = (Date) dataforexplicit.getSerializableExtra(CentralizedPickerFragment.EXTRA_DATE);
    updateCrime();
    mCrime.setDate(crimedate);
    if (reqcode == REQUEST_Date) {
        setCrimeDate();
    } else if (reqcode == REQUEST_TIME) {
        setCrimeTime();
    }
}
    }



    private void setCrimeTime() {
       String timeFormat="hh:mm a";
        SimpleDateFormat formatTime=new SimpleDateFormat(timeFormat);
        mTimeButton.setText(formatTime.format(mCrime.getDate()));
    }




    private void setCrimeDate() {
        String dateFormat="EEE MMM dd, yyyy";
        SimpleDateFormat formatDate=new SimpleDateFormat(dateFormat);
        mDateButton.setText(formatDate.format(mCrime.getDate()));
    }




    private String getCrimeReport(){
        String solvedString;
        if(mCrime.isCrimeSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }else{
            solvedString=getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String crimedateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();


        String crimeSuspect=mCrime.getSuspect();
        if(crimeSuspect==null){
            crimeSuspect=getString(R.string.crime_report_no_suspect);
        }else{
            crimeSuspect=getString(R.string.crime_report_suspect, crimeSuspect);
        }

        String crimeReport=getString(R.string.crime_report,mCrime.getTitle(),solvedString,
               crimedateString,crimeSuspect);

        return crimeReport;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(mCrime.getId());
                getActivity().finish();
                default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCrime(){
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }


    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }
}
