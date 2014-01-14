package ch.epfl.lia.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ch.epfl.lia.datamodel.NewsOpinion;
import ch.epfl.lia.file.OpinionFileReader;
import ch.epfl.lia.file.TopicFileReader;
import ch.epfl.lia.main.CommonCommands;
import ch.epfl.lia.others.CommonMethods;
import ch.epfl.lia.others.Constants;
import ch.epfl.lia.webpage.WebPageRecord;

public class LocalConnector extends DatabaseReader{

	public LocalConnector() {
		super("jdbc:mysql://localhost/tom?user=root&password=1d2r3m4f5s");
		// TODO Auto-generated constructor stub
	}
	
	public LocalConnector(String connectionStr){
		super(connectionStr);
	}
	
	public void copy(WebPageRecord page){
		Statement stmt=null;
		String query;
		
		page.title=page.title.replace("'", "\\'");
		page.url=page.url.replace("'", "\\'");
		
		try{
			query="SELECT COUNT(*) AS num FROM  articles WHERE id='"+page.id+"'";
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			 
			int num=rs.getInt("num");
			rs.close();
			//stmt.close();
			
			if(num==0){
				String value;
				value="(";
				value+="'"+page.id+"',";
				value+="'"+page.feed_id+"',";
				value+="'"+page.url+"',";
				value+="'"+page.title+"',";
				value+="'"+page.created_at+"',";
				value+="'"+page.updated_at+"')";
				query="INSERT INTO articles (id, feed_id, url, title, created_at, updated_at) VALUES "+ value;
				
				//System.out.println("In local connector "+query);
				
				stmt.executeUpdate(query);
				stmt.close();
			}
			
			 
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		 
	}
	
	public ArrayList<Integer> getIDs(){
		Statement stmt=null;
		String query="SELECT id FROM articles";
		ArrayList<Integer> idList = new ArrayList<Integer>();
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				idList.add(rs.getInt("id"));
			}
			return idList;
		}catch(SQLException e){
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public void updateHTML(int id, String html){
		Statement stmt=null;
		String query="UPDATE articles SET html = '"+html+"' WHERE id="+id;
		 
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			stmt.executeUpdate(query);
			 
			 
		}catch(SQLException e){
			e.printStackTrace();
		}
		 
		 
	}

	public WebPageRecord read(int row){
		Statement stmt=null;
		String query="SELECT id, feed_id, url, title, created_at, updated_at, content  FROM articles "+" LIMIT "+(row-1)+" , "+1;
		WebPageRecord page=new WebPageRecord();
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			page.id=rs.getInt("id");
			page.feed_id=rs.getInt("feed_id");
			page.url=rs.getString("url");
			page.title=rs.getString("title");
			page.created_at=rs.getDate("created_at");
			page.updated_at=rs.getDate("updated_at");
			page.content=rs.getString("content");
			stmt.close();
			
			return page;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public ArrayList<WebPageRecord> readMore(int row, int number){
		Statement stmt=null;
		String query="SELECT id, feed_id, url, title, created_at, updated_at, content  FROM articles "+" LIMIT "+(row-1)+" , "+number;
		
		ArrayList<WebPageRecord> result=new ArrayList<WebPageRecord>();
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			
			while(rs.next()){
				WebPageRecord page=new WebPageRecord();
				page.id=rs.getInt("id");
				page.feed_id=rs.getInt("feed_id");
				page.url=rs.getString("url");
				page.title=rs.getString("title");
				page.created_at=rs.getDate("created_at");
				page.updated_at=rs.getDate("updated_at");
				page.content=rs.getString("content");
				result.add(page);
			}
			 
			
			stmt.close();
			
			return result;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		 
		return null;
	}

	public void insertContent(String content, int id){
		Statement stmt=null;
		content=content.replace("'", "\\'");
		
		String query="UPDATE articles SET content='"+content+"' WHERE id="+id+" AND content IS NULL"; 
		
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			
		}catch(SQLException e){
			System.err.println("wrong sql:"+ id);
			e.printStackTrace();
		}
		 
	}
	
	public void webContentToFile(String outputFolder, int row) throws IOException{
		WebPageRecord record=read(row);
		int id=record.id;
		String filename=id+"";
		File file=new File(outputFolder+"/"+filename);
		
		if(record.content==null||record.content.equals("")) return;
		if(file.exists()) return;
		else{
			file.createNewFile();
		}
		
		 
		
		PrintWriter pw=new PrintWriter(file);
		
		
		//System.out.println(record.content);
		
		pw.write(record.content);
		pw.close();
	}
	
	public void webContentToFile(String outputFolder, WebPageRecord record) throws IOException{
		 
		int id=record.id;
		String filename=id+"";
		File file=new File(outputFolder+"/"+filename);
		
		if(record.content==null||record.content.equals("")) return;
		if(file.exists()) return;
		else{
			file.createNewFile();
		}
		
		 
		
		PrintWriter pw=new PrintWriter(file);
		
		
		//System.out.println(record.content);
		
		pw.write(record.content);
		pw.close();
	}
	
