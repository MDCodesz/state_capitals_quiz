package edu.uga.cs.statecapitalsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private QuizDBHelper dbHelper; // Declare a reference to the QuizDBHelper instance
    private QuestionsData questionsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = QuizDBHelper.getInstance(this);
        questionsData = new QuestionsData(this); // Initialize the QuestionsData instance
        questionsData.open(); // Open the database

        CSVDataImporter.importDataFromCSV(this, questionsData);
    }



}
