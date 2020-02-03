package com.example.julietoh.expressionpractice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;

/**
 * Created by julietoh on 11/28/18.
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button buttonID = findViewById(R.id.button_identification);
        Button buttonIM= findViewById(R.id.button_imitation);
        Button buttonSC= findViewById(R.id.button_scenario);

//        MediaPlayer mediaPlayer= MediaPlayer.create( this, R.raw.welcome_dale_2);
//        mediaPlayer.start();
//        final Intent intentQuiz = new Intent(this, StartScreen.class);
        final Intent intentQuiz = new Intent(this, IdentifyActivity_v2.class);
        final Intent intentMain = new Intent(this, ImitationActivity.class);
//        final Intent intentScenario = new Intent(this, ScenarioActivity.class);
        final Intent intentScenario = new Intent(this, Scenario_v2.class);

        buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentQuiz);
            }
        });
        buttonIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentMain);
            }
        });
        buttonSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentScenario);
            }
        });
    }
}
