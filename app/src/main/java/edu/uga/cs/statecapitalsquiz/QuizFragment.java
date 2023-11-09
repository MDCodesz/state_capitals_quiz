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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private Random random;
    private int correctAnswer; //store the index of the correct answer (the capital city)
    private int score = 0;


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
        View view =inflater.inflate(R.layout.fragment_quiz, container, false);
        return view;
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
        random = new Random(); //initialiize random generator

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId){
                //check if user selected correct answer
                RadioButton selectedRadioButton = view.findViewById(checkId);
                int selected = radioGroup.indexOfChild(selectedRadioButton);
                if(selected == correctAnswer){
                    score++;
                }
            }
        });
        //prints the score of the user
        TextView userScore = view.findViewById(R.id.textView2);
        userScore.setText("Score:" + score);
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

    //attempt at reading random 6 question
    private void displayRandomQuestions(){
        //Collections.shuffle(questionsList);
        int totalQuestion  = questionsList.size();
        int sixQuestions = 6;

        Set<Integer> selectedQuestions = new HashSet<>();
        while(selectedQuestions.size() < sixQuestions) {
            int randIndex = random.nextInt(totalQuestion);
            selectedQuestions.add(randIndex);
        }
        //to display 6 questions from shuffled list
        //for(int i =0; i<6 && i < questionsList.size(); i++){
        for(int i : selectedQuestions){
            Question question = questionsList.get(i);
            //to display the question and its options as necessary
            String questionNum = String.valueOf(question.getId());
            String state = question.getStateName();
            String capitalCity = question.getCapitalCity();
            String secondCity = question.getSecondCity();
            String thirdCity = question.getThirdCity();

            // Update UI elements with the question data
            TextView highlightsView = requireView().findViewById(R.id.highlightsView);
            highlightsView.setText("What is the capital of " + state);
//            //prints the score of the user
//            TextView userScore = requireView().findViewById(R.id.textView2);
//            userScore.setText("Score:" + score);
            //TextView questionNumber = requireView().findViewById(R.id.textView);
            //questionNumber.setText(questionNum);
            //shuffle answer choices
            String[] cities = new String[]{
                  question.getCapitalCity(),
                  question.getSecondCity(),
                  question.getThirdCity()
            };
            shuffleChoices(cities);
            //keeps track of correct answer
            correctAnswer = Arrays.asList(cities).indexOf(question.getCapitalCity());
            shuffleRadioButtons(radioButton1, radioButton2, radioButton3);
            radioButton1 = requireView().findViewById(R.id.radioButton);
            radioButton2 = requireView().findViewById(R.id.radioButton2);
            radioButton3 = requireView().findViewById(R.id.radioButton3);

            radioButton1.setText(cities[0]);
            radioButton2.setText(cities[1]);
            radioButton3.setText(cities[2]);


        }
    }
    // Helper method to shuffle RadioButtons
    private void shuffleRadioButtons(RadioButton... radioButtons) {
        for (int i = radioButtons.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Swap the text of RadioButtons at index i and index
            String tempText = radioButtons[i].getText().toString();
            radioButtons[i].setText(radioButtons[index].getText());
            radioButtons[index].setText(tempText);
        }
    }
    private void shuffleChoices(String[] array){
        for(int i = array.length - 1;i > 0;i--){
            int index = random.nextInt(i + 1);
            //Swap the elements
            String temp = array[i];
            array[i] = array[index];
            array[index] = temp;
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
            displayRandomQuestions();

            // else statment

        }
    }
    
}