package com.khinekhant.useofintentaction;

import android.content.Intent;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onSendMessage(View view){
        /**EditText editText=(EditText)findViewById(R.id.message);
        String input=editText.getText().toString();
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setType("images/*");
        intent.putExtra(intent.EXTRA_TEXT,input);
        String choosertitle=getString(R.string.chooser);
        Intent chooseMethod=Intent.createChooser(intent,choosertitle);
        startActivity(chooseMethod);
         */

        EditText editText1=(EditText)findViewById(R.id.message);
        String input1=editText1.getText().toString();
        Intent intent1=new Intent(Intent.ACTION_SEND);
        intent1.setType("text/plain");
        intent1.setType("images/*");
        intent1.putExtra(Intent.EXTRA_TEXT,input1);
        String choosertitle1=getString(R.string.chooser_title);
        Intent.createChooser(intent1,choosertitle1);
        startActivity(intent1);



    }
}
