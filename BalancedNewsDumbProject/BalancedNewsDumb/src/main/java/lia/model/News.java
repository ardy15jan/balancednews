package lia.model;

/**
 * Created by xtang on 13-10-8.
 */
public class News {
    public String title;
    public String content;
    public int opinionValue;
    public int id;

    public boolean isValid(){
        if(title==null||title.equals(""))
            return false;
        if(opinionValue==0)
            return false;

        return true;
    }
}
