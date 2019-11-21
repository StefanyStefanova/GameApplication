package com.example.gameapplication;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView hiddenNumberTextView;
    TextView hintTextView;
    TextView guessesTextView;
    TextView instructionsTextView;
    EditText numberEditText;
    Button guessButton;
    Button resetButton;
    Button saveScoreButton;

    TextView livesTextView;
    TextView scoreTextView;

    Random random = new Random();
    final int LIVES = 5;
    int tryToGuess =0;
    int numberToGuess;
    int score =100;


    protected EditText editName;
    protected TextView editScore;
    protected Button btnInsert, btnClose;
    protected ListView simpleList;

    public void resetGame()
    {
        numberToGuess = random.nextInt(101); //101
        tryToGuess =0;
        guessButton.setEnabled(true);
        saveScoreButton.setEnabled(false);
        hiddenNumberTextView.setText("?");
        hintTextView.setText("");
        guessesTextView.setText("");
        numberEditText.setText("");
        score =100;

        livesTextView.setText("Lives: 5");
        scoreTextView.setText("Score: 100");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hiddenNumberTextView = findViewById(R.id.numberToGuessTextView);
        hintTextView = findViewById(R.id.hintTextView);
        guessesTextView = findViewById(R.id.usedNumbersTextView);
        instructionsTextView = findViewById(R.id.instructionsTextView);
        numberEditText = findViewById(R.id.numberEditText);
        guessButton = findViewById(R.id.guessButton);
        resetButton = findViewById(R.id.resetButton);
        saveScoreButton = findViewById(R.id.saveScoreButton);


        guessButton.setOnClickListener(onclick);
        resetButton.setOnClickListener(onclick);
        saveScoreButton.setOnClickListener(onclick);

        livesTextView = findViewById(R.id.LivesTextView);
        scoreTextView = findViewById(R.id.scoreTextView);


        resetGame();
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.resetButton:
                    resetGame();
                    break;

                case R.id.guessButton:
                    makeGuess();
                    break;

                case R.id.saveScoreButton:
                    showLogin();
                    break;
            }
        }
    };



    private void makeGuess()
    {
        String numberAsString = numberEditText.getText().toString();

        if(numberAsString.isEmpty())
        {
            Toast.makeText(this,"Enter the number!",Toast.LENGTH_LONG).show();
            return;
        }

        int number = Integer.parseInt(numberAsString);
        if(number <0 || number > 100)
        {
            Toast.makeText(this,"The number must be between 0 and 100!",Toast.LENGTH_LONG).show();
            return;
        }


        tryToGuess++;
        if( number < numberToGuess )
        {
            hintTextView.setText(" Up ");
            int a =  LIVES-tryToGuess;
            String lives =String.valueOf (a);
            livesTextView.setText("Lives: " +lives +" ");
            numberEditText.setText("");
            score -=20;
            String endScore =String.valueOf(score);
            scoreTextView.setText("Score: " + endScore);
            saveScoreButton.setEnabled(false);

        }
        else if(number > numberToGuess)
        {
            hintTextView.setText(" Down ");
            int a =  LIVES-tryToGuess;
            String lives =String.valueOf (a);
            livesTextView.setText("Lives: " +lives +" ");
            numberEditText.setText("");

            score -=20;
            String endScore =String.valueOf(score);
            scoreTextView.setText("Score: " + endScore);
            saveScoreButton.setEnabled(false);


        }
        else if(number == numberToGuess)
        {
            hintTextView.setText(" You win! ");
            int a =  LIVES-tryToGuess;
            String lives =String.valueOf (a);
            livesTextView.setText("Lives: " +lives +" ");
            numberEditText.setText("");
            guessButton.setEnabled(false);
            hiddenNumberTextView.setText(numberToGuess +"");
            score -=0;
            String endScore =String.valueOf(score);
            scoreTextView.setText("Score: " + endScore);
            saveScoreButton.setEnabled(true);

        }

        if(tryToGuess  == LIVES)
        {
            if(number == numberToGuess)
            {
                hintTextView.setText(" You win! ");
                int a =  LIVES-tryToGuess;
                String lives =String.valueOf (a);
                livesTextView.setText("Lives: " +lives +" ");
                numberEditText.setText("");
                guessButton.setEnabled(false);
                hiddenNumberTextView.setText(numberToGuess +"");
                String endScore =String.valueOf(score);
                scoreTextView.setText("Score: " + endScore);
                saveScoreButton.setEnabled(true);

            }
            else {
                hintTextView.setText(" You lose! ");
                hiddenNumberTextView.setText(numberToGuess + "");
                guessButton.setEnabled(false);
                int a = LIVES - tryToGuess;
                String lives = String.valueOf(a);
                livesTextView.setText("Lives: " + lives + " ");
                numberEditText.setText("");
                scoreTextView.setText("Score: 0");
                saveScoreButton.setEnabled(false);
            }

        }

        guessesTextView.append(number + "  ");




    } // end method makeGuess()



    /// DataBase
    protected void initDB() throws SQLException {
        SQLiteDatabase db=null;
        db=SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/Players2.db",
                null

        );
        String q="CREATE TABLE if not exists Players( ";
        q+="ID integer primary key AUTOINCREMENT, ";
        q+="name text not null, ";
        q+="score int not null, ";
        q+="unique(name, score) ); ";
        db.execSQL(q);
        db.close();



    }



    public void selectDB() throws SQLException{
        SQLiteDatabase db=null;
        db=SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/Players2.db",
                null

        );

        String q="SELECT * FROM Players ORDER BY score DESC; ";
        Cursor c=db.rawQuery(q, null);
        StringBuilder sb=new StringBuilder();
        ArrayList<String> listResults=new ArrayList<String>();
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex("name"));
            String score=c.getString(c.getColumnIndex("score"));
            String ID=c.getString(c.getColumnIndex("ID"));
            listResults.add("\t"+name+"\t"+"\t"+score+"\n");
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.score_list_item,
                R.id.textView,
                listResults
        );
        simpleList.setAdapter(arrayAdapter);
        db.close();
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            selectDB();
        }catch (Exception e){

        }
    }


        private void showLogin()
        {

            String Score =String.valueOf(score);
            setContentView(R.layout.dialog_login);
            editName=findViewById(R.id.NameEditText);
            editScore=findViewById(R.id.ScoreTextView);
            editScore.setText(Score);
            btnInsert=findViewById(R.id.SaveButton);
            btnClose = findViewById(R.id.CancelButton);
            simpleList=findViewById(R.id.simpleListView);


            try{
                initDB();
                selectDB();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        e.getMessage(), Toast.LENGTH_LONG
                ).show();
            }

            btnInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                                SQLiteDatabase db = null;
                                try {
                                    db = SQLiteDatabase.openOrCreateDatabase(
                                            getFilesDir().getPath() + "/Players2.db",
                                            null

                                    );
                                    String name = editName.getText().toString();
                                    String score = editScore.getText().toString();
                                    String s = "INSERT INTO Players(name, score) ";
                                    s += " VALUES(?, ?);";
                                    db.execSQL(s, new Object[]{name, score});
                                    Toast.makeText(getApplicationContext(),
                                            "Record Inserted",
                                            Toast.LENGTH_LONG
                                    ).show();
                                    btnInsert.setEnabled(false);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                } finally {
                                    if (db != null) {
                                        db.close();
                                    }
                                }
                                try {
                                    selectDB();
                                } catch (Exception e) {

                                }


                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GameActivity.this,MenuActivity.class);


                    startActivity(intent);
                }
            });

        }





}

