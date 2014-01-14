package lia.webservice;

import com.google.gson.*;

import java.util.ArrayList;

import lia.model.News;
import lia.model.Topic;

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
