package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is a SQLiteOpenHelper class, which Android uses to create, upgrade, delete an SQLite database in an app.
 * This class is a singleton, following the Singleton Design Pattern.
 * Only one instance of this class will exist. To make sure, the only constructor is private.
 * Access to the only instance is via the getInstance method.
 */
public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "QuizDBHelper";
    private static final String DB_NAME = "statecapitalsquiz.db";
    private static final int DB_VERSION = 1;

    // Define all names (strings) for table and column names for questions table
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_ID = "questionId";
    public static final String QUESTIONS_COLUMN_STATE = "state";
    public static final String QUESTIONS_COLUMN_CAPITAL = "capitalCity";
    public static final String QUESTIONS_COLUMN_SECOND_CITY = "secondCity";
    public static final String QUESTIONS_COLUMN_THIRD_CITY = "thirdCity";

    // Define all names (strings) for table and column names for quizzes table
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_ID = "quizId";
    public static final String QUIZZES_COLUMN_DATE = "quizDate";
    public static final String QUIZZES_COLUMN_TIME = "quizTime";
    public static final String QUIZZES_COLUMN_RESULT = "quizResult";

    // Define all names (strings) for table and column names for relationship table
    public static final String TABLE_RELATIONSHIPS = "question_quiz_relationship";
    public static final String RELATIONSHIP_COLUMN_ID = "relationshipId";
    public static final String RELATIONSHIP_COLUMN_QUESTION_ID = "questionId";
    public static final String RELATIONSHIP_COLUMN_QUIZ_ID = "quizId";
    public static final String RELATIONSHIP_COLUMN_USER_ANSWER = "userAnswer";

    // This is a reference to the only instance for the helper.
    private static QuizDBHelper helperInstance;

    // A Create table SQL statement to create a table for questions.
    private static final String CREATE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + " ("
                    + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTIONS_COLUMN_STATE + " TEXT, "
                    + QUESTIONS_COLUMN_CAPITAL + " TEXT, "
                    + QUESTIONS_COLUMN_SECOND_CITY + " TEXT, "
                    + QUESTIONS_COLUMN_THIRD_CITY + " TEXT"
                    + ")";

    // Create table SQL statement for quizzes (you can define it).
    private static final String CREATE_QUIZZES =
            "CREATE TABLE " + TABLE_QUIZZES + " (" +
                    QUIZZES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUIZZES_COLUMN_DATE + " TEXT, " +
                    QUIZZES_COLUMN_TIME + " TEXT, " +
                    QUIZZES_COLUMN_RESULT + " INTEGER" +
                    ")";

    // Create table SQL statement for relationship (you can define it).
    private static final String CREATE_RELATIONSHIPS =
            "CREATE TABLE " + TABLE_RELATIONSHIPS + " (" +
                    RELATIONSHIP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RELATIONSHIP_COLUMN_QUESTION_ID + " INTEGER, " +
                    RELATIONSHIP_COLUMN_QUIZ_ID + " INTEGER, " +
                    RELATIONSHIP_COLUMN_USER_ANSWER + " TEXT" +
                    ")";

    private QuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized QuizDBHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new QuizDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUESTIONS);
        db.execSQL(CREATE_QUIZZES);
        db.execSQL(CREATE_RELATIONSHIPS);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created");
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZZES + " created");
        Log.d(DEBUG_TAG, "Table " + TABLE_RELATIONSHIPS + " created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELATIONSHIPS);
        onCreate(db);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUESTIONS + " upgraded");
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZZES + " upgraded");
        Log.d(DEBUG_TAG, "Table " + TABLE_RELATIONSHIPS + " upgraded");
    }
}

