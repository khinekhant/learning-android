package com.khinekhant.firstgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    SharedPreferences prefs;
    //String fileName="PREFERENCE_KEY";
   // String keyName="MyString";
    int defaultValue=0;
    public static int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realmain);

        /*Button buttonToPlay=(Button) findViewById(R.id.playButton);
        buttonToPlay.setOnClickListener(this);*/
        prefs=getSharedPreferences(Game2Activity.fileName,MODE_PRIVATE);
        highScore=prefs.getInt(Game2Activity.mKey,defaultValue);

        TextView textHighScore=(TextView) findViewById(R.id.textHiScore);
        textHighScore.setText("High Score\n"+highScore);
    }

    @Override
    public void onClick(View view) {
        /*Intent i=new Intent(this,GameActivity.class);
        startActivity(i);*/
    }
}
