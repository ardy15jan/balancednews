package ch.epfl.lia.model;

import java.util.ArrayList;

/**
 * Created by xtang on 13-10-8.
 */
public class News {
    public String title;
    public String content;
    public int opinionValue;
    public int id;
    public String url;
    public ArrayList<OpinionPhrase> phrases;

    public boolean isValid(){
        if(title==null||title.equals(""))
            return false;
        if(opinionValue==0)
            return false;

        return true;
    }

    public static class OpinionPhrase{
        public String phrase;
        public boolean isPositive;
        public String sentence;
        public int index;
    }
}
