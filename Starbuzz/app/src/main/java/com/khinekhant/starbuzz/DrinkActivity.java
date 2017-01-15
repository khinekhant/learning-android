package com.khinekhant.starbuzz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {
    public static final String EXTRA_DRINKNO = "drinknum";
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        Intent intent = getIntent();
        int drinkno = (Integer) intent.getExtras().get(EXTRA_DRINKNO);


        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        TextView textView = (TextView) findViewById(R.id.textView1);
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        checkBox=(CheckBox)findViewById(R.id.favourite);

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID","FAVOURITE"},
                    "_id=?", new String[]{Integer.toString(drinkno)}, null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String des = cursor.getString(1);
                int imgid = cursor.getInt(2);
                boolean fav=(cursor.getInt(3)==1);

                textView.setText(name);
                textView1.setText(des);
                imageView.setImageResource(imgid);
                imageView.setContentDescription(des);
                checkBox.setChecked(fav);


            }

cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavilable", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
    public void onFavouriteClick(View view){
        int drinkno=(Integer) getIntent().getExtras().get("drinkno");
       new UpadteDrinktask().execute(drinkno);

    }
    public class UpadteDrinktask extends AsyncTask<Integer,Void,Boolean>{

      private  ContentValues updatefavcol;
        @Override
        protected void onPreExecute() {
            checkBox=(CheckBox)findViewById(R.id.favourite);
            updatefavcol=new ContentValues();
            updatefavcol.put("FAVOURITE",checkBox.isChecked());
        }
        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkid=drinks[0];
            try {
                SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                db.update("DRINK", updatefavcol, "_id=?", new String[]{Integer.toString(drinkid)});
                db.close();
                return true;
            }catch (SQLiteException e){
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean successful) {
           if(!successful){
               Toast toast=Toast.makeText(DrinkActivity.this,"Update Unsucessful",Toast.LENGTH_SHORT);
               toast.show();
           }
        }
    }
}
