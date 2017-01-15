package com.khinekhant.starbuzz;

import android.content.Intent;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends Activity {
   private Cursor cursor;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> ListView,
                                    View itemview,
                                    int position,
                                    long id){
                if(position==0){
                    Intent intent=new Intent(TopLevelActivity.this,DrinkCategoryActivity.class);
                    startActivity(intent);
                }


            }};
        ListView listView=(ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(itemClickListener);

ListView listView1=(ListView) findViewById(R.id.favlist);
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(this);
             db = sqLiteOpenHelper.getReadableDatabase();
            cursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVOURITE=1", null, null, null, null);


            CursorAdapter cursorAdapter = new SimpleCursorAdapter(TopLevelActivity.this, android.R.layout.simple_list_item_1, cursor,
                    new String[]{"NAME"}, new int[]{android.R.id.text1},0);
            listView1.setAdapter(cursorAdapter);
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database Unavilable", Toast.LENGTH_SHORT);
            toast.show();
        }
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(TopLevelActivity.this,DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO,(int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       try {
           StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
           db = starbuzzDatabaseHelper.getReadableDatabase();
           Cursor newcursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVOURITE=1", null, null, null, null);

           ListView listView1 = (ListView) findViewById(R.id.favlist);
           CursorAdapter cursorAdapter = (CursorAdapter) listView1.getAdapter();
           cursorAdapter.changeCursor(newcursor);
           cursor = newcursor;
       }catch (SQLiteException e){
           Toast toast=Toast.makeText(this, "Database Unavilable", Toast.LENGTH_SHORT);
           toast.show();
       }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();

    }
}
