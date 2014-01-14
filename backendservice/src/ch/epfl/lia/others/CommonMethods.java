package ch.epfl.lia.others;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethods {
	
	public static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";

	public static String getDate(){
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	
	public static String getMD5(String text){
		try{
			MessageDigest m=MessageDigest.getInstance("MD5");
			m.update(text.getBytes("UTF-8"),0,text.length());
			BigInteger i=new BigInteger(1,m.digest());
			return String.format("%1$032x", i);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isDateValid(String date) 
	{
	        try {
	            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	            df.setLenient(false);
	            df.parse(date);
	            return true;
	        } catch (ParseException e) {
	            return false;
	        }
	}
	
	public static Date getDate(String timestamp){
		try {
			 DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	        df.setLenient(false);
	        
			 return df.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getRidOfResourceName(String title){
		String[] array=title.split(" - ");
	    return array[0];
	}
	
	 
}
