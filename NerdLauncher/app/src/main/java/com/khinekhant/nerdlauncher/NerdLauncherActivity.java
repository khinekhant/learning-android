package com.khinekhant.nerdlauncher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nerd_launcher);

        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment=new NerdLauncherFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}
