package lia.model;

import java.util.ArrayList;

/**
 * Created by xtang on 13-10-8.
 */
public class Topic {
    public int topic_id;
    public News positiveNews;
    public News negativeNews;
    public double positivePercentage;
    public ArrayList<Integer> newsList;
    public ArrayList<String> tags;

    public boolean isValid(){
        if(topic_id==0)
            return false;
        else if(positiveNews==null)
            return false;
        else if(newsList==null)
            return false;

        return true;
    }

    public boolean isPositive(){
        if(positivePercentage<0.5)
            return false;
        else
            return true;
    }

    public int getPositivePercentage(){

        return (int) (positivePercentage * 100);
    }

    public News getTopNews(){
        if(isPositive()){
            return this.positiveNews;
        }else{
            return this.negativeNews;
        }
    }
}


