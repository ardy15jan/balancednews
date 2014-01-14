package ch.epfl.lia.main;

import java.util.ArrayList;

import ch.epfl.lia.database.DatabaseManager;
import ch.epfl.lia.database.LIAReader;
import ch.epfl.lia.database.LocalConnector;
import ch.epfl.lia.opinion.CompareOpinion;
import ch.epfl.lia.opinion.OpinionExtractor;
import ch.epfl.lia.opinion.PCAAnalizer;
import ch.epfl.lia.topic.MalletExtension;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//int start=Integer.parseInt(args[0]);
		//int end=Integer.parseInt(args[1]);
		
		//DatabaseManager mManager=new DatabaseManager();
		//mManager.copy(start, end);
 
		//mManager.extractContent(new LIAReader(), new LocalConnector(), start, end);
 
		
		//CommonCommands.saveNewsTitleAsFile(1,2000,CommonCommands.DEFAULT_ORIGIN_TITLE_FOLDER, CommonCommands.DELETE_RESOURCE_NAME);
		//CommonCommands.parseAllFiles("/home/xtang/Master_Project/file/dp/test/", "/home/xtang/Master_Project/file/dp/parsed/");
		//CommonCommands.extractOpinion("topics_2013-10-24_02:50:12.txt");
		//CommonCommands.saveTopicFileToDB("topics_2013-12-01_11:02:55.txt");
		CommonCommands.saveOpinionFileToDB("/home/xtang/Master_Project/file/content/opinion/opinion_2013-12-01_11:02:55.txt", "/home/xtang/Master_Project/file/content/opinion/phrase_2013-12-01_11:02:55.txt", "/home/xtang/Master_Project/file/topicID/topicID_2013-12-01_11:02:55");
		//CommonCommands.populateID(1, 5000);
		
		//CommonCommands.populateContent(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		//CommonCommands.saveContentAsFile(1, 50000);
		//CommonCommands.test();
		//CommonCommands.extractContent(start, end);
		
		/*
		for(int i=0;i<20;i++){
			MalletExtension.findArticlesByTopic("composition.txt",i,"/home/xtang/Master_Project/file/handmade/"+i+"/");
		}
		*/
		
		/*
		try {
			
			OpinionExtractor extractor=new OpinionExtractor("/home/xtang/Master_Project/file/content/topic_model/1201/topics_2013-12-01_11:02:55.txt", CommonCommands.DEFAULT_DICTIONARY_FILE, "OpinionFinder","/home/xtang/Master_Project/file/content/topic_model/1113/composition.txt", false, false);
			extractor.extractOpinionFromFolder("/home/xtang/Master_Project/file/content/parsed/", "/home/xtang/Master_Project/file/content/opinion/");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*
		
		PCAAnalizer pca=new PCAAnalizer();
		pca.extractOpinionFromFolder("/home/xtang/Master_Project/file/dp/euro/opinion/opinionMatrix/");
		*/
		 
		 
		//pca.compareWith("/home/xtang/Master_Project/file/temp/classified/energy/opinion/opinionMatrix/pca", "/home/xtang/Master_Project/file/temp/classified/energy/opinion/opinion_2013-11-04_11:02:55.txt", "/home/xtang/Master_Project/file/temp/classified/energy/opinion/opinionMatrix/");
		
	}

}
