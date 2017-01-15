package com.khinekhant.workout;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




public class WorkoutDetailFragment extends Fragment {

    private long workoutid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            workoutid = savedInstanceState.getLong("workoutid");
        } else {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            StopwachFragment stopwatch = new StopwachFragment();
            transaction.replace(R.id.fragment_contianer, stopwatch);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack("Stopwatch");
            transaction.commit();

        }
        return inflater.inflate(R.layout.fragment_workout_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        if(view!=null){
            Workout workout=Workout.workouts[(int)workoutid];
            TextView textView=(TextView) view.findViewById(R.id.textView1);
            textView.setText(workout.getName());
            TextView textView1=(TextView)view.findViewById(R.id.textView2);
            textView.setText(workout.getDescription());

        }

    }
    public void setWorkout(long id) {

        workoutid = id;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("workoutid",workoutid);
    }

}
