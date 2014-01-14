package ch.epfl.lia.opinion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import ch.epfl.lia.others.CommonMethods;
import ch.epfl.lia.webpage.WebPage;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

import com.csvreader.CsvWriter;

import ch.epfl.ic.oe.model.TopicModel;
import ch.epfl.ic.oe.opinion.DependencyOpinionExtractor;
import ch.epfl.ic.oe.opinion.dictionary.DictionaryFactory;
import ch.epfl.ic.oe.opinion.dictionary.SentimentDictionary;

public class OpinionExtractor {
	
	/*
	-dict_class <arg>        The class of the sentiment dictionary
	-dict_path <arg>
	-input_dir               Input directory that contains the parsed
	                          documents (.stp file)
	-output_context <arg>    Output file for topical contextual sentences
	-output_opinion <arg>    Output file for topical opinion
	-output_phrase <arg>     Output file for topical opinionated phrases
	-stem_topic_words        Whether or not the topic words are stemmed
	-topic_word_as_opinion   Whether or not topic words can appear as opinion
	                          words (i.e. modifier of another noun)
	-topics <arg>            The set of topics used to extract opinions
	*/
	
	private String topicFilePath;
	private String dictionaryFilePath;
	private String dictionaryClass;
	private String contexFilePath;
	private String compositionFilePath;
	private String ABSOpinionFilePath;
	private String opinionFilePath;
	private String phraseFilePath;
	private String inputFolder;
	private String inputFilePath;
	private String opinionMatrixFolder;
	private boolean isTopicWordsStemmed;
	private boolean isTopicWordAsOpinion;
	 
	
	public OpinionExtractor(String topicFilePath, String dictionaryFilePath, String dictionaryClass, 
			String contextFilePath, String opinionFilePath, String phraseFilePath, String inputFolder,
			String inputFilePath, boolean isTopicWordsStemmed, boolean isTopicWordAsOpinion){
		
		this.topicFilePath=topicFilePath;
		this.dictionaryFilePath=dictionaryFilePath;
		this.dictionaryClass=dictionaryClass;
		this.contexFilePath=contextFilePath;
		this.opinionFilePath=opinionFilePath;
		this.phraseFilePath=phraseFilePath;
		this.inputFolder=inputFolder;
		this.inputFilePath=inputFilePath;
		this.isTopicWordsStemmed=isTopicWordsStemmed;
		this.isTopicWordAsOpinion=isTopicWordAsOpinion;
	}
	
	public OpinionExtractor(){
		
	}
	
	public OpinionExtractor(String topicFilePath, String dictionaryFilePath, String dictionaryClass, String compositionFilePath, 
			 boolean isTopicWordsStemmed, boolean isTopicWordAsOpinion) throws Exception{
		this.topicFilePath=topicFilePath;
		this.dictionaryFilePath=dictionaryFilePath;
		this.dictionaryClass=dictionaryClass;
		this.compositionFilePath=compositionFilePath;
		this.isTopicWordsStemmed=isTopicWordsStemmed;
		this.isTopicWordAsOpinion=isTopicWordAsOpinion;
		
		//Check the validity of input topic file 
		String[] filePath=this.topicFilePath.split("/");
		String fileName=filePath[filePath.length-1];
		String timeStamp=fileName.split("_")[1]+" "+fileName.split("_")[2];
		if(!CommonMethods.isDateValid(timeStamp)){
			throw new Exception("not valid topic file.");
		}
	}
	
	public void getOpinion(URL url, String outputFileName) throws FileNotFoundException, MalformedURLException, BoilerpipeProcessingException{
		WebPage page=new WebPage(url);
		DocumentParser.parseString(page.getContent(), outputFileName+".stp");
	}
	
	public void getOpinion(String inputString, String outputFileName) throws FileNotFoundException{
		DocumentParser.parseString(inputString, outputFileName+".stp");
	}
	
