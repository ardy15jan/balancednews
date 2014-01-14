package ch.epfl.lia.datamodel;

public class NewsOpinion {
 
	public int newsID;
	public int topicID;
	public double opinionValue;
	public double relevance;
	public String phrases;
	
	public NewsOpinion(){
	}
	
	public NewsOpinion(int newsID, int topicID, double opinionValue, double relevance, String phrases){
		this.newsID=newsID;
		this.topicID=topicID;
		this.opinionValue=opinionValue;
		this.relevance=relevance;
		this.phrases=phrases;
	}
 
}
