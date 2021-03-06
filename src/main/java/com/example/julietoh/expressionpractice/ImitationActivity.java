package com.example.julietoh.expressionpractice;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

public class ImitationActivity extends AppCompatActivity implements Detector.FaceListener, Detector.ImageListener {

    // Variables for checking emotion
    private static final int CAMERA_PERMISSIONS_REQUEST = 42;  //value is arbitrary (between 0 and 255)
    boolean cameraPermissionsAvailable = false;
    boolean isFrontFacingCameraDetected = false;
    private CameraDetector detector = null;
    CameraDetector.CameraType cameraType;
    private SurfaceView cameraView; //SurfaceView used to display camera images
    private Button skipButton;
    private TextView score_text;
    private MediaPlayer mediaPlayer;

    // Variables for question
    private int mQuestionNumber = 0;
    private int mScore = 0;
    private QuestionsLibrary mQuestionsLibrary;
    private ImageView questionImageView;
    private String mCorrectAnswer;
    public static CountDownTimer cTimer = null;
    boolean timerInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initializeUI();
        mQuestionsLibrary = new QuestionsLibrary(this);
        checkForCameraPermissions();
        determineCameraAvailability();
        initializeCameraDetector();
        detector.start();
        updateQuestion();
        mediaPlayer= MediaPlayer.create( this, R.raw.imitate_emotion);
        mediaPlayer.start();

    }

    void startTimer() {
        cTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                updateQuestion();
                timerInProgress = false;
            }
        };
        cTimer.start();
        timerInProgress = true;
    }

    /**
     * Displays next question
     */
    private void updateQuestion() {
        if (mQuestionNumber == 20) {
//            detector.stop();
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra("SCORE", mScore);
            startActivity(intent);
        }
        score_text.setText("Score " + mScore + "/20");
        detector.reset();
        questionImageView.setBackgroundResource(mQuestionsLibrary.getQuestion(mQuestionNumber));
        mCorrectAnswer = mQuestionsLibrary.getCorrectAnswer(mQuestionNumber);
        mQuestionNumber++;
        // Reached last question
    }

    /**
     * Assume device has front facing camera for now
     */
    void determineCameraAvailability() {
        PackageManager manager = getPackageManager();
        isFrontFacingCameraDetected = manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        //set default camera settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //restore the camera type settings
        String cameraTypeName = sharedPreferences.getString("cameraType", CameraDetector.CameraType.CAMERA_FRONT.name());
        if (cameraTypeName.equals(CameraDetector.CameraType.CAMERA_FRONT.name())) {
            cameraType = CameraDetector.CameraType.CAMERA_FRONT;
        }
    }

    private void checkForCameraPermissions() {
        cameraPermissionsAvailable =
                ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermissionsAvailable) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                // showPermissionExplanationDialog(CAMERA_PERMISSIONS_REQUEST);
            } else {
                // No explanation needed, we can request the permission.
                requestCameraPermissions();
            }
        }
    }

    private void requestCameraPermissions() {
        if (!cameraPermissionsAvailable) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSIONS_REQUEST);

            // CAMERA_PERMISSIONS_REQUEST is an app-defined int constant that must be between 0 and 255.
            // The callback method gets the result of the request.
        }
    }

    void initializeCameraDetector() {
        /* Put the SDK in camera mode by using this constructor. The SDK will be in control of
         * the camera. If a SurfaceView is passed in as the last argument to the constructor,
         * that view will be painted with what the camera sees.
         */
        detector = new CameraDetector(this, cameraType, cameraView, 1, Detector.FaceDetectorMode.LARGE_FACES);
        int rate = 15; // 10
        int camFPS = 60;
        detector.setMaxProcessRate(rate);
        detector.setSendUnprocessedFrames(true);
        detector.setFaceListener(this);
        detector.setImageListener(this);
        detector.setDetectAllExpressions(true);
        detector.setDetectAllEmotions(true);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame image, float timestamp) {
        if (faces == null)
            return; //frame was not processed

        if (faces.size() == 0)
            return; //no face found

        // Don't check for correct facial expression if new question hasn't been asked
        if (timerInProgress) {
            return;
        }

        Face face = faces.get(0);

        //Get Emotions
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float disgust = face.emotions.getDisgust();
        float sadness = face.emotions.getSadness();
        float surprise = face.emotions.getSurprise();

        //Some Expressions
        float smile = face.expressions.getSmile();
        float brow_furrow = face.expressions.getBrowFurrow();
        float brow_raise = face.expressions.getBrowRaise();
        float mouth_open = face.expressions.getMouthOpen();

        // Check for correct emotion
        switch (mCorrectAnswer) {
            case "happy":
                if (joy > 95 && smile > 90) {
                    mScore++;
                    startTimer();
                    mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
                    mediaPlayer.start();
                    Toast toast = Toast.makeText(this, "Correct! Expression is happy.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case "sad":
                if ((sadness > 20) || (disgust > 50 && joy < 10)) {
                    mScore++;
                    startTimer();
                    mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
                    mediaPlayer.start();
                    Toast toast = Toast.makeText(this, "Correct! Expression is sad.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case "surprise":
                if (surprise > 10 || mouth_open > 15 || brow_raise > 25) {
                    mScore++;
                    startTimer();
                    mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
                    mediaPlayer.start();
                    Toast toast = Toast.makeText(this, "Correct! Expression is surprise.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case "anger":
                if (anger > 15 || brow_furrow > 10) {
                    mScore++;
                    startTimer();
                    mediaPlayer= MediaPlayer.create( this, R.raw.correct_sound);
                    mediaPlayer.start();
                    Toast toast = Toast.makeText(this, "Correct! Expression is anger.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
        }
    }

    void initializeUI() {
        cameraView = findViewById(R.id.camera_view);
        questionImageView = findViewById(R.id.question_image);
        skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateQuestion();
            }
        });
        score_text = findViewById(R.id.score_text);
    }

    @Override
    public void onFaceDetectionStarted() {

    }

    @Override
    public void onFaceDetectionStopped() {
    }

}