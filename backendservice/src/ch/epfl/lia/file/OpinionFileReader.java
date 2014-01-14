package ch.epfl.lia.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.csvreader.CsvReader;

import ch.epfl.lia.datamodel.NewsOpinion;

public class OpinionFileReader {
	
	private String opinionFilePath;
	private String topicIDFilePath;
	private String phraseFilePath;
	private File opinionFile;
	private File topicIDFile;
	private File phraseFile;
	private Scanner opinionScanner;
	private Scanner phraseScanner;
	private ArrayList<Integer> topicIDs;
 
/*	
	public OpinionFileReader(String opinionFilePath, String topicIDFilePath) throws FileNotFoundException{
		this.opinionFilePath=opinionFilePath;
		this.topicIDFilePath=topicIDFilePath;
		this.opinionFile=new File(opinionFilePath);
		this.topicIDFile=new File(topicIDFilePath);
		this.opinionScanner=new Scanner(opinionFile);
     	this.topicIDs=getTopicIDList();
	}
	*/
	
	public OpinionFileReader(String opinionFilePath, String phraseFilePath, String topicIDFilePath) throws FileNotFoundException{
		this.opinionFilePath=opinionFilePath;
		this.topicIDFilePath=topicIDFilePath;
		this.phraseFilePath=phraseFilePath;
		this.opinionFile=new File(opinionFilePath);
		this.topicIDFile=new File(topicIDFilePath);
		this.phraseFile=new File(phraseFilePath);
		this.opinionScanner=new Scanner(opinionFile);
		this.phraseScanner=new Scanner(phraseFile);
		
		this.topicIDs=getTopicIDList();
	}
	
	private ArrayList<Integer> getTopicIDList(){
		
		ArrayList<Integer> idList=new ArrayList<Integer> ();
		try {
			Scanner scan=new Scanner(this.topicIDFile);
			while(scan.hasNext()){
				idList.add(scan.nextInt());
				
			}
			scan.close();
			
			return idList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot read file.");
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasNext(){
		return opinionScanner.hasNext() && phraseScanner.hasNext();
	}
	
	public ArrayList<NewsOpinion> nextLine(){
		ArrayList<NewsOpinion> result=new ArrayList<NewsOpinion>();
		
		String aLine=opinionScanner.nextLine();
		String[] opinions=aLine.split(",");
		int id=Integer.parseInt(opinions[0]);
		
		String phraseLine = phraseScanner.nextLine();
		CsvReader phraseReader = CsvReader.parse(phraseLine);
		 
		try {
			phraseReader.readRecord();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int length=opinions.length;
		for(int i=1;i<length;i++){
			try{
				double opinionValue=Double.parseDouble(opinions[i]);
				if(opinionValue==0) 
					continue;
				result.add(new NewsOpinion(id,topicIDs.get(i-1),opinionValue,Math.abs(opinionValue), phraseReader.get(i)));
			}catch(Exception e){
				
			}	 
		}
		return result;
	}
	
 
}