	public void extractOpinionFromFile(String inputFilename, String contextFilePath, String opinionFilePath, String phraseFilePath) throws IOException{
		
		this.inputFilePath=inputFilename;
		this.opinionFilePath=opinionFilePath;
		this.contexFilePath=contextFilePath;
		this.phraseFilePath=phraseFilePath;
		
		File inputFile=new File(this.inputFilePath);
		if(!inputFile.exists()){
			System.err.println("Error: the input path does not exist");
           System.exit(1);
		}
		
		TopicModel model = new TopicModel(this.topicFilePath, 100,
                this.isTopicWordsStemmed);
		DictionaryFactory df = new DictionaryFactory();
		SentimentDictionary dict = df.createDictionary(
	                this.dictionaryClass,
	                this.dictionaryFilePath);
	   DependencyOpinionExtractor extractor = new DependencyOpinionExtractor(
	                model, dict, this.isTopicWordAsOpinion);
	   
	   
	   CsvWriter opinionWriter = null;
       if (this.opinionFilePath != null) {
           opinionWriter = new CsvWriter(this.opinionFilePath ,
                   ',', Charset.forName("UTF-8"));
       }
       CsvWriter phraseWriter = null;
       if (this.phraseFilePath != null) {
           phraseWriter = new CsvWriter(this.phraseFilePath,
                   ',', Charset.forName("UTF-8"));
       }
       CsvWriter contextWriter = null;
       if (this.contexFilePath != null) {
           contextWriter = new CsvWriter(this.contexFilePath,
                   ',', Charset.forName("UTF-8"));
       }
       
       
       extractor.extractOpinion(inputFilePath);
       if (opinionWriter != null) {
           opinionWriter.write(inputFile.getName());
           for (Double d : extractor.topicalOpinions()) {
               opinionWriter.write(d.toString());
           }
           opinionWriter.endRecord();
       }
       if (phraseWriter != null) {
           phraseWriter.write(inputFile.getName());
           for (String phrase : extractor.topicalPhrases()) {
               phraseWriter.write(phrase);
           }
           phraseWriter.endRecord();
       }
       if (contextWriter != null) {
           contextWriter.write(inputFile.getName());
           for (String context : extractor.topicalContexts()) {
               contextWriter.write(context);
           }
           contextWriter.endRecord();
       }
       
       if (opinionWriter != null) {
           opinionWriter.close();
       }
       if (phraseWriter != null) {
           phraseWriter.close();
       }
       if (contextWriter != null) {
           contextWriter.close();
       }
       
		
	}
	
