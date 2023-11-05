package edu.uga.cs.statecapitalsquiz;


/**
 * This class (a POJO) represents a single job lead, including the id, company name,
 * capitalCity number, URL, and some thirdCity.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Question {

    private long   id;
    private String stateName;
    private String capitalCity;
    private String secondCity;
    private String thirdCity;

    public Question()
    {
        this.id = -1;
        this.stateName = null;
        this.capitalCity = null;
        this.secondCity = null;
        this.thirdCity = null;
    }

    public Question( String stateName, String capitalCity, String secondCity, String thirdCity ) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.stateName = stateName;
        this.capitalCity = capitalCity;
        this.secondCity = secondCity;
        this.thirdCity = thirdCity;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

    public String getCapitalCity()
    {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity)
    {
        this.capitalCity = capitalCity;
    }

    public String getSecondCity()
    {
        return secondCity;
    }

    public void setSecondCity(String secondCity)
    {
        this.secondCity = secondCity;
    }

    public String getThirdCity()
    {
        return thirdCity;
    }

    public void setThirdCity(String thirdCity)
    {
        this.thirdCity = thirdCity;
    }

    public String toString()
    {
        return id + ": " + stateName + " " + capitalCity + " " + secondCity + " " + thirdCity;
    }
}
