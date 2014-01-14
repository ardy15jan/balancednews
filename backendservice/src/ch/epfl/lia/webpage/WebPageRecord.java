package ch.epfl.lia.webpage;

import java.sql.Date;

import ch.epfl.lia.database.LIAReader;

public class WebPageRecord extends WebPage {
	 
	
	public String toString(){
		String result="";
		result+=id;
		result+=feed_id;
		result+=url;
		result+=title;
		return result;
		
	}
	
	 
}
