package com.khinekhant.workout;


import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class WorkoutListFragment extends ListFragment {
    private WorkoutListListener listener;

    interface WorkoutListListener{
        public void itemclicked(long id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String[] name=new String[Workout.workouts.length];
        for(int i=0;i<Workout.workouts.length;i++){
            name[i]=Workout.workouts[i].getName();
        }

        ArrayAdapter<String> workoutArrayAdapter=new ArrayAdapter<String>(inflater.getContext(),android.R.layout.simple_list_item_1,name);
       setListAdapter(workoutArrayAdapter);
        return   super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener=(WorkoutListListener)context;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
  if(listener!=null){
      listener.itemclicked(id);
  }
    }
}
