package com.example.julietoh.expressionpractice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.CardView;
import android.os.Handler;
import android.widget.ViewFlipper;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

public class IdentifyActivity_v2 extends AppCompatActivity {
    private Button homeButton;
    private Button scoreDetailsButton;
    private static int questionNum = 0;
    private static int score = 0;
    private String nextQuestion = "";
    private String anger = "anger";
    private String surprise = "surprise";
    private String happy = "happy";
    private String sad = "sad";
    private static boolean attempted = false;
    private static boolean repeat = false;
    private static boolean inRepeat = false;
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
    private ViewFlipper viewFlipper;
    private LottieAnimationView animationView;

    static int questionCount = 0;
    static int happyCorrect = 0;
    static int happyTot = 0;
    static int sadCorrect = 0;
    static int sadTot = 0;
    static int surpriseCorrect = 0;
    static int surpriseTot = 0;
    static int angryCorrect = 0;
    static int angryTot = 0;
    String qType;
    int one = 1;
    static int randomNum;
    static Random rand = new Random();
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_v2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        homeButton = findViewById(R.id.home_button);
        scoreDetailsButton = findViewById(R.id.button_score_details);
        viewFlipper = findViewById(R.id.view_flipper);
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
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        QuestionsLib = new QuestionsLib_ID(this);

        tvQuestion.setText(QuestionsLib.questions[questionNum]);
        tvResponse.setText("");

        String scr = "Score: " + score + "/" + questionCount;
        tvScore.setText(scr);


        final Intent intentHome = new Intent(this, StartActivity.class);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentHome);
            }
        });

        final Intent intentScoreDetails = new Intent(this, ScoreDetails.class);
        scoreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentScoreDetails.putExtra("happyCorrect", happyCorrect);
                intentScoreDetails.putExtra("happyTot", happyTot);
                intentScoreDetails.putExtra("sadCorrect", sadCorrect);
                intentScoreDetails.putExtra("sadTot", sadTot);
                intentScoreDetails.putExtra("surpriseCorrect", surpriseCorrect);
                intentScoreDetails.putExtra("surpriseTot", surpriseTot);
                intentScoreDetails.putExtra("angryCorrect", angryCorrect);
                intentScoreDetails.putExtra("angryTot", angryTot);
                intentScoreDetails.putExtra("level", one);
                startActivity(intentScoreDetails);
            }
        });

        image1.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary1));
        image2.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary2));
        image3.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary3));
        image4.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary4));

        mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
        mediaPlayer.start();

