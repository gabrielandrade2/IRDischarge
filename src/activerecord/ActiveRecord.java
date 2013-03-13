package activerecord;

import com.mysql.jdbc.Connection;

import conn.DbConnection;

public class ActiveRecord {
	
	public Connection con;  
	
	public ActiveRecord(){
		
		 con = (Connection) DbConnection.getInstance().getConnection(); 
		
	}

}
