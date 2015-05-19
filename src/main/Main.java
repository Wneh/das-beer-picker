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
		System.out.println(calculateCentroid(persons).get(0)+ " " + calculateCentroid(persons).get(1) + " " +calculateCentroid(persons).get(2));
		System.out.println(persons.get(45).toString());
		System.out.println(calculateWeightedValues(persons.get(45)).get(0) + " " + calculateWeightedValues(persons.get(45)).get(1) + " " +calculateWeightedValues(persons.get(45)).get(2));
		System.out.println(persons.get(12));
		System.out.println(persons.get(345));
		System.out.println("CosineSim: " + cosineSimilarity(persons.get(12), persons.get(345)));
		System.out.println("Euc: " + euclideanDistance(persons.get(12), persons.get(345)));
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
			a += person.age;
			l += person.length;
			w += person.weight;
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

	public Double euclideanDistance(Person person1, Person person2){
		Double dist = Math.sqrt((person1.age-person2.age)^2 + (person1.length-person2.length)^2 +(person1.weight-person2.weight)^2);
		return dist;
	}

	public Double sqrtEuclideanDistance(Person person1, Person person2){
		//maybe later
		return null;
	}

	public Double cosineSimilarity(Person person1, Person person2){
		double [] p1 = person1.toArray();
		double [] p2 = person2.toArray();
		return new CosineSimilarity().cosineSimilarity(p1, p2);
	}

	public static void main(String[] args) {
		new Main();
	}
}
