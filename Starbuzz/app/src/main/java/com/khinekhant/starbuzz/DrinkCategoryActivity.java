package com.khinekhant.starbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends ListActivity {

  private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView=getListView();

     try {
         SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(this);
     db = sqLiteOpenHelper.getReadableDatabase();
        cursor = db.query("DRINK", new String[]{"_id", "NAME"}, null, null, null, null, null);


         CursorAdapter cursorAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,
                 new String[]{"NAME"},new int[]{android.R.id.text1},0);
         listView.setAdapter(cursorAdapter);
     }catch (SQLiteException e){
         Toast toast = Toast.makeText(this, "Database Unavilable", Toast.LENGTH_SHORT);
         toast.show();
     }

    }
    public void onListItemClick(ListView listView, View item, int position, long id){
        Intent intent=new Intent(DrinkCategoryActivity.this,DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINKNO,(int) id);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();

    }
}

