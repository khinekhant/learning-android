package com.khinekhant.workout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {
public static final String EXTRAID="id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        WorkoutDetailFragment detailFragment=(WorkoutDetailFragment)getFragmentManager().findFragmentById(R.id.fragmentdetail);
        int workoutID=(int)getIntent().getExtras().get(EXTRAID);
        detailFragment.setWorkout(workoutID);
    }
}
