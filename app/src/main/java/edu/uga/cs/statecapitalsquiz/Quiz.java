package edu.uga.cs.statecapitalsquiz;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Quiz {
    private long   id;
    private String quizDate;
    private String quizTime;
    private Integer quizResult; // int?

    public Quiz()
    {
        this.id = -1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Define the date format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Define the time format
        this.quizDate = dateFormat.format(new Date()); // Set the current date in the desired format
        this.quizTime = timeFormat.format(new Date()); // Set the current time in the desired format
        this.quizResult = null;
    }

    public Quiz( String quizDate, String quizTime, Integer quizResult ) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.quizDate = quizDate;
        this.quizTime = quizTime;
        this.quizResult = quizResult;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getQuizDate()
    {
        return quizDate;
    }

    public void setQuizDate(String quizDate)
    {
        this.quizDate = quizDate;
    }

    public String getQuizTime()
    {
        return quizTime;
    }

    public void setQuizTime(String quizTime)
    {
        this.quizTime = quizTime;
    }

    public Integer getResult()
    {
        return quizResult;
    }

    public void setResult(Integer quizResult)
    {
        this.quizResult = quizResult;
    }



    public String toString()
    {
        return id + ": " + quizDate + " " + quizTime + " " + quizResult;
    }
}
