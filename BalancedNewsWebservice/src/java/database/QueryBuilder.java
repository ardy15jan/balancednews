/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import common.BalancedNewsConstants;
import common.CommonMethods;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xtang
 */
public class QueryBuilder {
    public static String findTopicsByUUID(String uuid, String category, String today, String tomorrow, int version){
        if(version == BalancedNewsConstants.VERSION_BALANCED)
            return "SELECT topics1 AS topics FROM users WHERE uuid = '"+uuid+"' AND category = '"+category+"' AND created_at>= '"+today+"' AND created_at<='"+tomorrow+"'"; 
        else
            return "SELECT topics2 AS topics FROM users WHERE uuid = '"+uuid+"' AND category = '"+category+"' AND created_at>= '"+today+"' AND created_at<='"+tomorrow+"'"; 
    }
    
    public static String insertIntoUsers(String uuid, String category, List<Integer> topicsList1, List<Integer> topicsList2){
        String topics1 = "";
        String topics2 = "";
        
        if(topicsList1.size()>0)
            topics1 += topicsList1.get(0);
        for(int i=1; i<topicsList1.size(); i++){
            topics1 += ","+topicsList1.get(i);
        }
        
        if(topicsList2.size()>0)
            topics2 += topicsList2.get(0);
        for(int i=1; i<topicsList2.size(); i++){
            topics2 += ","+topicsList2.get(i);
        }
        
        return "INSERT INTO `users` (`uuid`, `topics1`, `topics2`, `category`, `created_at`) VALUES ('"+uuid+"', '"+topics1+"', '"+topics2+"', '"+category+"', '"+CommonMethods.getCurrentStr()+"')";
    }
    
    public static String getNumberOfPositiveNews(int topicID){
        return "SELECT COUNT(news_id) AS num FROM  news_opinion WHERE topic_id="+topicID+" AND opinion_value>0";
    }
    
    public static String getNumberOfNegativeNews(int topicID){
        return "SELECT COUNT(news_id) AS num FROM news_opinion WHERE topic_id="+topicID+" AND opinion_value<0";
    }
    
    public static String insertRecord(String uuid, String type, int duration, int news_id, int topic_id, Date created_at, int version){
        return "INSERT INTO `tom`.`records` (`uuid`, `type`, `duration`, `news_id`, `topic_id`, `created_at`, `version`) VALUES ('"+uuid+"', '"+type+"', '"+duration+"', '"+news_id+"', '"+topic_id+"', '"+CommonMethods.dateToString(created_at)+"', '"+version+"')";
    }
    
    public static String insertAnswer(String uuid, int topic_id, int understanding, String description, Date created_at, int version){
        return "INSERT INTO `tom`.`answer` (`uuid`, `topic_id`, `understanding`, `description`, `created_at`, `version`) VALUES ('"+uuid+"', '"+topic_id+"', '"+understanding+"', '"+description+"', '"+CommonMethods.dateToString(created_at)+"','"+version+"')";
        
    }
}
