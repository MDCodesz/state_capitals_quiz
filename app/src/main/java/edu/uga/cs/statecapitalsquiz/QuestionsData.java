package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is facilitates storing and restoring job leads stored.
 */
public class QuestionsData {

    public static final String DEBUG_TAG = "QuestionsData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase   db;
    private SQLiteOpenHelper stateCapitalsQuizDBHelper;
    private static final String[] allColumns = {
            QuizDBHelper.QUESTIONS_COLUMN_ID,
            QuizDBHelper.QUESTIONS_COLUMN_STATE,
            QuizDBHelper.QUESTIONS_COLUMN_CAPITAL,
            QuizDBHelper.QUESTIONS_COLUMN_SECOND_CITY,
            QuizDBHelper.QUESTIONS_COLUMN_THIRD_CITY
    };

    public QuestionsData( Context context ) {
        this.stateCapitalsQuizDBHelper = QuizDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = stateCapitalsQuizDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuestionsData: db open" );
    }

    // Close the database
    public void close() {
        if( stateCapitalsQuizDBHelper != null ) {
            stateCapitalsQuizDBHelper.close();
            Log.d(DEBUG_TAG, "QuestionsData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Question> retrieveAllQuestions() {
        ArrayList<Question> quizQuestions = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizDBHelper.TABLE_QUESTIONS, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.QUESTIONS_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.QUESTIONS_COLUMN_STATE );
                        String state = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.QUESTIONS_COLUMN_CAPITAL );
                        String capital = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.QUESTIONS_COLUMN_SECOND_CITY );
                        String cityOne = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.QUESTIONS_COLUMN_THIRD_CITY );
                        String cityTwo = cursor.getString( columnIndex );

                        // create a new Question object and set its state to the retrieved values
                        Question quizQuestion = new Question( state, capital, cityOne, cityTwo );
                        quizQuestion.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        quizQuestions.add( quizQuestion );
                        Log.d(DEBUG_TAG, "Retrieved Question: " + quizQuestion);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved job leads
        return quizQuestions;
    }

    // Store a new job lead in the database.
    public Question storeQuestion( Question quizQuestion ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Question argument.
        // This is how we are providing persistence to a Question (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( QuizDBHelper.QUESTIONS_COLUMN_STATE, quizQuestion.getStateName());
        values.put( QuizDBHelper.QUESTIONS_COLUMN_CAPITAL, quizQuestion.getCapitalCity() );
        values.put( QuizDBHelper.QUESTIONS_COLUMN_SECOND_CITY, quizQuestion.getSecondCity() );
        values.put( QuizDBHelper.QUESTIONS_COLUMN_THIRD_CITY, quizQuestion.getThirdCity() );

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( QuizDBHelper.TABLE_QUESTIONS, null, values );

        // store the id (the primary key) in the Question instance, as it is now persistent
        quizQuestion.setId( id );

        Log.d( DEBUG_TAG, "Stored new question with id: " + String.valueOf( quizQuestion.getId() ) );

        return quizQuestion;
    }


}

