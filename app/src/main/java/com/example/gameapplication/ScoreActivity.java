package com.example.gameapplication;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {


    ListView listScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_score);

        listScore = findViewById(R.id.listView);

        try {

            selectDB();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG
            ).show();
        }

    }


  public void selectDB() throws SQLException {
      SQLiteDatabase db=null;
      db=SQLiteDatabase.openOrCreateDatabase(
              getFilesDir().getPath()+"/Players2.db",
              null

      );

      String q="SELECT * FROM Players ORDER BY score DESC LIMIT 3; ";
      Cursor c=db.rawQuery(q, null);
      StringBuilder sb=new StringBuilder();
      ArrayList<String> listResults=new ArrayList<String>();
      while (c.moveToNext()){
          String name=c.getString(c.getColumnIndex("name"));
          String score=c.getString(c.getColumnIndex("score"));
          listResults.add("\t"+"\t"+name+"\t"+"\t"+score+"\n");
      }

      ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
              getApplicationContext(),
              R.layout.score_list_item,
              R.id.textView,
              listResults
      );
      listScore.setAdapter(arrayAdapter);

      db.close();
  }


}
