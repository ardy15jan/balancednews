package ch.epfl.lia.opinion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import Jama.util.Maths;

public class PCAAnalizer {
	private Matrix opinionMatrix;
	private ArrayList<String> docNames=new ArrayList<String>();
	private double[] pcaScore;
	private double[] singulars;
	
	public void readOpinionMatrix(String inputFilename){
		
		docNames.clear();
		ArrayList<double[]> matrix=new ArrayList<double[]>();
		double[][] result;
		
		File inputFile=new File(inputFilename);
		if(!inputFile.exists()){
			System.err.println("File not found");
			return;
		}
			
		
		try {
			Scanner scanner=new Scanner(inputFile);
			while(scanner.hasNextLine()){
				
				String line=scanner.nextLine().split(",Phrase,")[0];
				String[] doc=line.split(",");
				
				docNames.add(doc[0]);
				double[] values=new double[doc.length-1];
				for(int i=1;i<doc.length;i++){
					values[i-1]=Double.parseDouble(doc[i]);
				}
				matrix.add(values);
			}
			
		result=new double[matrix.size()][matrix.get(0).length];
		for(int i=0;i<matrix.size();i++){
			result[i]=matrix.get(i);
		}
			this.opinionMatrix=new Matrix(result);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void calculatePCAScore(){
		if(opinionMatrix==null){
			System.err.println("Please load the opinion matrix!!!");
		}
		
		 SingularValueDecomposition composition=new SingularValueDecomposition(opinionMatrix);
		 Matrix V=composition.getV();
		  
		 
		 double[][] result=opinionMatrix.times(V).getArray();
		 double magnitude=Math.sqrt(result[0].length);
		 this.pcaScore=new double[result.length];
		 this.singulars=composition.getSingularValues();
		 
		  
		 for(int i=0;i<result.length;i++){
			 pcaScore[i]=result[i][0]*magnitude;
			  
		 }
	}
	
	public void printScore(String outputFilePath){
		File file=new File(outputFilePath);
		
		try {
			if(!file.exists())
				file.createNewFile();
			
			PrintWriter pw=new PrintWriter(file);
			
			//print singulars and their contribution
			double singularSum=0.0;
			for(double d: this.singulars){
				singularSum+=d;
			}
			pw.write("Top 3 singulars: ");
			for(int i=0;i<singulars.length&&i<3;i++){
				pw.write(singulars[i]+"/"+singulars[i]/singularSum+" ");
			}
			pw.write("\n");
			
			
			//print scores
			for(int i=0;i<pcaScore.length;i++){
				pw.write(docNames.get(i));
				pw.write(",");
				pw.write(pcaScore[i]+"\n");
			}
			
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void extractOpinionFromFolder(String opinionMatrixFolder){
		File folder=new File(opinionMatrixFolder);
		File outputFolder=new File(opinionMatrixFolder+"pca/");
		
		
		if(!folder.isDirectory()){
			System.err.println("Not a valid folder.");
			return;
		}
		
		if(!outputFolder.exists()){
			outputFolder.mkdir();
		}
		
		for(File file: folder.listFiles()){
			try{
				if(!file.getName().contains("pca")){
					readOpinionMatrix(file.getCanonicalPath());
					calculatePCAScore();
					printScore(outputFolder.getCanonicalPath()+"/pca_"+file.getName());
				}
				
			}catch(Exception e){
				System.err.println("Not valid opinion matrix file "+file.getName());
			}
		}
		
		 try {
			combineAllOpinionFile(outputFolder.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void combineAllOpinionFile(String opinionMatrixFolder){
		File folder=new File(opinionMatrixFolder);
		
		if(!folder.isDirectory()){
			System.err.println("Not a valid folder.");
			return;
		}
		
		int numTopic=0;
		for(File file: folder.listFiles()){
			if(file.getName().contains("pca"))
				numTopic++;
		}
		
		//Get file name
		ArrayList<String> docName=new ArrayList<String>();
		
		for(File file:folder.listFiles()){
			if(file.getName().contains("pca")){
				try{
					Scanner scanner=new Scanner(file);
					
					//skip head
					scanner.nextLine();
					
					//get file name
					while(scanner.hasNextLine()){
						String aLine=scanner.nextLine();
						docName.add(aLine.split(",")[0]);
						
					}
				}catch(Exception e){
					
				}
				
				break;
			}
		}
		
		//Get opinions of each topic
		int numDoc=docName.size();
		double[][] combinedOpinionMatrix=new double[numTopic][numDoc];
		for(File file:folder.listFiles()){
			if(file.getName().contains("pca")){
				
				
				
				try {
					int topicID=Integer.parseInt(file.getName().split("_")[1]);
					Scanner scanner=new Scanner(file);
					
					//skip head
					scanner.nextLine();
					
					//read each line
					int lineNum=0;
					while(scanner.hasNextLine()){
						String aLine=scanner.nextLine();	 
						combinedOpinionMatrix[topicID][lineNum]=Double.parseDouble(aLine.split(",")[1]);
						lineNum++;
					}
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
		}
		
		File pcaOpinion=new File(opinionMatrixFolder+"/combined_opinion");
		if(!pcaOpinion.exists()){
			try {
				pcaOpinion.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			PrintWriter pw=new PrintWriter(pcaOpinion);
			for(int i=0;i<docName.size();i++){
				pw.write(docName.get(i));
				for(int j=0;j<combinedOpinionMatrix.length;j++){
					pw.write(",");
					pw.write(combinedOpinionMatrix[j][i]+"");
				}
				pw.write("\n");
				
			}
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	public void compareWith(String pcaOpinionFolder, String originOpinionFilePath, String opinionMatrixFolder){
		File oFile=new File(originOpinionFilePath);
		File folder=new File(pcaOpinionFolder);
		File compareFolder=new File(pcaOpinionFolder+"/compare/");
		
		if(!compareFolder.exists())
			compareFolder.mkdir();
		 
		
		if(!oFile.exists()||!folder.exists()){
			System.err.println("file or folder not found");
			return;
		}
			
		if(!folder.isDirectory()){
			System.err.println("Not a valid opinion matrix folder");
			return;
		}
		
		//create comparison file
		for(File pcaFile: folder.listFiles()){
			
			if(pcaFile.isDirectory())
				continue;
			
			if(!pcaFile.getName().contains("pca"))
				continue;
			
			try{
				
				
				File compareFile=new File(compareFolder.getCanonicalPath()+"/compare_"+pcaFile.getName());
				compareFile.createNewFile();
				
				int topicID=Integer.parseInt(pcaFile.getName().split("_")[1]);
				
				File opinionMatrixFile=new File(opinionMatrixFolder+"/"+topicID);
				
				PrintWriter pw=new PrintWriter(compareFile);
				Scanner oScanner=new Scanner(oFile);
				Scanner pScanner=new Scanner(pcaFile);
				Scanner mScanner=new Scanner(opinionMatrixFile);
				
				//skip pca opinion file head
				pScanner.nextLine();
				
				while(oScanner.hasNextLine()){
					String aLine=oScanner.nextLine();
					
					String docName=aLine.split(",")[0];
					double originOpinion=Double.parseDouble(aLine.split(",")[topicID+1]);
					double pcaOpinion=Double.parseDouble(pScanner.nextLine().split(",")[1]);
					
					String phrase;
					String[] phraseArray=mScanner.nextLine().split("Phrase,") ;
					if(phraseArray.length>1)
						phrase=phraseArray[1];
					else
						phrase="";
					
					pw.write(docName);
					pw.write(",");
					pw.write(originOpinion+"");
					pw.write(",");
					pw.write(pcaOpinion+"");
					pw.write(",Phrase,");
					pw.write(phrase);
					pw.write("\n");
					
				}
				
				pw.flush();
				
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
				
		}
		
		
		
		 
	}
	
	 
}
