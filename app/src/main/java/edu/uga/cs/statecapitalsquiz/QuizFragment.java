package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {

    private int quizNum;
    private static final String TAG = "ReviewJobLeadsFragment";

    private QuestionsData questionsData = null;
    private List<Question> questionsList;

    private TextView highlightsView;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;


    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(int quizNum) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt( "quizNum", quizNum );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizNum = getArguments().getInt( "quizNum" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        questionsList = new ArrayList<Question>();

        // Create a QuestionsData instance, since we will need to save a new Question to the dn.
        questionsData = new QuestionsData( getActivity() );

        // Open that database for reading of the full list of job leads.
        questionsData.open();

        // Execute the retrieval of the job leads in an asynchronous way
        new QuestionDBReader().execute();
    }


    // Method to get a question from the questionsList by index
    private Question getQuestionByIndex(int index) {
        if (index >= 0 && index < questionsList.size()) {
            return questionsList.get(index);
        } else {
            // Handle index out of bounds or other error condition
            return null;
        }
    }


    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of job leads, asynchronously.
    private class QuestionDBReader extends AsyncTask<Void, List<Question>> {
        // This method will run as a background process to read from db.
        // It returns a list of retrieved Question objects.
        @Override
        protected List<Question> doInBackground( Void... params ) {
            List<Question> questionsList = questionsData.retrieveAllQuestions();

            Log.d( TAG, "QuestionDBReader: Questions retrieved: " + questionsList.size() );

            return questionsList;
        }


        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( List<Question> qList ) {
            Log.d( TAG, "QuestionDBReader: qList.size(): " + qList.size() );
            questionsList.addAll( qList );

            // Example of how to get a question by index
            int index = 0; // Replace with the index you want to access
            Question question = getQuestionByIndex(index);
            if (question != null) {
                String state = question.getStateName();
                String capitalCity = question.getCapitalCity();
                String secondCity = question.getSecondCity();
                String thirdCity = question.getThirdCity();
                // Use the question data as needed

                // Update the highlightsView to display the state
                String sentence = "What is the capital of " + state;
                TextView highlightsView = requireView().findViewById(R.id.highlightsView);
                highlightsView.setText(sentence);

                radioButton1 = requireView().findViewById(R.id.radioButton);
                radioButton2 = requireView().findViewById(R.id.radioButton2);
                radioButton3 = requireView().findViewById(R.id.radioButton3);

                radioButton1.setText(capitalCity);
                radioButton2.setText(secondCity);
                radioButton3.setText(thirdCity);
            }

            // else statment

        }
    }
    
}