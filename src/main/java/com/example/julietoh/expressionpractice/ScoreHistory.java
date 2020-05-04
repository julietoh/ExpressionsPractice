package com.example.julietoh.expressionpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;

public class ScoreHistory extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        mListView = findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();

        Button home_button = findViewById(R.id.button_home);
        final Button clear_history = findViewById(R.id.button_clear);

        final Intent intent = new Intent(this, StartActivity.class);
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

//        need to code clear table
        clear_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDatabaseHelper.clearTable();
            }
        });

    }


    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add("Date: " + data.getString(1) + "\n"
                    + data.getString(2) + "\n"
                    + data.getString(3) + "\n"
                    + data.getString(4) + "\n"
                    + data.getString(5) + "\n"
                    + data.getString(6) + "\n"
                    + data.getString(7) + "\n");
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
