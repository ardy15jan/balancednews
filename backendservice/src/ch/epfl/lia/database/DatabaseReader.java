package ch.epfl.lia.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseReader {
	
	protected String connectionURL;
	protected Connection conn;
	
	public DatabaseReader(String url){
		this.connectionURL=url;
		
	}
	
	public void connect(){
		try {
			this.conn=DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close(){
		
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 

}
