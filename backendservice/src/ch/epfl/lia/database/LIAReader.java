package ch.epfl.lia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ch.epfl.lia.webpage.WebPageRecord;

public class LIAReader extends DatabaseReader {

	public LIAReader() {
		super("jdbc:mysql://liapc3.epfl.ch:3306/peshterldb?"+"user=xtang");
		//super("jdbc:mysql://localhost/lia?user=root&password=1d2r3m4f5s");
		// TODO Auto-generated constructor stub
	}
	
	public LIAReader(String connectionStr){
		super(connectionStr);
	}
	
	public WebPageRecord read(int row){
		Statement stmt=null;
		String query="SELECT id, feed_id, url, title, created_at, updated_at FROM articles "+" LIMIT "+(row-1)+" , "+1;
		WebPageRecord page=new WebPageRecord();
		
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			page.id=rs.getInt("id");
			page.feed_id=rs.getInt("feed_id");
			page.url=rs.getString("url");
			page.title=rs.getString("title");
			page.created_at=rs.getDate("created_at");
			page.updated_at=rs.getDate("updated_at");
			
			return page;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public String getHTML(int row){
		Statement stmt=null;
		String query="SELECT html FROM articles "+" LIMIT "+(row-1)+" , "+1;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getString("html");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getHTMLContent(int id){
		Statement stmt=null;
		String query="SELECT content FROM articles where id="+id;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getString("content");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getURL(int row){
		Statement stmt=null;
		String query="SELECT html FROM articles "+" LIMIT "+(row-1)+" , "+1;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getString("url");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public int getID(int row){
		Statement stmt=null;
		String query="SELECT id FROM articles "+" LIMIT "+(row-1)+" , "+1;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getInt("id");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public String getTitle(int row){
		Statement stmt=null;
		String query="SELECT title FROM articles "+" LIMIT "+(row-1)+" , "+1;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getString("title");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContent(int row){
		Statement stmt=null;
		String query="SELECT content FROM articles "+" LIMIT "+(row-1)+" , "+1;
		 
		try{
			 
			stmt=conn.createStatement();
			//System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			rs.next();
			
			
			return rs.getString("content");
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteHTML(int start, int end){
		if(this.connectionURL.equals("jdbc:mysql://liapc3.epfl.ch:3306/peshterldb?"+"user=xtang")){
			return;
		}
		
		for(int i=start;i<=end;i++){
			Statement stmt=null;
			String query="update articles set html='empty' where id="+i;
			 
			try{
				 
				stmt=conn.createStatement();
				//System.out.println(query);
				stmt.executeUpdate(query);
				System.out.println(i);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		 
	}

	
}
