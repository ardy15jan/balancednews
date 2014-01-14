package ch.epfl.lia.model;

import android.os.Handler;
import android.util.Log;

import java.util.Date;

import ch.epfl.lia.webservice.NewsService;

/**
 * Created by xtang on 13-11-28.
 */
public class Record {

    public static final String APP_TYPE ="APP";
    public static final String TOPIC_TYPE = "TOPIC";
    public static final String NEWS_TYPE = "NEWS";

    private String uuid;
    private String type;
    private int duration;
    private int news_id;
    private int topic_id;
    private Date created_at;

    public Record(String uuid, String type, Date created_at){
        this.uuid = uuid;
        this.type = type;
        this.created_at = created_at;
        this.news_id = -1;
        this.topic_id = -1;
    }

    public void setNewsID(int news_id){
        this.news_id = news_id;
    }

    public void setTopicID(int topic_id){
        this.topic_id = topic_id;
    }

    public void setDuration(Date stopTime){
        long diff = stopTime.getTime() - created_at.getTime();
        long secondsInMilli = 1000;
        int seconds = (int) (diff/secondsInMilli);
        this.duration = seconds;
    }

    public void sendRecord(Handler handler){
        //Log.d("test", "send "+type+" "+news_id);
        if(duration > 2)
            new NewsService(handler).insertRecord(uuid, type, duration, news_id, topic_id, created_at);
    }
}
