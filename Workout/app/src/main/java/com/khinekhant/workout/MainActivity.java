package com.khinekhant.workout;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements WorkoutListFragment.WorkoutListListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void itemclicked(long id) {
        View fragmentcontainer=findViewById(R.id.fragment_contianer);
        if(fragmentcontainer != null){
        WorkoutDetailFragment detailFragment=new WorkoutDetailFragment();

            FragmentTransaction transaction=getFragmentManager().beginTransaction();
        detailFragment.setWorkout(id);
        transaction.replace(R.id.fragment_contianer,detailFragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }else{
            Intent intent=new Intent(this,DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRAID,(int)id);
            startActivity(intent);
        }
}
}
