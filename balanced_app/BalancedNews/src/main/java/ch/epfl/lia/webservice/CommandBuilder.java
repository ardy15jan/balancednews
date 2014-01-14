package ch.epfl.lia.webservice;

import android.util.Log;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;



import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.model.Answer;

/**
 * Created by xtang on 13-10-8.
 */
public class CommandBuilder {
    public final static String GET_TOPIC_IDS_STR="Get_Topic_IDs";
    public final static String GET_TOPIC_STR="Get_Topic";
    public final static String GET_NEWS_STR="Get_News";
    public final static String GET_OPINION_STR="Get_Opinion";
    public final static String GET_NEWS_COMPLETE_STR="Get_News_Complete";
    public final static String INSERT_RECORD="Insert_Record";
    public final static String SEND_ANSWER="Send_Answer";

    public static String createGetTopicIDs(String category){
        return "cmd="+GET_TOPIC_IDS_STR+"&arg="+category+"&user_id="+ Constants.USER_ID+"&version="+ Constants.VERSION;
    }

    public static String createGetTopic(int topicID){
        return "cmd="+GET_TOPIC_STR+"&arg="+topicID;
    }

    public static String createGetNews(int topicID, int newsID){
        return "cmd="+GET_NEWS_STR+"&topic_id="+topicID+"&news_id="+newsID;
    }

    public static String createGetOpinion(int topicID, int newsID){

        return "cmd="+GET_OPINION_STR+"&topic_id="+topicID+"&news_id="+newsID;
    }

    public static String createGetNewsComplete(int topicID, int newsID){


        return "cmd="+GET_NEWS_COMPLETE_STR+"&topic_id="+topicID+"&news_id="+newsID;
    }

    public static String createInsertRecord(String uuid, String type, int duration, int news_id, int topic_id, Date created_at){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return "cmd="+INSERT_RECORD+"&uuid="+uuid+"&type="+type+"&duration="+duration+"&news_id="+news_id+"&topic_id="+topic_id+"&created_at="+sdf.format(created_at)+"&version="+Constants.VERSION;
    }

    public static String createSendAnswer(String uuid, int topic_id, int understanding, String description, Date created_at){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Answer answer = new Answer(understanding, description, topic_id);
        String answerStr = toJson(answer);
        try {
            answerStr = URLEncoder.encode(answerStr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d("test",e.toString());
        }
        return "cmd="+SEND_ANSWER+"&uuid="+uuid+"&topic_id="+topic_id+"&created_at="+sdf.format(created_at)+"&answer="+answerStr+"&version="+Constants.VERSION;
    }

    public static String toJson(Object obj){
        Gson gson=new Gson();
        return gson.toJson(obj);
    }

}
