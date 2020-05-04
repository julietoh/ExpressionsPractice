package com.example.julietoh.expressionpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Database code adapted from https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "people_table";
    private static final String COL0 = "ID";
    private static final String COL1 = "time";
    private static final String COL2 = "name";
    private static final String COL3 = "score";
    private static final String COL4 = "happy";
    private static final String COL5 = "sad";
    private static final String COL6 = "surprise";
    private static final String COL7 = "anger";



    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 +" TEXT, " +
                COL2 +" TEXT, " +
                COL3 +" TEXT, " +
                COL4 +" TEXT, " +
                COL5 +" TEXT, " +
                COL6 +" TEXT, " +
                COL7 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String time, String name, String score, String happy,
                           String sad, String surprise, String anger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, time);
        contentValues.put(COL2, name);
        contentValues.put(COL3, score);
        contentValues.put(COL4, happy);
        contentValues.put(COL5, sad);
        contentValues.put(COL6, surprise);
        contentValues.put(COL7, anger);

        Log.d(TAG, "addData: Adding " + name  + ", " + score + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
}
