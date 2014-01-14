package common;

import com.google.gson.Gson;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import model.Answer;



public class CommonMethods {

        public static String dateToString(Date date){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date); 
        }
        
        public static Date stringToDate(String dateStr){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
            Date date;
            try {
                date = df.parse(dateStr);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
	
         public static String toJson(Object obj){
                Gson gson=new Gson();
                return gson.toJson(obj);
         }
         
         public static Date getToday(){
             Calendar exactToday = new GregorianCalendar();
             Calendar today = new GregorianCalendar(exactToday.get(Calendar.YEAR), exactToday.get(Calendar.MONTH), exactToday.get(Calendar.DAY_OF_MONTH));
             return today.getTime();
         }
         
         public static Date getCurrent(){
              Calendar current = new GregorianCalendar();
              return current.getTime();
         }
         
         public static Date getTomorrow(){
             Calendar exactToday = new GregorianCalendar();
             Calendar tomorrow = new GregorianCalendar(exactToday.get(Calendar.YEAR), exactToday.get(Calendar.MONTH), exactToday.get(Calendar.DAY_OF_MONTH));
             tomorrow.add(Calendar.DAY_OF_MONTH, 1);
             return tomorrow.getTime();
         }
         
         public static String getTodayStr(){
             return dateToString(getToday());
         }
	 
         public static String getTomorrowStr(){
             return dateToString(getTomorrow());
         }
         
         public static String getCurrentStr(){
             return dateToString(getCurrent());
         }
         
         public static Answer toAnswer(String message){
             Gson gson = new Gson();
             return gson.fromJson(message, Answer.class);
    
         }
         
          
}
