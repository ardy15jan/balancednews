package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseReader {
	
	protected String connectionURL;
	protected static Connection conn;
	
	public DatabaseReader(String url){
		this.connectionURL=url;
		
	}
	
	public void connect() {
           
                try {
                     if(conn == null || conn.isClosed()){
                         Class.forName("com.mysql.jdbc.Driver");
                         this.conn=DriverManager.getConnection(connectionURL);
                     }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
                }	
	}
        
        public Connection getConnection(){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection(connectionURL);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return null;
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
