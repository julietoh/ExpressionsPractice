package com.example.julietoh.expressionpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

public class ScoreDetails extends AppCompatActivity {

    private static final String TAG = "ScoreDetails";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd;
    private Button btnViewData;


    int happyCorrect;
    int happyTot;
    int sadCorrect;
    int sadTot;
    int surpriseCorrect;
    int surpriseTot;
    int angryCorrect;
    int angryTot;
    int total_correct;
    int total_questions;
    int level = 0;
    String levelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_v2);

        btnAdd = findViewById(R.id.btnAdd);
        btnViewData = findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);


        happyCorrect = getIntent().getIntExtra("happyCorrect", 0);
        happyTot = getIntent().getIntExtra("happyTot", 0);
        sadCorrect = getIntent().getIntExtra("sadCorrect", 0);
        sadTot = getIntent().getIntExtra("sadTot", 0);
        surpriseCorrect = getIntent().getIntExtra("surpriseCorrect", 0);
        surpriseTot = getIntent().getIntExtra("surpriseTot", 0);
        angryCorrect = getIntent().getIntExtra("angryCorrect", 0);
        angryTot = getIntent().getIntExtra("angryTot", 0);
        level = getIntent().getIntExtra("level", 0);


        TextView text_view_overall_score = findViewById(R.id.text_view_overall_score);
        TextView text_view_happy_score = findViewById(R.id.text_view_happy_score);
        TextView text_view_sad_score = findViewById(R.id.text_view_sad_score);
        TextView text_view_angry_score = findViewById(R.id.text_view_angry_score);
        TextView text_view_surprise_score = findViewById(R.id.text_view_surprise_score);
        TextView text_view_level = findViewById(R.id.text_view_level);

        total_correct = happyCorrect + sadCorrect + surpriseCorrect + angryCorrect;
        total_questions = happyTot + sadTot + surpriseTot + angryTot;



        Button home_button = findViewById(R.id.button_home);
        final Intent intent = new Intent(this, StartActivity.class);
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        Button back_button = findViewById(R.id.button_back);
        final Intent idIntent = new Intent(this, IdentifyActivity_v2.class);
        final Intent scenarioIntent = new Intent(this, Scenario_v2.class);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (level == 1) {
                    startActivity(idIntent);
                }
                else {
                    startActivity(scenarioIntent);
                }
            }
        });

        if (level == 1) {
            levelName = "Level: Identify";
        }
        if (level == 0) {
            levelName = "Level: Scenario";
        }
        final String overall = "Total score: " + total_correct + "/" + total_questions;
        final String happy = "Happy score: " + happyCorrect + "/" + happyTot;
        final String sad = "Sad score: " + sadCorrect + "/" + sadTot;
        final String surprise = "Surprise score: " + surpriseCorrect + "/" + surpriseTot;
        final String anger = "Angry score: " + angryCorrect + "/" + angryTot;

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        final String time = formatter.format(currentTime);

        text_view_level.setText(levelName);
        text_view_overall_score.setText(overall);
        text_view_happy_score.setText(happy);
        text_view_sad_score.setText(sad);
        text_view_angry_score.setText(surprise);
        text_view_surprise_score.setText(anger);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddData(time, levelName, overall, happy, sad, surprise, anger);
            }
        });

        final Intent historyIntent = new Intent(this, ScoreHistory.class);
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(historyIntent);
            }
        });



    }

    public void AddData(String time, String level, String score, String happy, String sad, String surprise, String anger) {
        boolean insertData = mDatabaseHelper.addData(time, level, score, happy, sad, surprise, anger);

        if (insertData) {
            toastMessage("Your score was saved!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
