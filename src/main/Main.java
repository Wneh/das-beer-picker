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
	private int RESULT_ID = 1;



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
					printToFile("prods.dat",products);
					ArrayList<Product> top =marketAnalyzer.getTopKProducts(5);
					printToFile("top.dat",top);
					ArrayList<Product> div =marketAnalyzer.getKMostDiverseProducts(marketAnalyzer.getTopKProducts(5), 3);
					printToFile("div.dat",div);
					createResultTable(connection);

					insertResult(connection, products, "all");
					insertResult(connection,top,"top");
					insertResult(connection,div,"div");

					break;
				case GET_VOTES:
					break;
				default:return;
			}
			printMenu();
		}
	}
	private void createResultTable(Connection connection){
		System.out.println("HEJ");
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS results(id SERIAL NOT NULL PRIMARY KEY, x DOUBLE PRECISION , y DOUBLE PRECISION, z DOUBLE PRECISION, type VARCHAR(40))");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created results table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void insertResult(Connection connection,ArrayList<Product> products,String name) {
		String insertString = "INSERT INTO results VALUES(?,?,?,?,?)";
		PreparedStatement insertPrefs = null;
		try {
			insertPrefs = connection.prepareStatement(insertString);
			for(Product p : products){
				insertPrefs.setInt(1,RESULT_ID);
				insertPrefs.setDouble(2,p.attributes.get(0));
				insertPrefs.setDouble(3,p.attributes.get(1));
				insertPrefs.setDouble(4,p.attributes.get(2));
				insertPrefs.setString(5, name);
				insertPrefs.executeUpdate();
				RESULT_ID++;
			}
		}catch (SQLException e){
			e.printStackTrace();
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

	public void printToFile(String file, ArrayList<Product> products){
			try {
				PrintWriter writer = new PrintWriter(file,"UTF-8");
				for(Product p: products)writer.write(p.printForGNUPlot()+"\n");
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
			ps = connection.prepareStatement("DROP TABLE prefs");
			ps.executeUpdate();
			System.out.println("Dropped table prefs");
			
			ps = connection.prepareStatement("DROP TABLE beer");
			ps.executeUpdate();
			System.out.println("Dropped table beer");
			
			ps = connection.prepareStatement("DROP TABLE votes");
			ps.executeUpdate();
			System.out.println("Dropped table votes");

			ps = connection.prepareStatement("DROP TABLE results");
			ps.executeUpdate();
			System.out.println("Dropped table results");
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
