package com.khinekhant.crimereporter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeListActivity extends CentralizedFragmentActivity implements CrimeListFragment.Callbacks ,
CrimeFragment.Callbacks{


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getlayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {

if(findViewById(R.id.detail_fragment_container)==null){
    Intent intent=CrimeViewPagerActivity.newIntent(this,crime.getId());
    startActivity(intent);
}else{
    Fragment newDetail=CrimeFragment.newInstance(crime.getId());
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.detail_fragment_container, newDetail)
            .commit();
}
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment=(CrimeListFragment)getSupportFragmentManager().
                findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUserInterface();
    }
}
