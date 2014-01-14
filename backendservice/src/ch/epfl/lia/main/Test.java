package ch.epfl.lia.main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.lia.others.UpdateHTML;
import ch.epfl.lia.topic.MalletExtension;
import ch.epfl.lia.topic.TopicProcessor;
import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.ImageExtractor;
import de.l3s.boilerpipe.document.Image;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//UpdateHTML.updateHTML();
		
		
		TopicProcessor tp = new TopicProcessor("/home/xtang/Master_Project/file/content/topic_model/1113/topickey.txt");
		tp.getDistinctTopic(50, "/home/xtang/Master_Project/file/content/topic_model/arranged/distinct_topickey.txt");
		
		/*
		for(int i=0;i<50;i++){
			MalletExtension.classifyArticlesByTopic("/home/xtang/Master_Project/file/content/topic_model/1113/composition.txt", "/home/xtang/Master_Project/file/content/noun/", "/home/xtang/Master_Project/file/content/parsed/", "/home/xtang/Master_Project/file/classified/", i);
		}
		*/
		
		/*
		try{
			URL url = new URL(
	                "http://www.spiegel.de/wissenschaft/natur/0,1518,789176,00.html");

			// choose from a set of useful BoilerpipeExtractors...
			final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
			//final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
			//final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
			//final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
			
			final ImageExtractor ie = ImageExtractor.INSTANCE;
			
			List<Image> imgUrls = ie.process(url, extractor);
			
			// automatically sorts them by decreasing area, i.e. most probable true positives come first
			Collections.sort(imgUrls);
			
			for(Image img : imgUrls) {
			        System.out.println("* "+img);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		*/
		
		
	
	}

}
