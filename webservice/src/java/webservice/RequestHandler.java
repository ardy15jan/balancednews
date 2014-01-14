package webservice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.CommonMethods;
import database.DatabaseManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import model.Answer;

public class RequestHandler {

    private DatabaseManager dbManager=new DatabaseManager();

    
    public String getResponse(String cmd, HttpServletRequest request)  {
            
            if(cmd.equals("Get_Topic_IDs")){
                System.out.println("test1");
                return getTopics(request.getParameter("arg"), request.getParameter("user_id"), Integer.parseInt(request.getParameter("version")));
            }else if(cmd.equals("Get_Topic")){
                return getTopicByID(Integer.parseInt(request.getParameter("arg")));
            }else if(cmd.equals("Get_News")){
                int topicID=Integer.parseInt(request.getParameter("topic_id"));
                int newsID=Integer.parseInt(request.getParameter("news_id"));
                return getNews(topicID, newsID);
            }else if(cmd.equals("Get_Opinion")){
                 int topicID=Integer.parseInt(request.getParameter("topic_id"));
                 int newsID=Integer.parseInt(request.getParameter("news_id"));
                 return getNewsOpinion(topicID,newsID);
            }else if(cmd.equals("Get_News_Complete")){
                 int topicID=Integer.parseInt(request.getParameter("topic_id"));
                 int newsID=Integer.parseInt(request.getParameter("news_id"));
                 return getNewsComplete(topicID,newsID);
            }else if(cmd.equals("Insert_Record")){
                //(String uuid, String type, int duration, int news_id, int topic_id, Date created_at)
                 
                String uuid = request.getParameter("uuid");
                String type = request.getParameter("type");
                int version = Integer.parseInt(request.getParameter("version"));
                int duration = Integer.parseInt(request.getParameter("duration"));
                int news_id = Integer.parseInt(request.getParameter("news_id"));
                int topic_id = Integer.parseInt(request.getParameter("topic_id"));
                Date created_at = CommonMethods.stringToDate(request.getParameter("created_at"));
                 
                return insertRecord(uuid, type, duration, news_id, topic_id, created_at, version);
            }else if(cmd.equals("Send_Answer")){
                
                String uuid = request.getParameter("uuid");
               
                Date created_at = CommonMethods.stringToDate(request.getParameter("created_at"));
                Answer answer = CommonMethods.toAnswer(request.getParameter("answer"));
                int understanding = answer.understanding;
                String description = answer.description;
                int topic_id = answer.topic_id;
               int version = Integer.parseInt(request.getParameter("version"));
                
                return insertAnswer(uuid, topic_id, understanding, description, created_at, version);
            }
            
            
            return null;
    }
	
    public String getTopics(String category, String userID, int version) {
        try {
            dbManager.connect();
            String result=dbManager.getTopics(category, userID, version);
            dbManager.close();
            return result;
        } catch (Exception ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String getTopicByID(int id){
        return CommonMethods.toJson(dbManager.getTopicByID(id));
    }
    
    public String getNewsByID(int id){
        return CommonMethods.toJson(dbManager.getNewsByID(id));
    }
    
    public String getNews(int topicID, int newsID){
        return CommonMethods.toJson(dbManager.getNews(topicID, newsID));
    }
    
    public String getNewsOpinion(int topicID, int newsID){
        return CommonMethods.toJson(dbManager.getNewsOpinion(topicID, newsID));
    }
    
    public String getNewsComplete(int topicID, int newsID){
        return CommonMethods.toJson(dbManager.getNewsComplete(topicID, newsID));
    }
    
    public String insertRecord(String uuid, String type, int duration, int news_id, int topic_id, Date created_at, int version){
        if(dbManager.insertRecord(uuid, type, duration, news_id, topic_id, created_at,version))
            return "success";
        else
            return "fail";
    }
    
    public String insertAnswer(String uuid, int topic_id, int understanding, String description, Date created_at, int version ){
        if(dbManager.insertAnswer(uuid, topic_id, understanding, description, created_at, version))
            return "success";
        else
            return "fail";
    }
}
