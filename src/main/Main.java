package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
	private final int IDEAL_AGE = 25;
	private final int IDEAL_WEIGHT = 65;
	private final int IDEAL_LENGTH = 175;


	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();
		loadPersons(connection);
	}
	
	public ArrayList<Person> loadPersons(Connection connection){
		ArrayList<Person> result = new ArrayList<Person>();
		
		String getStatement = "SELECT * FROM persons;";
		
		try {
//			PreparedStatement ps = connection.prepareStatement(getStatement);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getStatement);
			while(rs.next()){
				Person p = new Person(rs.getInt("persons_id"),rs.getInt("age"),rs.getInt("length"),rs.getInt("weight"));
				System.out.println(p);
				result.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
