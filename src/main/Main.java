package main;

import utils.createRandomPrefs;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class Main {

	private final int MENU_EXIT = 0;
	private final int MENU_INITIALIZE = 1;
	private final int MENU_RUN = 2;
	private int RESULT_ID = 1;



	/**
	 * Main loop is executed from where.
	 * This is where the user makes all the decisions
	 */
	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();

		Scanner input = new Scanner(System.in);
		int i = 0;
		printMenu();
		while (true){
			switch (input.nextInt()){
				case MENU_EXIT: return;
				case MENU_RUN:
					//==LOAD DATA
					ArrayList<Product> products = loadProducts(connection);
					int s = loadVotes(connection);
					if(s < 1){
						System.out.println("No votes found, exiting");
						return;
					}
					ArrayList<Customer> customers = loadCustomers(connection);

					//==CREATE MARKET FROM DATA
					MarketAnalyzer marketAnalyzer = new MarketAnalyzer(new ProductGroup(products),new CustomerGroup(customers));

					//==RUN MAIN ALGORITHM

					//Top k products by Customer preferences
					int k = 8;
					ArrayList<Product> topKProducts = marketAnalyzer.getTopKProducts(k);
					System.out.println("Top K Products: \n" + topKProducts);


					//Get r most diverse products
					int r = 3;
					ArrayList<Product> mostDiverseProducts = marketAnalyzer.getKMostDiverseProducts(topKProducts,r);
					System.out.println("Most diverse products: "+mostDiverseProducts);

					//print results to file
					printToFile("prods.dat",products);
					printToFile("top.dat",topKProducts);
					printToFile("div.dat",mostDiverseProducts);

					//send to database
					insertResult(connection, products, "all");
					insertResult(connection, topKProducts,"top");
					insertResult(connection, mostDiverseProducts,"div");

					break;
				case MENU_INITIALIZE:
					dropTables(connection);
					dropResultTable(connection);
					createResultTable(connection);
					new createRandomPrefs();
					System.out.println();
					System.out.println("WARNING! Wait for votes");
					break;
				default:return;
			}
			printMenu();
		}
	}

	private void dropResultTable(Connection connection) {
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("DROP TABLE results");
			ps.executeUpdate();
			System.out.println("Dropped table results");

			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createResultTable(Connection connection){
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
		System.out.println("1: Initialize");
		System.out.println("2: Run");
		System.out.println("0: Exit");
	}

	/**
	 * Loads all preferences from the database and return it in
	 * an arraylist
	 *
	 * @param connection
	 * @return
	 */
	public int loadVotes(Connection connection){
		ArrayList<VotingFactory> result = new ArrayList<VotingFactory>();
		String getStatement = "select votes.name, avg(beska) as beska, avg(sotma) as sotma, avg(fyllighet) as fyllighet from votes, beer where beer.beer_id = votes.beer_id GROUP by votes.name";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getStatement);
			while(rs.next()){
				VotingFactory vote = new VotingFactory(rs.getDouble("beska"),rs.getDouble("sotma"),rs.getDouble("fyllighet"));
				result.add(vote);
				System.out.println("Vote: "+vote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String insertStatement = "INSERT INTO prefs(prefs_id,pref1, pref2, pref3) VALUES(DEFAULT,?,?,?)";
		PreparedStatement prepared = null;

		try {
			prepared = connection.prepareStatement(insertStatement);
			for (VotingFactory votingFactory : result) {
				prepared.setDouble(1, votingFactory.beska);
				prepared.setDouble(2, votingFactory.sotma);
				prepared.setDouble(3, votingFactory.fyllighet);
				prepared.executeUpdate();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result.size();
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
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void oldStuff(){
		/*
		//Reverse Top k Customers for each product
		HashMap<Product,ArrayList<Customer>> reverseTopKProductMap = new HashMap<Product, ArrayList<Customer>>();
		HashMap<Customer,ArrayList<Product>> reverseTopKCustomerMap = new HashMap<Customer, ArrayList<Product>>();
		for (Product p : topKProducts){
			ArrayList<Customer> reverseTopKCustomers = marketAnalyzer.getTopKCustomerCentroidCandidates(p,2);
			reverseTopKProductMap.put(p,reverseTopKCustomers);
			for (Customer c: reverseTopKCustomers){	//create a hashmap that connects customers to products
				if(!reverseTopKCustomerMap.containsKey(c))reverseTopKCustomerMap.put(c,new ArrayList<Product>());
				reverseTopKCustomerMap.get(c).add(p);
			}
		}
		//Top r diverse products by customer


					ArrayList<Customer> reverseCustomers = new ArrayList<Customer>();
					for (Product p : topKProducts){ //get customers for all top k products
						for (Customer c : reverseTopKProductMap.get(p)){
							if(!reverseCustomers.contains(c)){
								reverseCustomers.add(c);
							}
						}
					}
					//get the most diverse customers by their preferences
					ArrayList<Customer> mostDiverseCustomers = marketAnalyzer.getKMostDiverseCustomers(reverseCustomer,r);

					//add products from the most diverse customers
					ArrayList<Product> mostDiverseProducts = new ArrayList<Product>();
					for(Customer c : mostDiverseCustomers){
						for (Product p : reverseTopKCustomerMap.get(c))mostDiverseProducts.add(p);
					}

					*/
	}




}
