package ch.epfl.lia.datamodel;

import java.util.ArrayList;
import java.util.HashSet;

public class Topic {
	private int id;
	private ArrayList<String> keys;
	private double param;
	
	public Topic(int id, double param, ArrayList<String> keys){
		this.id = id;
		this.keys = keys;
		this.param = param;
	}
	 
	public void setKeys(ArrayList<String> keys){
		this.keys=keys;
	}
	
	public void setID(int id){
		this.id=id;
	}
	
	public void setParam(double param){
		this.param = param;
	}
	
	public double getParam(){
		return param;
	}
	
	public ArrayList<String> getKeys(){
		return keys;
	}
	
	public int getID(){
		return id;
	}
	
	public String toString(){
		String s="";
		s = s+id;
		s = s+" "+param;
		for(String key: keys){
			s = s+" "+key;
		}
		return s;
	}
}
