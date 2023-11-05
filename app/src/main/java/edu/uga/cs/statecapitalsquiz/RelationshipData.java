package edu.uga.cs.statecapitalsquiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RelationshipData {
    public static final String DEBUG_TAG = "RelationshipData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper stateCapitalsQuizDBHelper;
    private static final String[] allColumns = {
            QuizDBHelper.RELATIONSHIP_COLUMN_ID,
            QuizDBHelper.RELATIONSHIP_COLUMN_QUESTION_ID,
            QuizDBHelper.RELATIONSHIP_COLUMN_QUIZ_ID,
            QuizDBHelper.RELATIONSHIP_COLUMN_USER_ANSWER
    };

    public RelationshipData( Context context ) {
        this.stateCapitalsQuizDBHelper = QuizDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = stateCapitalsQuizDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "RelationshipData: db open" );
    }

    // Close the database
    public void close() {
        if( stateCapitalsQuizDBHelper != null ) {
            stateCapitalsQuizDBHelper.close();
            Log.d(DEBUG_TAG, "RelationshipData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Relationship> retrieveAllRelationships() {
        ArrayList<Relationship> relationships = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizDBHelper.TABLE_RELATIONSHIPS, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.RELATIONSHIP_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.RELATIONSHIP_COLUMN_QUESTION_ID );
                        long questionId = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.RELATIONSHIP_COLUMN_QUIZ_ID );
                        long quizId = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDBHelper.RELATIONSHIP_COLUMN_USER_ANSWER );
                        String userAnswer = cursor.getString( columnIndex );

                        // create a new Relationship object and set its state to the retrieved values
                        Relationship relationship = new Relationship( questionId, quizId, userAnswer );
                        relationship.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        relationships.add( relationship );
                        Log.d(DEBUG_TAG, "Retrieved Relationship: " + relationship);
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
        return relationships;
    }

    // Store a new job lead in the database.
    public Relationship storeRelationship( Relationship relationship ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Relationship argument.
        // This is how we are providing persistence to a Relationship (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( QuizDBHelper.RELATIONSHIP_COLUMN_QUESTION_ID, relationship.getQuestionId());
        values.put( QuizDBHelper.RELATIONSHIP_COLUMN_QUIZ_ID, relationship.getQuizId() );
        values.put( QuizDBHelper.RELATIONSHIP_COLUMN_USER_ANSWER, relationship.getUserAnswer() );

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( QuizDBHelper.TABLE_RELATIONSHIPS, null, values );

        // store the id (the primary key) in the Relationship instance, as it is now persistent
        relationship.setId( id );

        Log.d( DEBUG_TAG, "Stored new job lead with id: " + String.valueOf( relationship.getId() ) );

        return relationship;
    }
}
