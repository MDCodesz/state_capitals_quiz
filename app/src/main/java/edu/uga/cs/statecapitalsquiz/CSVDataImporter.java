package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVDataImporter {

    public static void importDataFromCSV(Context context, QuestionsData questionsData) {
//        private static boolean doesDatabaseExist(Context context, String dbName) {
//            File dbFile = context.getDatabasePath(dbName);
//            return dbFile.exists();
//        }


        if (!isDatabaseEmpty(questionsData)) {
            Log.i("Database Status", "Data already exists in the database. Skipping import.");
            return;
        }

        Log.i("Database Status", "Database is empty. First Init.");

        try {
            InputStream inputStream = context.getAssets().open("StateCapitalsOnly.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextRow;

            while ((nextRow = reader.readNext()) != null) {
                if (nextRow.length >= 4) {
                    String state = nextRow[0].trim();
                    String capitalCity = nextRow[1].trim();
                    String secondCity = nextRow[2].trim();
                    String thirdCity = nextRow[3].trim();

                    Question question = new Question(state, capitalCity, secondCity, thirdCity);
                    questionsData.storeQuestion(question);
                }
            }
        } catch (Exception e) {
            Log.e("CSVDataImporter", e.toString());
        }
    }

    private static boolean isDatabaseEmpty(QuestionsData questionsData) {
        // Check if there are any questions in the database
        return questionsData.retrieveAllQuestions().isEmpty();
    }
}
