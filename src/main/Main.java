package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {



	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();
		ArrayList<Person> persons = loadPersons(connection);
		//System.out.println(persons);
		ArrayList<Prefs> prefs = loadPrefs(connection);
		//System.out.println(prefs);
		System.out.println();
		System.out.println(new CentroidCandidateFinder().findCentroidCandidates(persons,20));
		System.out.println(new DiversePersonSelection().selectMostDiversePersons(new CentroidCandidateFinder().findCentroidCandidates(persons, 20),5));
	}
	
	public ArrayList<Prefs> loadPrefs(Connection connection){
		ArrayList<Prefs> result = new ArrayList<Prefs>();
		String getStatement = "SELECT * FROM prefs;";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getStatement);
			while(rs.next()){
				Prefs p = new Prefs(rs.getInt("prefs_id"),rs.getDouble("pref1"),rs.getDouble("pref2"),rs.getDouble("pref3"));
				result.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return result;
	}
	
	public ArrayList<Person> loadPersons(Connection connection){
		ArrayList<Person> result = new ArrayList<Person>();
		String getStatement = "SELECT * FROM persons;";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getStatement);
			while(rs.next()){
				Person p = new Person(rs.getInt("persons_id"),rs.getInt("age"),rs.getInt("length"),rs.getInt("weight"));
				result.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return result;
	}





	public static void main(String[] args) {
		new Main();
	}
}
