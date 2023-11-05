package edu.uga.cs.statecapitalsquiz;

public class Relationship {
    private long id;
    private long questionId;
    private long quizId;
    private String userAnswer;

    public Relationship()
    {
        this.id = -1;
        this.questionId = -1;
        this.quizId = -1;
        this.userAnswer = null;
    }

    public Relationship( long questionId, long quizId, String userAnswer ) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.questionId = questionId;
        this.quizId = quizId;
        this.userAnswer = userAnswer;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(long questionId)
    {
        this.questionId = questionId;
    }

    public long getQuizId()
    {
        return quizId;
    }

    public void setQuizId(long quizId)
    {
        this.quizId = quizId;
    }

    public String getUserAnswer()
    {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer)
    {
        this.userAnswer = userAnswer;
    }



    public String toString()
    {
        return id + ": " + questionId + " " + quizId + " " + userAnswer;
    }
}