	public void extractOpinionFromFolder(String inputFolder, String contextFilePath, String opinionFilePath,  String phraseFilePath, String ABSOpinionFilePath, String opinionMatrixFolder) throws IOException{
		 
		this.inputFolder=inputFolder;
		this.opinionFilePath=opinionFilePath;
		this.contexFilePath=contextFilePath;
		this.phraseFilePath=phraseFilePath;
		this.ABSOpinionFilePath=ABSOpinionFilePath;
		this.opinionMatrixFolder=opinionMatrixFolder;
		
		File inputDir=new File(this.inputFolder);
		if (!inputDir.isDirectory()) {
            System.err.println("Error: the input path is not a directory");
            System.exit(1);
        }
		
		TopicModel model = new TopicModel(this.topicFilePath, 100,
                this.isTopicWordsStemmed);
		DictionaryFactory df = new DictionaryFactory();
		SentimentDictionary dict = df.createDictionary(
	                this.dictionaryClass,
	                this.dictionaryFilePath);
	   DependencyOpinionExtractor extractor = new DependencyOpinionExtractor(
	                model, dict, this.compositionFilePath, this.isTopicWordAsOpinion);
	   
	   CsvWriter opinionWriter = null;
       if (this.opinionFilePath != null) {
           opinionWriter = new CsvWriter(this.opinionFilePath ,
                   ',', Charset.forName("UTF-8"));
       }
       CsvWriter phraseWriter = null;
       if (this.phraseFilePath != null) {
           phraseWriter = new CsvWriter(this.phraseFilePath,
                   ',', Charset.forName("UTF-8"));
       }
       CsvWriter contextWriter = null;
       if (this.contexFilePath != null) {
           contextWriter = new CsvWriter(this.contexFilePath,
                   ',', Charset.forName("UTF-8"));
       }
       CsvWriter ABSOpinionWriter = null;
       if (this.ABSOpinionFilePath != null) {
    	   ABSOpinionWriter = new CsvWriter(this.ABSOpinionFilePath,
                   ',', Charset.forName("UTF-8"));
       }
       
       ArrayList<CsvWriter> opinionMatrixWriters=new ArrayList<CsvWriter>();
       if(this.opinionMatrixFolder!=null){
    	    int topicNum=model.getTopicNum();
    	    for(int i=0;i<topicNum;i++){
    	    	opinionMatrixWriters.add(new CsvWriter(this.opinionMatrixFolder+"/"+i,
                   ',', Charset.forName("UTF-8")));
    	    }
       }

       int numDocuments = 0;
       for (File file : inputDir.listFiles()) {
           if (file.isDirectory()) {
               continue;
           }
           String path;
           try {
               path = file.getCanonicalPath();
               /*
               if (!path.endsWith(".stp")) {
                   continue;
               }
               */
               // Extract and output the opinions and phrases containing those.
               ++numDocuments;
               extractor.extractOpinion(path);
               if (opinionWriter != null) {
                   opinionWriter.write(file.getName());
                   for (Double d : extractor.topicalOpinions()) {
                       opinionWriter.write(d.toString());
                   }
                   opinionWriter.endRecord();
               }
               if (phraseWriter != null) {
                   phraseWriter.write(file.getName());
                   for (String phrase : extractor.topicalPhrases()) {
                       phraseWriter.write(phrase);
                   }
                   phraseWriter.endRecord();
               }
               /*
               if (contextWriter != null) {
                   contextWriter.write(file.getName());
                   for (String context : extractor.topicalContexts()) {
                       contextWriter.write(context);
                   }
                   contextWriter.endRecord();
               }
               */
               if (ABSOpinionWriter!= null) {
            	   ABSOpinionWriter.write(file.getName());
                   for (Double d : extractor.topicalABSOpinions()) {
                       ABSOpinionWriter.write(d.toString());
                   }
                   ABSOpinionWriter.endRecord();
               }
               
               if(opinionMatrixWriters!=null){
            	   	 String[] phrases=extractor.topicalPhrases();
            	   	 
            	    for(int i=0;i<extractor.getOpinionMatrix().size();i++){
            	    	CsvWriter writer=opinionMatrixWriters.get(i);
            	    	Double[] array=extractor.getOpinionMatrix().get(i);
            	    	writer.write(file.getName());
            	    	for(Double d:array){
            	    		writer.write(d.toString());
            	    	}
            	    	writer.write("Phrase");
            	    	writer.write(phrases[i]);
            	    	writer.endRecord();
            	    }
            	   
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       
       if (opinionWriter != null) {
           opinionWriter.close();
       }
       if (phraseWriter != null) {
           phraseWriter.close();
       }
       if (contextWriter != null) {
           contextWriter.close();
       }
       
       if(ABSOpinionWriter!=null){
    	   ABSOpinionWriter.close();
       }
       
       if(opinionMatrixWriters!=null){
    	   for(CsvWriter writer:opinionMatrixWriters){
    		   writer.close();
    	   }
       }
	}
	
	public void extractOpinionFromFolder(String inputFolder, String outputFolder) throws IOException{
		String opinionMatrixFolder=outputFolder+"/opinionMatrix";
		File file=new File(opinionMatrixFolder);
		if(!file.exists()){
			file.mkdir();
		}
		
		extractOpinionFromFolder(inputFolder, outputFolder+"/context_"+getTimeStamp(),outputFolder+"/opinion_"+getTimeStamp(),outputFolder+"/phrase_"+getTimeStamp(), outputFolder+"/absOpinion_"+getTimeStamp(), opinionMatrixFolder);
	}
	
	private String getTimeStamp(){
		String[] filePaths=topicFilePath.split("/");
		String fileName=filePaths[filePaths.length-1];
		return fileName.split("_")[1]+"_"+fileName.split("_")[2];
	}
	
	
	
	
	
	
	
	
	
}
