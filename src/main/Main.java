package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

	/**
	 * Main loop is executed from where.
	 * This is where the user makes all the decisions
	 */
	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();
		ArrayList<Person> persons = loadPersons(connection);
		//System.out.println(persons);
		ArrayList<Prefs> prefs = loadPrefs(connection);
		//System.out.println(prefs);
		double sum1 = 0, sum2 = 0, sum3 = 0;
		for(Prefs p: prefs){
			sum1+=p.pref1;
			sum2+=p.pref2;
			sum3+=p.pref3;
		}
		System.out.println(sum1+" "+sum2+" "+sum3);

		ArrayList<Person> toppersons = new PreferencePersonSelection().selectPersonsByPreference(persons,prefs,3);
		System.out.println("Toppersons size: "+toppersons.size());
		System.out.println(toppersons);
		ArrayList<Person> cands = new CentroidCandidateFinder().findCentroidCandidates(toppersons,20);
		System.out.println("Candidates: "+cands);
		ArrayList<Person> res = new DiversePersonSelection().selectMostDiversePersons(cands,5);
		System.out.println("Res: "+res);


		for(Person p : persons) System.out.println(p.toPlotString());
		System.out.println();
		for(Person p : toppersons) System.out.println(p.toPlotString());
		System.out.println();
		for(Person p : cands) System.out.println(p.toPlotString());
		System.out.println();
		for(Person p : res) System.out.println(p.toPlotString());

	}
	
	/**
	 * Loads all preferences from the database and return it in
	 * an arraylist
	 * 
	 * @param connection
	 * @return
	 */
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
	
	/**
	 * Loads all people from the the database and return it in a
	 * arraylist of Person objects
	 * 
	 * @param connection
	 * @return
	 */
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
	
	/**
	 * Get this party started
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}