//        mediaPlayer= MediaPlayer.create( this, R.raw.show_surprise);
//        mediaPlayer.start();

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

        // increment score
        int score = happyCorrect + sadCorrect + angryCorrect + surpriseCorrect;
        int questionCount = happyTot + sadTot + angryTot + surpriseTot;
        scr = "Score: " + score + "/" + questionCount;
        tvScore.setText(scr);

    }

    private void cardClicked(String[] selectedAnswer, int cardNum) {
        if (checkAnswer(selectedAnswer, cardNum)) {

            if (!attempted) {
                qType = QuestionsLib.mCorrectAnswer[questionNum];
                if (qType.equals(anger)) {
                    angryCorrect++;
                    angryTot++;
                }
                else if (qType.equals(sad)) {
                    sadCorrect++;
                    sadTot++;
                }
                else if (qType.equals(surprise)) {
                    surpriseCorrect++;
                    surpriseTot++;
                }
                else if (qType.equals(happy)) {
                    happyCorrect++;
                    happyTot++;
                }

                // increment score
                int score = happyCorrect + sadCorrect + angryCorrect + surpriseCorrect;
                int questionCount = happyTot + sadTot + angryTot + surpriseTot;
                String scr = "Score: " + score + "/" + questionCount;
                tvScore.setText(scr);
            }

            inRepeat = false;
            mediaPlayer.stop();
            mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
            mediaPlayer.start();
            mediaPlayer= MediaPlayer.create( this, R.raw.great_work);
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
            if (!attempted) {
                qType = QuestionsLib.mCorrectAnswer[questionNum];
                if (qType.equals(anger)) {
                    angryTot++;
                }
                else if (qType.equals(sad)) {
                    sadTot++;
                }
                else if (qType.equals(surprise)) {
                    surpriseTot++;
                }
                else if (qType.equals(happy)) {
                    happyTot++;
                }
                // increment score
                int score = happyCorrect + sadCorrect + angryCorrect + surpriseCorrect;
                int questionCount = happyTot + sadTot + angryTot + surpriseTot;
                String scr = "Score: " + score + "/" + questionCount;
                tvScore.setText(scr);
            }

            attempted = true;
            nextQuestion = QuestionsLib.mCorrectAnswer[questionNum];
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
            mediaPlayer= MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                }
            }, 850);
        }

    }

    private boolean checkAnswer(String[] selectedAnswer, int cardNum) {
        if (inRepeat) {
            if (cardNum == 1){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return selectedAnswer[questionNum].equals(QuestionsLib.mCorrectAnswer[questionNum]);
        }

    }

    private void incrementPicture() {
        count++;
        if (count == 20) {
            final Intent intentScoreDetails = new Intent(this, ScoreDetails.class);
            intentScoreDetails.putExtra("happyCorrect", happyCorrect);
            intentScoreDetails.putExtra("happyTot", happyTot);
            intentScoreDetails.putExtra("sadCorrect", sadCorrect);
            intentScoreDetails.putExtra("sadTot", sadTot);
            intentScoreDetails.putExtra("surpriseCorrect", surpriseCorrect);
            intentScoreDetails.putExtra("surpriseTot", surpriseTot);
            intentScoreDetails.putExtra("angryCorrect", angryCorrect);
            intentScoreDetails.putExtra("angryTot", angryTot);
            intentScoreDetails.putExtra("level", one);
            startActivity(intentScoreDetails);
        }
        questionCount++;
        if (!attempted) {
            score++;
        }
        if (attempted) {
            repeat = true;
        }
        attempted = false;

        if (repeat) {
            inRepeat = true;
            repeat = false;
            if (nextQuestion.equals(anger)) {
                tvQuestion.setText(QuestionsLib.questions[questionNum]);
                tvResponse.setText("");

                image1.setImageResource(R.drawable.anger_1);
                image2.setImageResource(R.drawable.happy_2);
                image3.setImageResource(R.drawable.surprise_2);
                image4.setImageResource(R.drawable.happy_3);

                mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
                mediaPlayer.start();
            }
            else if (nextQuestion.equals(happy)) {
                tvQuestion.setText(QuestionsLib.questions[questionNum]);
                tvResponse.setText("");

                image1.setImageResource(R.drawable.happy_2);
                image2.setImageResource(R.drawable.anger_1);
                image3.setImageResource(R.drawable.surprise_2);
                image4.setImageResource(R.drawable.sad_2);

                mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
                mediaPlayer.start();
            }
            else if (nextQuestion.equals(surprise)) {
                tvQuestion.setText(QuestionsLib.questions[questionNum]);
                tvResponse.setText("");

                image1.setImageResource(R.drawable.surprise_2);
                image2.setImageResource(R.drawable.happy_2);
                image3.setImageResource(R.drawable.sad_2);
                image4.setImageResource(R.drawable.happy_3);

                mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
                mediaPlayer.start();
            }
            else if (nextQuestion.equals(sad)) {
                tvQuestion.setText(QuestionsLib.questions[questionNum]);
                tvResponse.setText("");

                image1.setImageResource(R.drawable.sad_3);
                image2.setImageResource(R.drawable.happy_2);
                image3.setImageResource(R.drawable.surprise_2);
                image4.setImageResource(R.drawable.happy_3);

                mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
                mediaPlayer.start();
            }
        }

        else {
            int min = 0;
            int max = 18;
            randomNum = rand.nextInt((max - min) + 1) + min;
            questionNum = randomNum;
            questionNum++;
            tvQuestion.setText(QuestionsLib.questions[questionNum]);
            tvResponse.setText("");

            image1.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary1));
            image2.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary2));
            image3.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary3));
            image4.setImageResource(QuestionsLib.getQuestion(questionNum, QuestionsLib.mImageLibrary4));

            mediaPlayer = MediaPlayer.create(this, QuestionsLib.getAudio(questionNum, QuestionsLib.audioLibrary));
            mediaPlayer.start();
        }

    }
}