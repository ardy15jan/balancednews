package ch.epfl.lia.webpage;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.*;


public class WebPage {

	public int id;
	public int feed_id;
	public String url;
	public String title;
	public String html;
	public String content;
	public Date created_at;
	public Date updated_at;
	public URL webURL;
	
	public WebPage(String url){
		this.url=url;
		try {
			this.webURL=new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WebPage(URL webURL){
		this.webURL=webURL;
		this.url=webURL.toString();
	}
	 
	
	protected WebPage(){
		
	}
	 
	
	public String getContent() throws MalformedURLException, BoilerpipeProcessingException{
		URL webURL=new URL(url);
		content=ArticleExtractor.INSTANCE.getText(webURL);
		return content;
	}
	
	public static String getContent(String html) throws MalformedURLException, BoilerpipeProcessingException{
		String result=ArticleExtractor.INSTANCE.getText(html);
		/*
		if(result.equals("")){
			
			return html;
		}
		*/
		return result;
	}
}
