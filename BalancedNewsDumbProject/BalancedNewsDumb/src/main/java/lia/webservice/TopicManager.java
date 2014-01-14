package lia.webservice;

import android.os.Handler;

import java.util.ArrayList;

import lia.model.News;

/**
 * Created by xtang on 13-10-14.
 */
public class TopicManager {

    private int topicID;
    private ArrayList<News> newsList=new ArrayList<News>();
    private Handler handler;

    public TopicManager(Handler handler, int topicID){
        this.handler=handler;
        this.topicID=topicID;
    }

    public void updateNewsList(){

    }

}
