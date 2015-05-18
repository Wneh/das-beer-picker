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
		ArrayList<Person> persons = loadPersons(connection);
		System.out.println(persons);
		ArrayList<Prefs> prefs = loadPrefs(connection);
		System.out.println(prefs);
		
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

	public ArrayList<Integer> calculateCentroid(ArrayList<Person> persons){
		ArrayList<Integer> centroid = new ArrayList<Integer>();
		int a = 0, l = 0, w = 0;
		ArrayList<Integer> tmpWeights;
		for (Person person : persons){
			tmpWeights = calculateWeightedValues(person);
			a += tmpWeights.get(0);
			l += tmpWeights.get(1);
			w += tmpWeights.get(2);
		}
		centroid.add(a/persons.size());
		centroid.add(l/persons.size());
		centroid.add(w/persons.size());
		return centroid;
	}

	public ArrayList<Integer> calculateWeightedValues(Person person){
		ArrayList<Integer> weights = new ArrayList<Integer>();

		int a = 10 - (Math.abs(IDEAL_AGE - person.age) / 5);
		int l = 10 - (Math.abs(IDEAL_LENGTH - person.length) / 6);
		int w = 10 - (Math.abs(IDEAL_WEIGHT - person.weight) / 7);

		weights.add(a);
		weights.add(l);
		weights.add(w);
		return weights;
	}


	public static void main(String[] args) {
		new Main();
	}
}
