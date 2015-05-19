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
		ArrayList<Person> toppersons = new PreferencePersonSelection().selectPersonsByPreference(persons,prefs,3);
		System.out.println("Toppersons size: "+toppersons.size());
		System.out.println(toppersons);
		ArrayList<Person> cands = new CentroidCandidateFinder().findCentroidCandidates(toppersons,20);
		System.out.println("Candidates: "+cands);
		ArrayList<Person> res = new DiversePersonSelection().selectMostDiversePersons(cands,5);
		System.out.println("Res: "+res);



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

	public void testing(){
		ArrayList<Person> persons = new ArrayList<Person>();
		//Person p1 = new Person(0,)
	}





	public static void main(String[] args) {
		new Main();
	}
}
