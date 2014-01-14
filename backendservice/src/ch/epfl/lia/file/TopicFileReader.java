package ch.epfl.lia.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Scanner;

import ch.epfl.lia.others.CommonMethods;

public class TopicFileReader {
	String filePath;
	File topicFile;
	Scanner sc;
	
	public TopicFileReader(String filePath){
		this.filePath=filePath;
		this.topicFile=new File(filePath);
	}
	
	public void open() throws FileNotFoundException{
		this.sc=new Scanner(topicFile);
	}
	 
	public String nextTopic(){
		 
		if(sc.hasNext()){
			sc.nextInt();
			sc.nextDouble();
			return sc.nextLine();
		}else{
			return null;
		}
		
	}
	
	public boolean hasNext(){
		return sc.hasNext();
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
	
	
	public Date getDate(){
		return TopicFileReader.getDateFromFilePath(this.filePath);
	 
	}
	
	public void close(){
		sc.close();
	}
	
	public static Date getDateFromFilePath(String filePath){
		String[] filePaths=filePath.split("/");
		String fileName=filePaths[filePaths.length-1];
		String timeStamp=fileName.split("_")[1]+" "+fileName.split("_")[2];
		return CommonMethods.getDate(timeStamp);
	}
	
	
}
