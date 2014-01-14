package ch.epfl.lia.opinion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CompareOpinion {

	public static void compareTwoOpinion(String opinionFile1, String opinionFile2, String outputFilePath){
		File opinion1=new File(opinionFile1);
		File opinion2=new File(opinionFile2);
		
		File outputFile=new File(outputFilePath);
		if(!outputFile.exists())
			try {
				outputFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		try {
			Scanner scan1=new Scanner(opinion1);
			Scanner scan2=new Scanner(opinion2);
			PrintWriter pw=new PrintWriter(outputFile);
			
			while(scan1.hasNextLine()){
				String str1=scan1.nextLine();
				String str2=scan2.nextLine();
				double val1=Double.parseDouble(str1.split(",")[1]);
				double val2=Double.parseDouble(str2.split(",")[1]);
				
				if(val1==0.0&&val2==0.0)
					continue;
				
				pw.write(str1.split(",")[0]);
				pw.write(",	");
				pw.write(str1.split(",")[1]);
				pw.write(",	");
				pw.write(str2.split(",")[1]);
				pw.write("\n");
				
			}
			
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
