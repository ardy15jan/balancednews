package ch.epfl.lia.opinion;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.io.*;
import java.util.List;

public class DocumentParser {
	private static LexicalizedParser lp=LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	private static TreebankLanguagePack tlp = new PennTreebankLanguagePack();
   private static TreePrint tp = new TreePrint("wordsAndTags,typedDependencies", tlp);
	
	
	public static void parseSingleDocument(String inputFileName, String outputFileName) throws FileNotFoundException{
		System.err.println("Parsing file "+inputFileName);
		
		PrintWriter pw=new PrintWriter(outputFileName);
		
       for (List<HasWord> sentence : new DocumentPreprocessor(inputFileName)) {
            Tree parse = lp.apply(sentence);
            tp.printTree(parse, pw);
        }
        
       System.err.println(" Done");
	}
	
	public static void parseString(String inputString, String outputFileName) throws FileNotFoundException{
		System.err.println("Parsing string");
		
		PrintWriter pw=new PrintWriter(outputFileName);
		
		StringReader sr=new StringReader(inputString);
		
       for (List<HasWord> sentence : new DocumentPreprocessor(sr)) {
            Tree parse = lp.apply(sentence);
            tp.printTree(parse, pw);
        }
        
       System.err.println(" Done");
	}

	public static void parseWebContent(String inputFolderName, String outputFolderName) throws FileNotFoundException{
		File inputFolder=new File(inputFolderName);
		File outputFolder=new File(outputFolderName);
		
		if(!inputFolder.isDirectory()||!outputFolder.isDirectory()){
			System.err.println("Not valid folder name");
			return;
		}
		
		String inputFilename;
		String outputFilename;
		
		for(File file: inputFolder.listFiles()){
			inputFilename=file.getName();
			File outputFile=new File(outputFolderName+"/"+inputFilename);
			if(outputFile.exists())
				continue;
			parseSingleDocument(inputFolderName+"/"+inputFilename,outputFolderName+"/"+inputFilename);
		}
		
	}
}
