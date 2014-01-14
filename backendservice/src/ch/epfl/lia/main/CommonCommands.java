package ch.epfl.lia.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import ch.epfl.lia.database.DatabaseManager;
import ch.epfl.lia.database.LIAReader;
import ch.epfl.lia.database.LIAWriter;
import ch.epfl.lia.database.LocalConnector;
import ch.epfl.lia.opinion.DocumentParser;
import ch.epfl.lia.opinion.OpinionExtractor;
import ch.epfl.lia.webpage.WebPage;
import ch.epfl.lia.webpage.WebPageRecord;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

public class CommonCommands {
	public static final String DEFAULT_OUTPUT_FOLDER="/home/xtang/Master_Project/file/";
	public static final boolean DELETE_RESOURCE_NAME=true;
	public static final String DEFAULT_TITLE_FOLDER="/home/xtang/Master_Project/file/title/";
	public static final String DEFAULT_PARSED_TITLE_FOLDER="/home/xtang/Master_Projectfile/title/parsed/";
	public static final String DEFAULT_ORIGIN_TITLE_FOLDER="/home/xtang/Master_Project/file/title/origin/";
	public static final String DEFAULT_TOPIC_FOLDER="/home/xtang/Master_Project/file/topic/";
	public static final String DEFAULT_DICTIONARY_FILE="/home/xtang/Master_Project/file/dictionary/kai.tff";
	public static final String DEFAULT_OPINION_OUTPUT_FOLDER="/home/xtang/Master_Project/file/result";
	public static final String DEFAULT_CONTENT_ORIGIN_FOLDER="/home/xtang/Master_Project/file/content/origin";
	public static final String DEFAULT_PARSED_CONTENT_FOLDER="/home/xtang/Master_Project/file/parsed";
	public static final String DEFAULT_OPINION_FOLDER="/home/xtang/Master_Project/file/content/opinion/";
	public static final String DEFAULT_RESULT_FOLDER="/home/xtang/Master_Project/file/result/";
	public static final String TOPIC_ID_FOLDER="/home/xtang/Master_Project/file/topicID/";
	
	public static void saveNewsTitleAsFile(int start, int end, String outputFolderName, boolean isDeleteResourceName){
		LocalConnector connector=new LocalConnector();
		connector.connect();
		for(int i=start;i<=end;i++){
			try {
				connector.webTitleToFile(outputFolderName, i, isDeleteResourceName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}
		connector.close();
	}
	
	public static void parseAllFiles(String inputFolderName, String outputFolderName){
		try {
			DocumentParser.parseWebContent(inputFolderName, outputFolderName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public static void extractOpinion(String topicFileName){
		try {
			OpinionExtractor extractor=new OpinionExtractor(DEFAULT_TOPIC_FOLDER+topicFileName, DEFAULT_DICTIONARY_FILE, "OpinionFinder",false, false);
			extractor.extractOpinionFromFolder(DEFAULT_PARSED_CONTENT_FOLDER, DEFAULT_OPINION_OUTPUT_FOLDER);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	public static void saveTopicFileToDB(String topicFileName){
		LocalConnector connector=new LocalConnector();
		connector.connect();
		try {
			connector.saveTopicFile(DEFAULT_TOPIC_FOLDER+topicFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connector.close();
	}
	
	public static void saveOpinionFileToDB(String opinionFilePath, String phraseFilePath, String topicFilePath){
		LocalConnector connector=new LocalConnector();
		connector.connect();
		connector.saveOpinions(opinionFilePath, phraseFilePath, topicFilePath);
		connector.close();
	}
	
	/*
	public static void extractContent(int start, int end){
		 LIAReader reader=new LIAReader();
		 LIAWriter writer=new LIAWriter();
		 DatabaseManager.extractContent(reader, writer, start, end);
	}
	*/
	
	public static void extractContent(int start, int end){
		 LIAReader reader=new LIAReader();
		 LocalConnector writer=new LocalConnector();
		 DatabaseManager.extractContent(reader, writer, start, end);
	}
	
	public static void populateID(int start, int end){
		LIAReader reader=new LIAReader();
		LIAWriter writer=new LIAWriter();
		
		DatabaseManager manager=new DatabaseManager();
		manager.populateID(reader, writer, start, end);
	}
	
	public static void populateContent(int start, int end){
		LIAReader reader=new LIAReader();
		LIAWriter writer=new LIAWriter();
		
		DatabaseManager manager=new DatabaseManager();
		 manager.populateContent(reader, writer, start, end);
	}
	
	public static void saveContentAsFile(int start, int end){
		LocalConnector connector=new LocalConnector();
		connector.connect();
		
		/*
		for(int i=start;i<=end;i++){
			try {
				connector.webContentToFile(DEFAULT_CONTENT_ORIGIN_FOLDER, i);
				System.out.println("Save "+i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		int i=start;
		int counter=i;
		int interval=100;
		while(i<end){
			if(i+interval>end){
				interval=1;
			}
			
			ArrayList<WebPageRecord> result=connector.readMore(i, interval);
			for(WebPageRecord record: result){
				try {
					connector.webContentToFile(DEFAULT_CONTENT_ORIGIN_FOLDER, record);
					System.err.println(counter++);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			i+=interval;
			
		}
		
		connector.close();
		
		
	}
	
	 
	
	 
}
