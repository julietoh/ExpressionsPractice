package com.example.julietoh.expressionpractice;

import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.CardView;
import android.os.Handler;

public class IdentifyActivity_v2 extends AppCompatActivity {
    private int questionNum = 0;
    private int score = 0;
    private boolean attempted = false;
    private TextView tvScore;
    private TextView tvQuestion;
    private TextView tvResponse;
    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;

    private QuestionsLib_ID QuestionsLib;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_v2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tvScore = findViewById(R.id.score);
        tvQuestion = findViewById(R.id.textQuestion);
        tvResponse = findViewById(R.id.responseToAnswer);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        image1 = findViewById(R.id.emotion_image_1);
        image2 = findViewById(R.id.emotion_image_2);
        image3 = findViewById(R.id.emotion_image_3);
        image4 = findViewById(R.id.emotion_image_4);
        QuestionsLib = new QuestionsLib_ID(this);
        mediaPlayer= MediaPlayer.create( this, R.raw.show_surprise);
        mediaPlayer.start();

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers1);
                }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers2);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers3);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers4);
            }
        });

    }

    private void cardClicked(String[] selectedAnswer) {
        if (checkAnswer(selectedAnswer)) {
//            Toast.makeText(IdentifyActivity_v2.this, "Good Job!", Toast.LENGTH_SHORT).show();
            incrementPicture();
            mediaPlayer= MediaPlayer.create( this, R.raw.nice_job);
            mediaPlayer.start();
        }
        else {
//            Toast.makeText(IdentifyActivity_v2.this, "Try Again", Toast.LENGTH_SHORT).show();
            attempted = true;
            tvResponse.setText("Try again");
            mediaPlayer= MediaPlayer.create( this, R.raw.try_again);
            mediaPlayer.start();
        }

    }

    private boolean checkAnswer(String[] selectedAnswer) {
        return selectedAnswer[questionNum].equals(QuestionsLib.mCorrectAnswer[questionNum]);

    }

    private void incrementPicture() {
        if (!attempted) {
            score++;
        }
        questionNum++;
        attempted = false;

        // increment score
        String scr = "Score: " + score + "/20";
        tvScore.setText(scr);

        tvResponse.setText("Nice Job!");



        // wait 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 3000); // Millisecond 1000 = 1 sec

        // setting new question
        tvQuestion.setText(QuestionsLib.questions[questionNum]);
        tvResponse.setText("");

        image1.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary1));
        image2.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary2));
        image3.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary3));
        image4.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary4));

    }
}