package ch.epfl.lia.topic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;

public class MalletExtension {

	public static final String DEFAULT_OPINION_CORPUS_FOLDER = "/home/xtang/Master_Project/file/content/topic_model/1031/";
	public static final String DEFAULT_ORIGIN_CONTENT_FOLDER = "/home/xtang/Master_Project/file/content/origin/";
	private static final double PROBABILITY_THRESHOLD = 0.7;
	 
	
	public static ArrayList<String> findArticlesByTopic(String compositionFileName, int topicID){
		File compositionFile=new File(DEFAULT_OPINION_CORPUS_FOLDER+compositionFileName);
		ArrayList<String> articleList=new ArrayList<String>();
		Scanner scanner;
		
		try {
			scanner = new Scanner(compositionFile);
			scanner.nextLine();
			while(scanner.hasNextLine()){
				String temp= scanner.nextLine();
			 
				String[] array=temp.split("	");
				
				if(Integer.parseInt(array[2])==topicID && Double.parseDouble(array[3])>=PROBABILITY_THRESHOLD){
					String[] temp2=array[1].split("/");
					articleList.add(temp2[temp2.length-1]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return articleList;
	}
	
	public static void findArticlesByTopic(String compositionFilePath, int topicID, String outputFolderPath){
		ArrayList<String> articleList=findArticlesByTopic(compositionFilePath, topicID);
		File outputFolder=new File(outputFolderPath);
		if(!outputFolder.exists()){
			outputFolder.mkdir();
		}
		
		for(String fileName:articleList){
			try{
				File source=new File("/home/xtang/Master_Project/file/content/noun/"+fileName);
				File target=new File(outputFolder+fileName);
				if(!target.exists())
					target.createNewFile();
				Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			 
		}
	}
	
	public static void classifyArticlesByTopic(String compositionFilePath, String nounFileFolder, String parsedFileFolder, String outputFolder, int topicID){
		File compositionFile=new File(compositionFilePath);
		ArrayList<String> articleList=new ArrayList<String>();
		Scanner scanner;
		
		try {
			scanner = new Scanner(compositionFile);
			scanner.nextLine();
			while(scanner.hasNextLine()){
				String temp= scanner.nextLine();
			 
				String[] array=temp.split("	");
				
				if(Integer.parseInt(array[2])==topicID && Double.parseDouble(array[3])>=PROBABILITY_THRESHOLD){
					String[] temp2=array[1].split("/");
					articleList.add(temp2[temp2.length-1]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File outputNounFolder = new File(outputFolder+"/noun/"+topicID+"/");
		File outputParsedFolder = new File(outputFolder+"/parsed/"+topicID+"/");
		
		if(!outputNounFolder.exists())
			outputNounFolder.mkdir();
		if(!outputParsedFolder.exists())
			outputParsedFolder.mkdir();
		
		for(String article: articleList){
			try{
				File nounFile=new File(nounFileFolder+"/"+article);
				File parsedFile=new File(parsedFileFolder+"/"+article);
				
				File copyNounFile=new File(outputNounFolder+"/"+article);
				File copyParsedFile=new File(outputParsedFolder+"/"+article);
				 
				if(!copyNounFile.exists())
					copyNounFile.createNewFile();
				if(!copyParsedFile.exists())
					copyParsedFile.createNewFile();
				
				Files.copy(nounFile.toPath(), copyNounFile.toPath(), REPLACE_EXISTING);
				Files.copy(parsedFile.toPath(), copyParsedFile.toPath(), REPLACE_EXISTING);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		 
	}
	
}
