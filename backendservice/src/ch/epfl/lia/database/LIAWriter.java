package ch.epfl.lia.database;

import java.sql.SQLException;
import java.sql.Statement;

public class LIAWriter extends DatabaseReader {

	private String tableName="articles_parsed";
	
	public void setTable(String tableName) {
		this.tableName=tableName;
	}
	
	public LIAWriter(){
		super("jdbc:mysql://liapc3.epfl.ch:3306/peshterldb?"+"user=xtang");
	}
	
	public void updateContent(String column, int id, String content){
		Statement stmt=null;
		content=content.replace("'", "\\'");
		
		String query="UPDATE "+tableName+" SET "+column+"='"+content+"' WHERE id="+id; 
		
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			
		}catch(SQLException e){
			System.err.println("wrong sql:"+ id);
			e.printStackTrace();
		}
	}
	
	public void insertID(int id){
		Statement stmt=null;
		 
		
		String query="INSERT INTO "+tableName+" (id) VALUES ('"+id+"')"; 
		
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			
		}catch(SQLException e){
			System.err.println("wrong sql:"+ id);
			e.printStackTrace();
		}
	}
}
