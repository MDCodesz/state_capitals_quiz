package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all the past quizzes.
 */
public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.QuizHolder> implements Filterable {
    public static final String DEBUG_TAG = "QuizRecyclerAdapter";
    private final Context context;
    private List<Quiz> values;
    private List<Quiz> originalValues;

    public QuizRecyclerAdapter (Context context, List<Quiz> quizList) {
        this.context = context;
        this.values = quizList;
        this.originalValues = new ArrayList<Quiz>( quizList );
    }

    // reset the originalValues to the current contents of values
    public void sync()
    {
        originalValues = new ArrayList<Quiz>( values );
    }
    // The adapter must have a ViewHolder class to "hold" one item to show.
    public static class QuizHolder extends RecyclerView.ViewHolder {
        TextView quizDate;
        TextView quizTime;
        TextView quizScore;
        public QuizHolder( View itemView ) {
            super( itemView );

            quizDate = itemView.findViewById( R.id.quizDate );
            quizTime = itemView.findViewById( R.id.quizTime );
            quizScore = itemView.findViewById( R.id.quizScore );
        }
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.quiz_card, parent, false );
        return new QuizHolder( view );
    }

    /**
     * Method to filter the list of past quizzes
     * @return
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Quiz> list = new ArrayList<Quiz>( originalValues );
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0) {
                    filterResults.count = list.size();
                    filterResults.values = list;
                }
                else{
                    List<Quiz> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for( Quiz quiz : list ) {
                        // check if either the date contain the search string
                        if( quiz.getQuizDate().toLowerCase().contains( searchStr ) ) {
                            resultsModel.add( quiz );
                        }
/*
                        // this may be a faster approach with a long list of items to search
                        if( jobLead.getCompanyName().regionMatches( true, i, searchStr, 0, length ) )
                            return true;

 */
                    }

                    filterResults.count = resultsModel.size();
                    filterResults.values = resultsModel;
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                values = (ArrayList<Quiz>) results.values;
                notifyDataSetChanged();
                if( values.size() == 0 ) {
                    Toast.makeText( context, "Not Found", Toast.LENGTH_LONG).show();
                }
            }
        };
        return filter;
    }


    /**
     * This method fills in the values of a holder to show a past quiz.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        Quiz quiz = values.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + quiz );

        holder.quizDate.setText(quiz.getQuizDate());
        holder.quizTime.setText(quiz.getQuizTime() );
        holder.quizScore.setText(quiz.getResult() );
    }

    @Override
    public int getItemCount() {
        if( values != null )
            return values.size();
        else
            return 0;
    }


}
