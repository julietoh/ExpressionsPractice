package com.example.julietoh.expressionpractice;

import android.content.Context;
import android.content.res.TypedArray;

public class QuestionsLib_ID {
    public String[] mCorrectAnswer = {
            "surprise", "happy", "surprise",
            "sad", "anger", "happy",
            "anger", "surprise", "sad",
            "anger", "sad", "happy", "surprise",
            "anger", "happy", "surprise", "sad",
            "happy", "anger",
            "happy" };
    public TypedArray mImageLibrary1;
    public TypedArray mImageLibrary2;
    public TypedArray mImageLibrary3;
    public TypedArray mImageLibrary4;
    public TypedArray audioLibrary;

    public String[] answers1 = {
            "anger", "happy", "pencil",
            "happy", "surprise", "sad",
            "sad", "anger", "sad",
            "anger", "happy", "surprise", "sad",
            "anger", "sad", "happy", "surprise",
            "happy", "sad",
            "surprise" };
    public String[] answers2 = {
            "happy", "anger", "surprise",
            "sad", "happy", "surprise",
            "sad", "happy", "anger",
            "surprise", "sad", "hat", "anger",
            "happy", "anger", "surprise", "happy",
            "sad", "sad",
            "surprise" };
    public String[] answers3 = {
            "paperclip", "sad", "happy",
            "surprise", "anger", "happy",
            "anger", "sad", "surprise",
            "happy", "anger", "happy", "surprise",
            "sad", "surprise", "anger", "sad",
            "surprise", "anger",
            "happy" };
    public String[] answers4 = {
            "surprise", "surprise", "happy",
            "anger", "sad", "sad",
            "happy", "surprise", "anger",
            "sad", "surprise", "anger", "happy",
            "surprise", "happy", "sad", "anger",
            "anger", "happy",
            "sad" };

    public String[] questions = {
            "show me surprise", "show me happy", "show me surprise",
            "show me sad", "show me anger", "show me happy",
            "show me anger", "show me surprise", "show me sad",
            "show me anger", "show me sad", "show me happy", "show me surprise",
            "show me anger", "show me happy", "show me surprise", "show me sad",
            "show me happy", "show me anger",
            "show me happy" };

    public QuestionsLib_ID(Context context) {
        super();
        mImageLibrary1 = context.getResources().obtainTypedArray(R.array.ID_Images1);
        mImageLibrary2 = context.getResources().obtainTypedArray(R.array.ID_Images2);
        mImageLibrary3 = context.getResources().obtainTypedArray(R.array.ID_Images3);
        mImageLibrary4 = context.getResources().obtainTypedArray(R.array.ID_Images4);
        audioLibrary = context.getResources().obtainTypedArray(R.array.audio_array_ID);
    }

    /**
     * Returns the resource id of the image question
     */
    public int getQuestion(int questionNumber, TypedArray ImageLib) {
        return ImageLib.getResourceId(questionNumber, 0);
    }

    public int getAudio(int questionNumber, TypedArray ImageLib) {
        return ImageLib.getResourceId(questionNumber, 0);
    }

    /**
     * Returns the correct answer to the question
     */
    public String getCorrectAnswer(int questionNumber) {
        return mCorrectAnswer[questionNumber];
    }
}
