package ch.epfl.lia.others;

import java.util.ArrayList;

import ch.epfl.lia.database.LIAReader;
import ch.epfl.lia.database.LocalConnector;
import ch.epfl.lia.webpage.WebPageRecord;

public class UpdateHTML {

	public static void updateHTML(){
		LIAReader reader=new LIAReader();
		reader.connect();
		
		LocalConnector localConnector=new LocalConnector();
		localConnector.connect();
		
		ArrayList<Integer> idList = localConnector.getIDs();
		for(Integer id: idList){
			String htmlContent = reader.getHTMLContent(id).replace("'", "\\'");
			localConnector.updateHTML(id, htmlContent);
			System.err.println("finish "+id);
		}
		
	}
}
