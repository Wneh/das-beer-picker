package utils;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
	
	private Connection connection = null;

	public DatabaseConnection(){
		// Do nothing at the moment
	}
	
	public Connection getConnection(){
		/*
		 * Load the postgresql drivers
		 */
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return null;
		}

		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/mdb","postgres","root");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
		
		if(connection != null){
			System.out.println("You made it, take control your database now!");
		} 
		else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	}
}
