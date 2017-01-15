package com.khinekhant.crimereporter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.util.List;
import java.util.UUID;

public class CrimeViewPagerActivity extends AppCompatActivity implements
        CrimeFragment.Callbacks {


    private ViewPager mCirmeViewPager;
    public static final String EXTRA = "data";

    List<Crime> mCrimes;

    public static Intent newIntent(Context context, UUID cID) {
        Intent intent = new Intent(context,CrimeViewPagerActivity.class);
        intent.putExtra(EXTRA, cID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_view_pager);

        UUID crimeId=(UUID)getIntent().getSerializableExtra(EXTRA);

        mCirmeViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mCirmeViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                //connection between this activity and crimeFragment
                return CrimeFragment.newInstance(crime.getId());
            }


        });

for(int i=0;i<mCrimes.size();i++){
    Crime crime=mCrimes.get(i);
    if(crime.getId().equals(crimeId)){
        mCirmeViewPager.setCurrentItem(i);
    }

}
    }


    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}