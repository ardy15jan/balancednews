package lia.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

import lia.constants.Constants;

/**
 * Created by xtang on 13-10-13.
 */
public class WebService extends Thread{

    private String command;
    private int commandType;
    private NewsService newsService;

    public WebService(String command, int commandType, NewsService newsService){
        this.newsService=newsService;
        this.command=command;
        this.commandType=commandType;
    }


    @Override
    public void run(){
        try{
            //Log.d("WebServiceException", command);
            String url= Constants.URL+command;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String output = EntityUtils.toString(httpEntity);

            if(newsService.checkValid(output)){
                newsService.notifyHandler(output);
            }else{
                if(newsService.isRetry())
                    new WebService(command,commandType,newsService).start();
            }



        }catch(Exception e){
            if(newsService.isRetry()){
                new WebService(command,commandType,newsService).start();
                Log.d("WebServiceException","retry: "+command+" "+commandType);
            }


            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            Log.d("WebServiceException",sw.toString());
        }

    }
}
