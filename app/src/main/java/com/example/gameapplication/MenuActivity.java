package com.example.gameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button gameButton;
    Button scoreButton;
    Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        gameButton = findViewById(R.id.gameButton);
        scoreButton = findViewById(R.id.scoreButton);
        helpButton = findViewById(R.id.helpButton);

        gameButton.setOnClickListener(onClick);
        scoreButton.setOnClickListener(onClick);
        helpButton.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = null;

            switch (v.getId())
            {
                case R.id.gameButton:
                    intent = new Intent(MenuActivity.this,GameActivity.class);
                    break;

                case R.id.scoreButton:
                   intent = new Intent(MenuActivity.this,ScoreActivity.class);

                    break;

                case R.id.helpButton:
                    intent = new Intent(MenuActivity.this,HelpActivity.class);

                    break;
            }

            startActivity(intent);
        }
    };










}
