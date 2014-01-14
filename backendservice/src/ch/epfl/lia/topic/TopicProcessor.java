package ch.epfl.lia.topic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

import ch.epfl.lia.datamodel.Topic;

public class TopicProcessor {

	private ArrayList<Topic> topicList = new ArrayList<Topic>();
	private String topicFilePath;
	private HashMap<String, Integer> keyCounter = new HashMap<String, Integer>(); 
	private final int COMMON_THRESHOLD = 2;
	private ArrayList<String> commonKeys = new ArrayList<String>();
	private ArrayList<Topic> distinctTopic = new ArrayList<Topic>();
	private final static int TOP_N = 30;
	
	public TopicProcessor(String topicFilePath){
		this.topicFilePath = topicFilePath;
	}
	
	public void getDistinctTopic(int numTopics, String outputFilePath){
		readTopics();
		findCommonKeys();
		findDistinctTopics(numTopics);
		saveDistinctTopics(outputFilePath);
	}
	
	private void readTopics(){
		File topicFile = new File(topicFilePath);
		if(!topicFile.exists())
			System.err.println("Topic file not found");
		topicList.clear();
		
		try {
			Scanner scanner = new Scanner(topicFile);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] infos = line.split("	");
				
				 
				int id = Integer.parseInt(infos[0]);
				double param = Double.parseDouble(infos[1]);
				String[] keyString = infos[2].split(" ");
				
				ArrayList<String> keys = new ArrayList<String>();
				for(int i=0; i<keyString.length; i++){
					keys.add(keyString[i]);
				}
				Topic topic = new ExtendedTopic(id, param, keys);
				topicList.add(topic);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void findCommonKeys(){
		for(Topic topic: topicList){
			ArrayList<String> keys = topic.getKeys();
			for(String key: keys){
				if(keyCounter.containsKey(key)){
					
					keyCounter.put(key, keyCounter.get(key)+1);
				}else{
					 
					keyCounter.put(key, 1);
				}
			}
		}
		
		for(Entry<String, Integer> entry: keyCounter.entrySet()){
			 
			if(entry.getValue() >= COMMON_THRESHOLD)
				commonKeys.add(entry.getKey());
		}
	}
	
	private void findDistinctTopics(int numTopic){
		
		 ArrayList<ExtendedTopic> eTopicList = new ArrayList<ExtendedTopic>();
		 for(Topic topic: topicList){
			 ExtendedTopic eTopic = (ExtendedTopic) topic;
			 Iterator<String> keyIterator = eTopic.getKeys().iterator();
			 int counter = 0;
			 while(keyIterator.hasNext() && counter < TOP_N){
				 String key = keyIterator.next();
				 if(commonKeys.contains(key)){
					  //Count the common key number and delete the common key from topic
					  eTopic.setNumCommonKeys(eTopic.getNumCommonKeys()+1);
					  //keyIterator.remove();
				 }
				 counter ++;
				 
			 }
			 eTopicList.add(eTopic);
		 }
		 Collections.sort(eTopicList, new TopicComparator());
		 for(int i=0; i<numTopic&&i<eTopicList.size(); i++){
			 distinctTopic.add(eTopicList.get(i));
		 }
	}
	
	private void saveDistinctTopics(String outputFilePath){
		File file = new File(outputFilePath);
		 
		try {
			
			if(!file.exists())
				file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			
			for(Topic topic: distinctTopic){
				pw.println(topic.toString());
			}
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ExtendedTopic extends Topic{

		private int numCommonKeys=0;
		
		public ExtendedTopic(int id, double param, ArrayList keys) {
			super(id, param, keys);
 
			// TODO Auto-generated constructor stub
		}
		
		public void setNumCommonKeys(int numCommonKeys){
			this.numCommonKeys = numCommonKeys;
		}
		
		public int getNumCommonKeys(){
			return numCommonKeys;
		}
	}
	
	private class TopicComparator implements Comparator<ExtendedTopic>{

		@Override
		public int compare(ExtendedTopic o1, ExtendedTopic o2) {
			// TODO Auto-generated method stub
			
			return o1.getNumCommonKeys() - o2.getNumCommonKeys();
		}

		 

		 
		
	}
}
