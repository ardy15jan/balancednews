package database;


import com.google.gson.Gson;
import common.BalancedNewsConstants;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;


 


import common.CommonMethods;
import common.OpinionFinder;
import common.TextProcessor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.News;
import model.News.OpinionPhrase;
import model.Topic;
import webservice.RequestHandler;

public class DatabaseManager extends DatabaseReader{

    public DatabaseManager() {
            super("jdbc:mysql://localhost/tom?user=root&password=1d2r3m4f5s");
            // TODO Auto-generated constructor stub
    }

    public String getTopics(String category, String uuid, int version) throws Exception{
        
        Statement stmt=null;
        String query = null;
        ArrayList<Integer> topicList=new ArrayList<Integer>();
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query = QueryBuilder.findTopicsByUUID(uuid, category, CommonMethods.dateToString(CommonMethods.getToday()), CommonMethods.dateToString(CommonMethods.getTomorrow()), version);
            ResultSet rs = stmt.executeQuery(query);
            
            
            if(rs.next() && !rs.getString("topics").equals(" ")){
                String[] topicIDs = rs.getString("topics").split(",");
                for(String topicID: topicIDs){
                    topicList.add(Integer.parseInt(topicID));
                }
            }else{
                rs.close();
                stmt.close();
                ArrayList<List<Integer>> randomTopics = selectTopicRandomly(category, BalancedNewsConstants.DefaultToday, BalancedNewsConstants.DefaultTomorrow);
                query = QueryBuilder.insertIntoUsers(uuid, category, randomTopics.get(0), randomTopics.get(1));
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                if(version == BalancedNewsConstants.VERSION_BALANCED){
                    for(Integer topicID: randomTopics.get(0)){
                        
                        topicList.add(topicID);
                    }
                }else{
                    for(Integer topicID: randomTopics.get(1)){
                        topicList.add(topicID);
                    }
                }

            }
            
            return CommonMethods.toJson(topicList);
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Topic getTopicByID(int topicID){
        Topic topic=new Topic();
        topic.topic_id=topicID;
        topic.newsList=getNewsByTopic(topicID);
        topic.positivePercentage=getPositivePencentage(topicID);
        topic.tags = getTopicTags(topicID);
        if(topic.newsList!=null){
            topic.positiveNews = getNewsByID(topic.newsList.get(0));
            topic.negativeNews = getNewsByID(topic.newsList.get(1));
        }
        return topic;
    }
        
    private ArrayList<Integer> getNewsByTopic(int topicID){
        Statement stmt1=null;
        Statement stmt2=null;
        String query = null;
        ArrayList<Integer> result=new ArrayList<Integer>();
        HashMap<Integer, Integer> feedIDs = new HashMap<Integer, Integer>();
        Map<String, Integer> titles = new HashMap<String, Integer>();
        
        try{
            this.connect();
            stmt1 = conn.createStatement();
            stmt2 = conn.createStatement();
            query="SELECT news_id FROM news_opinion WHERE topic_id="+topicID+" AND opinion_value>0  ORDER BY relevance DESC limit 0,100";
            ResultSet rsPositive=stmt1.executeQuery(query);
            query="SELECT news_id FROM news_opinion WHERE topic_id="+topicID+" AND opinion_value<0  ORDER BY relevance DESC limit 0,100";
            ResultSet rsNegative=stmt2.executeQuery(query);
           
            
           
            while(!rsPositive.isLast() && !rsNegative.isLast()){
                rsPositive.next();
                rsNegative.next();
                int newsIDPositive = rsPositive.getInt("news_id");
                int newsIDNegative = rsNegative.getInt("news_id");
                
                if(isValidNewsCandidate(newsIDPositive, feedIDs, titles)){
                     result.add(newsIDPositive);
                     if(result.size() == BalancedNewsConstants.NUMBER_OF_NEWS)
                            return result;
                }
                
                if(isValidNewsCandidate(newsIDNegative, feedIDs, titles)){
                     result.add(newsIDNegative);
                     if(result.size() == BalancedNewsConstants.NUMBER_OF_NEWS)
                            return result;
                }
            }
            
            while(!rsPositive.isLast()){
                rsPositive.next();
                int newsIDPositive = rsPositive.getInt("news_id");
                if(isValidNewsCandidate(newsIDPositive, feedIDs, titles)){
                     result.add(newsIDPositive);
                     if(result.size() == BalancedNewsConstants.NUMBER_OF_NEWS)
                            return result;
                }
            }
            
            while(!rsNegative.isLast()){
                rsNegative.next();
                int newsIDNegative = rsNegative.getInt("news_id");
                if(isValidNewsCandidate(newsIDNegative, feedIDs, titles)){
                     result.add(newsIDNegative);
                     if(result.size() == BalancedNewsConstants.NUMBER_OF_NEWS)
                            return result;
                }
            }
           
            
             
            return result;
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    private boolean isValidNewsCandidate(int newsID, HashMap<Integer, Integer> feedIDs, Map<String, Integer> titles){
        int feedID = getFeedID(newsID);
        String title = getNewsTitle(newsID);
        
        if(titles.containsKey(titles))
            return false;
        else{
            titles.put(title, newsID);
        }
        
        if(feedIDs.containsKey(feedID)){
            if(feedIDs.get(feedID) >= BalancedNewsConstants.NUMBER_OF_NEWS_FROM_SAME_SOURCE){
                return false;
            }else{
                feedIDs.put(feedID, feedIDs.get(feedID)+1);
                return true;
            }
        }else{
            feedIDs.put(feedID, 1);
            return true;
        }
    }
    
    
    private int getFeedID(int newsID){
        Statement stmt=null;
        String query = null;
       
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT feed_id FROM articles WHERE id="+newsID;
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            return rs.getInt("feed_id");
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return Integer.MIN_VALUE;
        }
        
    }
    
    
    
    public News getNewsByID(int newsID){
        Statement stmt=null;
        String query = null;
        News news = new News();
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT * FROM articles WHERE id="+newsID;
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            news.title=rs.getString("title").replace("’", "'").trim();
            
            news.content=rs.getString("content").replace("’", "'");
            news.content=TextProcessor.processContent(news.content, news.title);
            
            //news.content = rs.getString("html");
            news.opinionValue= 0;
            news.id=newsID;
            news.url = rs.getString("url");
             
            
            //this.close();
            return news;
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    private String getNewsTitle(int newsID){
        Statement stmt=null;
        String query = null;
        News news = new News();
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT title FROM articles WHERE id="+newsID;
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            return rs.getString("title").replace("’", "'").trim();
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public int getNewsOpinion(int topicID, int newsID){
        Statement stmt=null;
        String query = null;
        int result;
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT opinion_value FROM news_opinion WHERE news_id="+newsID+" AND topic_id="+topicID;
            //System.out.println(query);
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            result=rs.getInt("opinion_value");
            //this.close();
            return result;
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }
    
    public ArrayList<OpinionPhrase> getOpinionPhrases(int topicID, int newsID){
        Statement stmt=null;
        String query = null;
        String phrases;
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT phrase FROM news_opinion WHERE news_id="+newsID+" AND topic_id="+topicID;
            //System.out.println(query);
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            phrases=rs.getString("phrase");
            //this.close();
            return OpinionFinder.getPhrases(phrases);
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
     public News getNews(int topicID, int newsID){
        int opinion=getNewsOpinion(topicID, newsID);
        News news=getNewsByID(newsID);
        news.content=TextProcessor.getFirstNPara(news.content,4);
        news.opinionValue=opinion;
        return news;
    }
    
    public News getNewsComplete(int topicID, int newsID){
        int opinion=getNewsOpinion(topicID, newsID);
        News news=getNewsByID(newsID);
        news.content = TextProcessor.toHTML(news.content);
        news.opinionValue=opinion;
        news.phrases = getOpinionPhrases(topicID, newsID);
        
        return news;
    }
    
    public double getPositivePencentage(int topicID){
        Statement stmt=null;
        String query = null;
               
        try{
            this.connect();
            stmt=conn.createStatement();
            
            query = QueryBuilder.getNumberOfPositiveNews(topicID);
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            Double positiveNum = (double) rs.getInt("num");
            rs.close();
            stmt.close();
            
            query = QueryBuilder.getNumberOfNegativeNews(topicID);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            Double negativeNum = (double) rs.getInt("num");
            rs.close();
            stmt.close();
            
            return positiveNum/(positiveNum + negativeNum);
             
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return 0.0;
        }
        
    }
    
    public boolean insertRecord(String uuid, String type, int duration, int news_id, int topic_id, Date created_at, int version){
        Statement stmt=null;
        String query = null;
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query=QueryBuilder.insertRecord(uuid, type, duration, news_id, topic_id, created_at, version);
            stmt.executeUpdate(query);
            stmt.close();
            return true;
            //this.close();
             
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return false; 
        }
    }
    
    public boolean insertAnswer(String uuid, int topic_id, int understanding, String description, Date created_at, int version){
        Statement stmt=null;
        String query = null;
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query=QueryBuilder.insertAnswer(uuid, topic_id, understanding, description, created_at, version);
            stmt.executeUpdate(query);
            stmt.close();
            return true;
             
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return false; 
        }
    
    }
    
    private ArrayList<String> getTopicTags(int topicID){
        Statement stmt=null;
        String query = null;
        ArrayList<String> result=new ArrayList<String>();
        
        try{
            this.connect();
            stmt=conn.createStatement();
            query="SELECT topic_keys FROM topic WHERE topic_id="+topicID;
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            String keyString = rs.getString("topic_keys").trim();
            String[] keys = keyString.split(" ");
            Collections.addAll(result, keys);
            //this.close();
            return result;
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    private ArrayList<List<Integer>> selectTopicRandomly(String category, String from, String to){
        Statement stmt=null;
        String query = null;

        ArrayList<Integer> topicList=new ArrayList<Integer>();
        ArrayList<List<Integer>> selectedLists = null;
        try{
            
            this.connect();
            stmt=conn.createStatement();
            query="SELECT topic_id FROM topic WHERE category='"+category+"' AND created_at>='"+from+"' AND created_at<='"+to+"'";
            ResultSet rs=stmt.executeQuery(query);
            //System.out.println(query);
            while(rs.next()){
                topicList.add(rs.getInt("topic_id"));
            }

            rs.close();
            stmt.close();
            //this.close();
            Collections.shuffle(topicList);
            int size = topicList.size();
            selectedLists = new ArrayList<List<Integer>>();
            selectedLists.add(topicList.subList(0, size/2));
            selectedLists.add(topicList.subList(size/2, size));
            
        }catch(Exception e){
            System.err.println(query);
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
         return selectedLists;
    }
    
    
}
