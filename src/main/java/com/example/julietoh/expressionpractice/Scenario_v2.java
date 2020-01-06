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
import android.widget.ViewFlipper;

import com.airbnb.lottie.LottieAnimationView;

public class Scenario_v2 extends AppCompatActivity {
    private int questionNum = 0;
    private int score = 0;
    private boolean attempted = false;
    private TextView tvScore;
//    private TextView tvQuestion;
    private TextView tvResponse;
    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;

    private QuestionsLib_Scenario QuestionsLib;
    private ImageView imageScenario;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private MediaPlayer mediaPlayer;
    private ViewFlipper viewFlipper;
    private LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_v2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewFlipper = findViewById(R.id.view_flipper);
        tvScore = findViewById(R.id.score);
        tvResponse = findViewById(R.id.responseToAnswer);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        imageScenario = findViewById(R.id.scenario_image);
        image1 = findViewById(R.id.emotion_image_1);
        image2 = findViewById(R.id.emotion_image_2);
        image3 = findViewById(R.id.emotion_image_3);
        image4 = findViewById(R.id.emotion_image_4);
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        QuestionsLib = new QuestionsLib_Scenario(this);

        tvResponse.setText("");

        mediaPlayer= MediaPlayer.create( this, R.raw.happy_scenario_1);
        mediaPlayer.start();

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers1, 1);
                }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers2, 2);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers3, 3);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(QuestionsLib.answers4, 4);
            }
        });

    }

    private void cardClicked(String[] selectedAnswer, int cardNum) {
        if (checkAnswer(selectedAnswer)) {
            mediaPlayer.stop();
            mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
            mediaPlayer.start();
            mediaPlayer= MediaPlayer.create( this, R.raw.nice_job);
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                }
            }, 1000);
            tvResponse.setText("Nice Job!");
            viewFlipper.showNext();
            animationView.playAnimation();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewFlipper.showPrevious();
                    incrementPicture();
                }
            }, 3000);

        }
        else {
            attempted = true;
            tvResponse.setText("Try again");
            mediaPlayer.stop();
            mediaPlayer= MediaPlayer.create( this, R.raw.try_again);
            mediaPlayer.start();
            if (cardNum == 1) {
                image1.setImageResource(R.drawable.wrong_answer);
            }
            else if (cardNum == 2) {
                image2.setImageResource(R.drawable.wrong_answer);
            }
            else if (cardNum == 3) {
                image3.setImageResource(R.drawable.wrong_answer);
            }
            else if (cardNum == 4) {
                image4.setImageResource(R.drawable.wrong_answer);
            }

            final Handler handler = new Handler();
            mediaPlayer= MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary_scenario));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                }
            }, 500);

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

        tvResponse.setText("");
        imageScenario.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibraryScenario));
        image1.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary1));
        image2.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary2));
        image3.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary3));
        image4.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary4));

        mediaPlayer= MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary_scenario));
        mediaPlayer.start();

    }
}