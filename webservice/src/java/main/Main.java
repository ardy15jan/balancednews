/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ch.epfl.lia.webservice.MessageParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import common.CommonMethods;
import database.DatabaseManager;
import java.util.ArrayList;
import java.util.Date;
import model.News;
import model.Topic;
import webservice.RequestHandler;
 
 

/**
 *
 * @author xtang
 */
public class Main {
    public static void main(String args[]){
       /*
       RequestHandler handler=new RequestHandler();
       DatabaseManager manager=new DatabaseManager();

     String msg= handler.getResponse("Get_News", "233");
       News news=MessageParser.toNews(msg);
     
       String[] array=news.title.split(" - ");
       System.out.println(array[array.length-1]);
       if(array[array.length-1].equals("Bloomberg\n"))
           System.out.println("yes");
       
       String test="sdsd\nsd";
       System.out.println(test.substring(test.indexOf("\n")+1, test.length()));
       * */
        
         
    }
}
