package com.khinekhant.workout;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopwachFragment extends Fragment implements View.OnClickListener {

private static double seconds=0.0;
    private boolean running;
    private boolean wasrunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            seconds=savedInstanceState.getDouble("seconds");
            running=savedInstanceState.getBoolean("running");
            wasrunning=savedInstanceState.getBoolean("wasrun");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=getView();
        Button button1=(Button)view.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2=(Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3=(Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        runtimer();
        return inflater.inflate(R.layout.fragment_stopwach, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(wasrunning){
            running=true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(wasrunning){
            wasrunning=running;
            running=false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
   outState.putDouble("seconds",seconds);
        outState.putBoolean("running",running);
        outState.putBoolean("wasrun",wasrunning);

    }

    //Start the stopwatch running when the Start button is clicked.
    public void onClickStart(View view) {
        running = true;
    }

    //Stop the stopwatch running when the Stop button is clicked.
    public void onClickStop(View view) {
        running = false;
    }

    //Reset the stopwatch when the Reset button is clicked.
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }


    public void runtimer(){
    View view=getView();
    final TextView textView=(TextView)view.findViewById(R.id.showtime);
    final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double hours = seconds / 3600;
                double minutes = (seconds % 3600) / 60;
                double sec = seconds % 60;
                String timer = String.format("%0d,%02d,%02d", hours, minutes, sec);
                textView.setText(timer);
                if(running){
                    seconds++;
                }
                handler.postDelayed(this,1000);
            }

        });



}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                onClickStart(view);
                break;
            case R.id.button2:
                onClickReset(view);
                break;
            case R.id.button3:
                onClickStop(view);
        }
    }
}
