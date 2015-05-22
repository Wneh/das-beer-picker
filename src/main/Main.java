package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

	private final int MENU_EXIT = 0;
	private final int MENU_RUN = 1;
	private final int MENU_RESET_DATABASE = 2;
	private final int MENU_CREATE_RANDOM_DATABASE = 3;
	private final int MENU_RUN_NEW = 4;
	private final int GET_VOTES = 5;



	/**
	 * Main loop is executed from where.
	 * This is where the user makes all the decisions
	 */
	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();

		Scanner input = new Scanner(System.in);
		ArrayList<Prefs> prefs;
		int i = 0;
		printMenu();
		while (true){
			switch (input.nextInt()){
				case MENU_EXIT: return;
				case MENU_RUN:
					System.out.println("Obsolete! go home");
					break;
				case MENU_RESET_DATABASE:
					dropTables(connection);
					break;
				case MENU_CREATE_RANDOM_DATABASE:
					new utils.createRandomPrefs();
					break;
				case MENU_RUN_NEW:
					ArrayList<Product> products = loadProducts(connection);
					ArrayList<Customer> customers = loadCustomers(connection);
					MarketAnalyzer marketAnalyzer = new MarketAnalyzer(new ProductGroup(products),new CustomerGroup(customers));
					System.out.println(products);
					for(Customer c : customers){
						System.out.println(c);
						System.out.println(marketAnalyzer.topP(c));
						System.out.println("=================================");
					}

					//System.out.println(marketAnalyzer.getKMostDiverseProducts(3));
					//System.out.println(marketAnalyzer.getTopKCustomerCentroidCandidates(marketAnalyzer.productGroup.products.get(1),5));
					break;
				case GET_VOTES:
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
		System.out.println("4: Run new");
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

	public ArrayList<Product> loadProducts(Connection connection){
		ArrayList<Product> result = new ArrayList<Product>();
		String getStatement = "SELECT * FROM beer;";

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getStatement);
			while(rs.next()){
				Product p = new Product(rs.getInt("beer_id"),rs.getString("name"), new ArrayList<Double>(Arrays.asList(rs.getDouble("beska"),rs.getDouble("fyllighet"),rs.getDouble("sotma"))));
				result.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	public ArrayList<Customer> loadCustomers(Connection connection){
		ArrayList<Prefs> result = new ArrayList<Prefs>();
		ArrayList<Customer> customers = new ArrayList<Customer>();
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

		for(Prefs p : result){
			ArrayList<Double> preference = new ArrayList<Double>();
			preference.add(p.pref1);
			preference.add(p.pref2);
			preference.add(p.pref3);
			customers.add(new Customer(preference,p.id,p.id+""));
		}
		return customers;
	}
	

	
	/**
	 * Get this party started
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}



	public void dropTables(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("DROP TABLE prefs");
			ps.executeUpdate();
			System.out.println("Dropped table prefs");
			
			ps = connection.prepareStatement("DROP TABLE beer");
			ps.executeUpdate();
			System.out.println("Dropped table beer");
			
			ps = connection.prepareStatement("DROP TABLE votes");
			ps.executeUpdate();
			System.out.println("Dropped table votes");
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