	public void webTitleToFile(String outputFolder, int row, boolean isDeleteResourceName) throws IOException{
		WebPageRecord record=read(row);
		int id=record.id;
		String filename=id+"";
		File file=new File(outputFolder+"/"+filename);
		
		if(!file.exists()){
			file.createNewFile();
		}
		//System.err.println(file.getName());
		
		PrintWriter pw=new PrintWriter(file);
		
		
		//System.out.println(record.content);
		if(isDeleteResourceName){
			record.title=CommonMethods.getRidOfResourceName(record.title);
		}
		pw.write(record.title);
		pw.close();
	}

	public static void webContentTofile(String outputFolder, int start, int end) throws IOException{
		LocalConnector conn=new LocalConnector();
		conn.connect();
		for(int i=start;i<=end;i++){
			conn.webContentToFile(outputFolder, i);
		}
		conn.close();
	}

	public void saveTopicFile(String topicFilePath) throws FileNotFoundException{
		String topic,query;
		Statement stmt=null;
		
		ArrayList<Integer> topicIDs=new ArrayList<Integer>();

		 
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		String timeStamp=sdf.format(TopicFileReader.getDateFromFilePath(topicFilePath));

		try{
			 
			stmt=conn.createStatement();
			 
			TopicFileReader topicReader=new TopicFileReader(topicFilePath);
			topicReader.open();
			
			query="SELECT COUNT(*) AS num FROM topic WHERE created_at= '"+timeStamp+"'";
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			int num=rs.getInt("num");
			rs.close();
			if(num==0){
				while(topicReader.hasNext()){
					topic=topicReader.nextTopic();
				
					query="INSERT INTO topic (topic_id,  topic_keys, category, created_at, updated_at) VALUES (NULL, '"+topic+"','Economy','"+timeStamp+"','"+timeStamp+"')";
					stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
					rs=stmt.getGeneratedKeys();
					rs.next();
					topicIDs.add(rs.getInt(1));
						 
				}
			}
			
			stmt.close();
			
			java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String timeStamp2=sdf2.format(TopicFileReader.getDateFromFilePath(topicFilePath));
			saveTopicIDs(CommonCommands.TOPIC_ID_FOLDER+"topicID_"+timeStamp2,topicIDs);
			
		}catch(SQLException e){
			 
			e.printStackTrace();
		}
		
	 
	}
	
	public void saveOpinions(String opinionFilePath, String phraseFilePath, String topicIDFilePath){
		String query = null;
		Statement stmt=null;
		int newsID, topicID;
		double opinionValue,relevance;
		String phrase;
		try {
			OpinionFileReader ofReader= new OpinionFileReader(opinionFilePath, phraseFilePath, topicIDFilePath);
			ArrayList<NewsOpinion> opinionList;
			while(ofReader.hasNext()){
				opinionList=ofReader.nextLine();
				for(int i=0;i<opinionList.size();i++){
					
					newsID=opinionList.get(i).newsID;
					topicID=opinionList.get(i).topicID;
					opinionValue=opinionList.get(i).opinionValue;
					relevance=opinionList.get(i).relevance;
					phrase=opinionList.get(i).phrases;
					
					if(isOpinionExists(newsID, topicID)) continue;
					
					stmt=conn.createStatement();
					//query="INSERT INTO news_opinion (id, news_id, topic_id, opinion_value, relevance， phrase) VALUES (NULL, '"+newsID+"','"+topicID+"','"+opinionValue+"','"+relevance+"', '"+phrase+"')";
					query="INSERT INTO `tom`.`news_opinion` (`news_id`, `topic_id`, `opinion_value`, `relevance`, `phrase`) VALUES ('"+newsID+"', '"+topicID+"', '"+opinionValue+"', '"+relevance+"', '"+phrase+"');";
					stmt.executeUpdate(query);
				
				}
			
			}
		} catch (FileNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(query);
		} 
	}
	
	private void saveTopicIDs(String filePath, ArrayList<Integer> topicIDs){
		 try{
			File topicIDFile=new File(filePath);
			if(!topicIDFile.exists())
				topicIDFile.createNewFile();
			else
				return;
			PrintWriter pw=new PrintWriter(topicIDFile);
			for(int i=0;i<topicIDs.size();i++){
				pw.write(topicIDs.get(i).intValue()+"\n");
			}
			pw.flush();
			pw.close();
		 }catch(Exception e){
			 System.out.println("Cannot save topic id as file.");
		 }
		
	}
	
	private boolean isOpinionExists(int newsID, int topicID){
		String query;
		try{
			Statement stmt=conn.createStatement();
			query="SELECT COUNT(*) AS num FROM news_opinion WHERE news_id= '"+newsID+"' AND topic_id='"+topicID+"'";
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			int num=rs.getInt("num");
			 
			if(num>0) return true;
			else return false;
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
}
