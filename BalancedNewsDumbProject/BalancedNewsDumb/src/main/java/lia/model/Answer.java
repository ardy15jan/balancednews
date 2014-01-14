package lia.model;

/**
 * Created by xtang on 13-12-10.
 */
public class Answer {
    public int understanding;
    public String description;
    public int topic_id;

    public Answer(int understanding, String description, int topicID){
        this.understanding = understanding;
        this.description = description;
        this.topic_id = topicID;
    }

    @Override
    public int hashCode() {
        return this.topic_id;
    }

    @Override
    public boolean equals(Object arg){
        Answer obj = (Answer) arg;
        if(obj.topic_id == this.topic_id)
            return true;
        else
            return false;
    }
}
