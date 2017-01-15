package com.khinekhant.firstgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements View.OnClickListener {

    int correctAnswer;
    Button buttonObjectChoice1;
    Button buttonObjectChoice2;
    Button buttonObjectChoice3;
    TextView textObjectPartA;
    TextView textObjectPartB;
    TextView textScore;
    TextView textLevel;

    int currentScore=0;
    int currentLevel=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The next line loads our UI design to the screen
        setContentView(R.layout.activity_game);


        /*Here we get a working object based on either the button
          or TextView class and base as well as link our new objects
          directly to the appropriate UI elements that we created previously*/

        textObjectPartA = (TextView) findViewById(R.id.textPartA);
        textObjectPartB = (TextView) findViewById(R.id.textPartB);
        buttonObjectChoice1 = (Button) findViewById(R.id.buttonChoice1);
        buttonObjectChoice2 = (Button) findViewById(R.id.buttonChoice2);
        buttonObjectChoice3 = (Button) findViewById(R.id.buttonChoice3);


        buttonObjectChoice1.setOnClickListener(this);
        buttonObjectChoice2.setOnClickListener(this);
        buttonObjectChoice3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int answerGiven=0;
        switch (view.getId()){
            case(R.id.buttonChoice1):
                answerGiven=Integer.parseInt(buttonObjectChoice1.getText().toString());
                break;

            case R.id.buttonChoice2:
                //same as previous case but using the next button
                answerGiven = Integer.parseInt("" + buttonObjectChoice2.getText());
                break;

            case R.id.buttonChoice3:
                //same as previous case but using the next button
                answerGiven = Integer.parseInt("" + buttonObjectChoice3.getText());
                break;

        }

        UpdateScoreLevel(answerGiven);
        setQuestion();
    }

    public void setQuestion(){
        int numRange=currentLevel*3;
        int rNum1=(int)(1+Math.random()*numRange);
        int rNum2=(int)(1+Math.random()*numRange);
        correctAnswer = rNum1 * rNum2;
        int wrongAnswer1 = correctAnswer /2;
        int wrongAnswer2 = correctAnswer + 7;

        //Now we use the setText method of the class on our objects
        //to show our variable values on the UI elements.

        textObjectPartA.setText("" + rNum1);
        textObjectPartB.setText(""+rNum2);

        int randomOptionDisplay=(int)(Math.random()*3);
        switch (randomOptionDisplay){
            case 0:
                buttonObjectChoice1.setText(""+correctAnswer);
                buttonObjectChoice2.setText(""+wrongAnswer1);
                buttonObjectChoice3.setText(""+wrongAnswer2);
                break;

            case 1:
                buttonObjectChoice2.setText(""+correctAnswer);
                buttonObjectChoice3.setText(""+wrongAnswer1);
                buttonObjectChoice1.setText(""+wrongAnswer2);
                break;

            case 2:
                buttonObjectChoice3.setText(""+correctAnswer);
                buttonObjectChoice1.setText(""+wrongAnswer1);
                buttonObjectChoice2.setText(""+wrongAnswer2);
                break;
        }


    }


    public boolean isCorrect(int answerGiven){
        if(answerGiven==correctAnswer){
            Toast.makeText(this,"Well done!", Toast.LENGTH_LONG).show();
            return  true;
        }else{//uh oh!
            Toast.makeText(this,"Sorry that's wrong", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public void UpdateScoreLevel(int answerGiven){
        if(isCorrect(answerGiven)){
            for(int i=1;i<=currentLevel;i++){
                currentScore=currentScore+1;
            }
            currentLevel++;
        }else{
            currentLevel=1;
            currentScore=0;
        }

        textScore.setText("Score: "+currentScore);
        textLevel.setText("Level: "+currentLevel);

    }


}
