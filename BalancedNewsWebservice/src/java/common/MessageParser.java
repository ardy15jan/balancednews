package ch.epfl.lia.webservice;

import java.util.ArrayList;



import com.google.gson.*;
import model.News;
import model.Topic;

/**
 * Created by xtang on 13-10-8.
 */
public class MessageParser {
    public static Gson gson=new Gson();

    public static ArrayList<Double> toTopicIDs(String message){
        return gson.fromJson(message, ArrayList.class);
    }

    public static Topic toTopic(String message){
        return gson.fromJson(message, Topic.class);
    }

    public static News toNews(String message){
        return gson.fromJson(message,News.class);
    }

    public static int toOpinion(String message){
        return Integer.parseInt(message);
    }
}
