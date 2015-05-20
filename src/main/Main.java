package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private final int MENU_EXIT = 0;
	private final int MENU_RUN = 1;
	private final int MENU_RESET_DATABASE = 2;
	private final int MENU_CREATE_RANDOM_DATABASE = 3;



	/**
	 * Main loop is executed from where.
	 * This is where the user makes all the decisions
	 */
	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();

		Scanner input = new Scanner(System.in);
		ArrayList<Person> persons;
		ArrayList<Prefs> prefs;
		int i = 0;
		printMenu();
		while (true){
			switch (input.nextInt()){
				case MENU_EXIT: return;
				case MENU_RUN:
					System.out.println("Running... ");
					persons = loadPersons(connection);
					prefs = loadPrefs(connection);
					ArrayList<Person> toppersons = new PreferencePersonSelection().selectPersonsByPreference(persons,prefs,3);
					ArrayList<Person> cands = new CentroidCandidateFinder().findCentroidCandidates(toppersons,10);
					ArrayList<Person> res = new DiversePersonSelection().selectMostDiversePersons(cands, 5);
					System.out.println("Writing results... ");
					printToPlot(persons,"persons.dat");
					printToPlot(toppersons,"toppersons.dat");
					printToPlot(cands,"cands.dat");
					printToPlot(res, "res.dat");
					System.out.println("Done!");
					break;
				case MENU_RESET_DATABASE:
					dropTables(connection);
					break;
				case MENU_CREATE_RANDOM_DATABASE:
					new utils.createRandomPrefs();
					break;
				default:return;
			}
			printMenu();
		}
	}
	private void printMenu(){
		System.out.println();
		System.out.println("Menu:");
		System.out.println("1: Run");
		System.out.println("2: Reset Database");
		System.out.println("3: Create Random Database");
		System.out.println("0: Exit");
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

	private void printToPlot(ArrayList<Person> persons, String file){
		try {
			PrintWriter writer = new PrintWriter(file,"UTF-8");
			for(Person p: persons)writer.write(p.toPlotString()+"\n");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void dropTables(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("DROP TABLE persons");
			ps.executeUpdate();
			System.out.println("Dropped table persons");
			ps = connection.prepareStatement("DROP TABLE prefs");
			ps.executeUpdate();
			System.out.println("Dropped table prefs");
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
