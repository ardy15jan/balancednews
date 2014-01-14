package ch.epfl.lia.database;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.SQLException;

import ch.epfl.lia.others.CommonMethods;
import ch.epfl.lia.webpage.WebPage;
import ch.epfl.lia.webpage.WebPageRecord;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
 

public class DatabaseManager {

	public static void copy(int start, int end){
		LIAReader reader=new LIAReader();
		reader.connect();
		
		LocalConnector localConnector=new LocalConnector();
		localConnector.connect();
		 
		WebPageRecord result=new WebPageRecord();
		
		for(int i=start;i<=end;i++){
			result=reader.read(i);
			localConnector.copy(result);
			System.out.println("copy "+i);
		}

	}
	
public static void extractContent(LIAReader reader, LocalConnector localConnector, int start, int end){
		
		//LocalConnector localConnector=new LocalConnector();
		localConnector.connect();
		
		//LIAReader reader=new LIAReader();
		reader.connect();
		
		
		WebPageRecord page;
		
		
		for(int i=start;i<=end;i++){
			page=localConnector.read(i);
			//content=new WebContent(reader.);
			
			
			if(page.content==null||page.content.equals("")){
				//System.out.println(i+" ");
				try {
					localConnector.insertContent(WebPage.getContent(reader.getContent(i)), page.id);
				} catch (MalformedURLException | BoilerpipeProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				 
			}
			System.out.println(" "+i);
			
		}
		
		localConnector.close();
		reader.close();
		
	}

	public static void extractContent(LIAReader reader, LIAWriter writer, int start, int end){
		System.out.println("Note: you are writing to lia database");
		//LocalConnector localConnector=new LocalConnector();
		writer.connect();
		reader.connect();
		 
		writer.setTable("articles_parsed");

		WebPageRecord page;
		
		
		for(int i=start;i<=end;i++){
			page=reader.read(i);
			//content=new WebContent(reader.);
			
			
		 
				//System.out.println(i+" ");
				try {
					//writer.insertContent(WebPage.getContent(reader.getHTML(i)), page.id);
					writer.updateContent("content", page.id, WebPage.getContent(reader.getHTML(i)));
				} catch (MalformedURLException | BoilerpipeProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 
			System.out.println(" "+i);
			
		}
		
		writer.close();
		reader.close();
		
	}
	
	public static void populateID(LIAReader reader, LIAWriter writer,int start, int end){
		 
			 
		writer.connect();
		reader.connect();
			
		 
	 
		writer.setTable("articles_parsed");
		
		for(int i=start;i<=end;i++){
			int id=reader.getID(i);
			writer.insertID(id);
			System.err.println("Insert "+i);
		}
	}
	
	public static void populateContent(LIAReader reader, LIAWriter writer,int start, int end){
		//writer.connect();
		reader.connect();
		//writer.setTable("articles_parsed");
		for(int i=start;i<=end;i++){
			
			try {
				int id=reader.getID(i);
				//writer.insertID(id);
				String title=reader.getTitle(i);
				String content=WebPage.getContent(reader.getHTML(i));
				//writer.updateContent("title", id, title);
				//writer.updateContent("content", id, content);
				
				saveAsFile(title, "file/title/origin", id, true);
				saveAsFile(content, "file/content/origin",id, false);
				
			} catch (MalformedURLException | BoilerpipeProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			System.err.println("Insert "+i);
			 
				
		}
	}

	public static void saveAsFile(String content, String outputFolder, int id, boolean isDeleteResourceName){
		 try{
			 String filename=id+"";
				File file=new File(outputFolder+"/"+filename);
				
				if(!file.exists()){
					 
						file.createNewFile();
					 
				}else{
					return;
				}
				//System.err.println(file.getName());
				
				PrintWriter pw=new PrintWriter(file);
				
				
				//System.out.println(record.content);
				if(isDeleteResourceName){
					content=CommonMethods.getRidOfResourceName(content);
				}
				pw.write(content);
				pw.close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }
			
	}
 
}
