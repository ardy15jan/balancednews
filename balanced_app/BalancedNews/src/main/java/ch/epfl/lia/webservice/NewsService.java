package ch.epfl.lia.webservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Date;

import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.model.Topic;

/**
 * Created by xtang on 13-10-13.
 */
public class NewsService extends Thread{
    private Handler handler;
    private String command;
    private int commandType;
    public int counter=0;


    public NewsService(Handler handler){
        this.handler=handler;
    }

    public NewsService(){

    }

    public void getTopic(int topicID){
        command=CommandBuilder.createGetTopic(topicID);
        commandType= Constants.GET_TOPIC;
        this.start();
    }

    public void getTopicIDs(String category){
        command=CommandBuilder.createGetTopicIDs(Constants.DEFAULT_CATEGORY);
        commandType=Constants.GET_TOPIC_IDS;
        this.start();
    }

    public void getNews(int topicID, int newsID){
        command=CommandBuilder.createGetNews(topicID, newsID);
        commandType=Constants.GET_NEWS;
        this.start();
    }

    public void getCompleteNews(int topicID, int newsID){
        command=CommandBuilder.createGetNewsComplete(topicID, newsID);
        commandType=Constants.GET_NEWS_COMPLETE;
        this.start();
    }

    public void insertRecord(String uuid, String type, int duration, int news_id, int topic_id, Date created_at){
        command = CommandBuilder.createInsertRecord(uuid, type, duration, news_id, topic_id, created_at);
        commandType=Constants.INSERT_RECORD;

        this.start();
    }

    public void sendAnswer(String uuid, int topic_id, int understanding, String description, Date created_at){
        command = CommandBuilder.createSendAnswer(uuid, topic_id, understanding, description, created_at);
        commandType = Constants.SEND_ANSWER;
        this.start();
    }

    public void run(){
        new WebService(command,commandType,this).start();
    }

    public void notifyHandler(String info){

        if(handler == null)
            return;

        Message msg=new Message();
        msg.what=commandType;
        msg.obj=info;

        if(commandType != Constants.INSERT_RECORD)
            handler.sendMessage(msg);
    }

    public boolean checkValid(String info){
        switch (commandType){
            case Constants.GET_TOPIC_IDS:
                if(MessageParser.toTopicIDs(info)!=null)
                    return true;
                else
                    return false;
            case Constants.GET_TOPIC:
                return MessageParser.toTopic(info).isValid();
            case Constants.GET_NEWS:
                if(MessageParser.toNews(info).isValid())
                    return true;
                else
                    return false;
            case Constants.GET_NEWS_COMPLETE:
                if(MessageParser.toNews(info).isValid())
                    return true;
                else
                    return false;
            case Constants.INSERT_RECORD:
                if(info.equals("success"))
                    return true;
                else
                    return false;
            case Constants.SEND_ANSWER:
                if(info.equals("success"))
                    return true;
                else
                    return false;
            default:
                return false;

        }
    }

    public boolean isRetry(){

        return counter++<Constants.RETRY_NUMBER;
    }

}
