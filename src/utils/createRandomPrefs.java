package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import main.Product;

/**
 * External program that generates some random test data
 * and insert it into the database
 */
public class createRandomPrefs {
	
	public static final int COUNTER = 100;
	public static final int PERSON_COUNTER = 100;
	
	public static final int PERSON_MIN_AGE = 18;
	public static final int PERSON_MAX_AGE = 65;
	public static final int PERSON_MIN_LENGTH = 140;
	public static final int PERSON_MAX_LENGTH = 200;
	public static final int PERSON_MIN_WEIGHT = 50;
	public static final int PERSON_MAX_WEIGHT = 120;

	public createRandomPrefs(){
		DatabaseConnection c = new DatabaseConnection();

		Connection connection = c.getConnection();

		ArrayList<ArrayList<Double>> data = createPrefs(COUNTER,3);

		createTable(connection);

		//insertPrefs(connection,data);

		//createPersons(connection);
		
		createTableBeer(connection);

		int beers =  fillBeerTable(connection);
		
		createTableVote(connection);

		createBeerVotess(connection, beers);
	}
	
	/**
	 * Create a matrix of ArrayList containing doubles 
	 * were each row is the sum of 1
	 * 
	 * @param n number of rows
	 * @param k number of columns
	 * @return n*k matrix of ArrayList<ArrayList<Double>>
	 */
	public static ArrayList<ArrayList<Double>> createPrefs(int n, int k){
		ArrayList<ArrayList<Double>> rows = new ArrayList<ArrayList<Double>>(n);
			
		for(int j = 0; j < n; j++){
			ArrayList<Double> row = new ArrayList<Double>(k);
			int rowSum = 0;
			int max = 100;
			int min = 0;
			for(int i = 0; i < k - 1; i++){
				int newNumber = randomNumber(min,max);
				double temp = (double)newNumber / 100;
				row.add(temp);
				max -= newNumber;
			}
			
			row.add((double)(max-min)/100);
			
			//check so the sum is 1
			double sum = 0;
			for(double pref : row){
				sum += pref;
			}
			if(sum != 1.0){
				System.err.println("SUM IS NOT 1 FOR ROW");
				System.err.println(row);
			}
			long seed = System.nanoTime();
			Collections.shuffle(row, new Random(seed));
			rows.add(row);
			
		}
		return rows;
	}
	
	/**
	 * Inserts preferences into the database table prefs
	 * 
	 * @param connection database connection
	 * @param data Preferences that is about to be inserted
	 */
	private static void insertPrefs(Connection connection, ArrayList<ArrayList<Double>> data){
		String insertString = "INSERT INTO prefs VALUES(?,?,?,?)";
		
		PreparedStatement insertPrefs = null;
		try {
			insertPrefs = connection.prepareStatement(insertString);
			int counter = 0;
			for(ArrayList<Double> row : data){
				insertPrefs.setInt(1,counter);
				insertPrefs.setDouble(2,row.get(0));
				insertPrefs.setDouble(3,row.get(1));
				insertPrefs.setDouble(4,row.get(2));
				insertPrefs.executeUpdate();
				counter++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a database table for inserting the preference data
	 * 
	 * @param connection
	 */
	private static void createTable(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS prefs(prefs_id SERIAL NOT NULL PRIMARY KEY,pref1 double precision, pref2 double precision, pref3 double precision)");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created prefs table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Creates the person table in the database
	 * It only creates if no table with the name already exists
	 * 
	 * @param connection
	 */
	private static void createTablePersons(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS persons(persons_id SERIAL NOT NULL PRIMARY KEY,age int, length int, weight int)");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created prefs table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the person table in the database
	 * It only creates if no table with the name already exists
	 * 
	 * @param connection
	 */
	private static void createTableBeer(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS beer(beer_id SERIAL NOT NULL PRIMARY KEY,name varchar(250), beska double precision, fyllighet double precision, sotma double precision, price double precision)");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created beer table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the person table in the database
	 * It only creates if no table with the name already exists
	 * 
	 * @param connection
	 */
	private static void createTableVote(Connection connection){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS votes(beer_id SERIAL NOT NULL,name varchar(250) NOT NULL)");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created vote table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int fillBeerTable(Connection connection){
		ArrayList<Product> toInsert = new ArrayList<Product>();
		
		toInsert.add(new Product(1,"Sofiero Original", new ArrayList<Double>(Arrays.asList(0.3, 0.4, 0.1,30.0))));
		toInsert.add(new Product(2,"Mariestads Export", new ArrayList<Double>(Arrays.asList(0.5, 0.5, 0.1,43.33))));
		toInsert.add(new Product(3,"Norrlands Guld Export", new ArrayList<Double>(Arrays.asList(0.4, 0.44, 0.15,27.0))));
		toInsert.add(new Product(4,"Falcon Export", new ArrayList<Double>(Arrays.asList(0.37, 0.37, 0.05,30.0))));
		toInsert.add(new Product(5,"Fem komma tvåan", new ArrayList<Double>(Arrays.asList(0.45, 0.45, 0.2,25.46))));
		toInsert.add(new Product(6,"Stockholm Festival", new ArrayList<Double>(Arrays.asList(0.43, 0.4, 0.05,23.40))));
		toInsert.add(new Product(7,"Pripps Blå", new ArrayList<Double>(Arrays.asList(0.54, 0.51, 0.17,33.03))));
		toInsert.add(new Product(8,"Kung", new ArrayList<Double>(Arrays.asList(0.55, 0.55, 0.15,22.20))));
		toInsert.add(new Product(9,"Sofiero Guld", new ArrayList<Double>(Arrays.asList(0.6, 0.7, 0.1,29.80))));
		toInsert.add(new Product(10,"Smålands", new ArrayList<Double>(Arrays.asList(0.5, 0.5, 0.05,27.88))));
		toInsert.add(new Product(11, "Brooklyn Lager", new ArrayList<Double>(Arrays.asList(0.7, 0.7, 0.1,50.2))));
		toInsert.add(new Product(12, "Stella Artois", new ArrayList<Double>(Arrays.asList(0.4, 0.4, 0.1, 67.2))));
		toInsert.add(new Product(13, "Hoegaarden", new ArrayList<Double>(Arrays.asList(0.1, 0.4, 0.1, 77.2))));
		toInsert.add(new Product(14, "Leffe Blonde", new ArrayList<Double>(Arrays.asList(0.4, 0.7, 0.4, 77.2))));
		toInsert.add(new Product(15, "Estrella Damm", new ArrayList<Double>(Arrays.asList(0.3, 0.3, 0.1, 77.2))));
		toInsert.add(new Product(16, "Erdinger", new ArrayList<Double>(Arrays.asList(0.2, 0.6, 0.1, 77.2))));


		String insertString = "INSERT INTO beer VALUES(?,?,?,?,?,?)";

		PreparedStatement insertPrefs = null;
		try {
			insertPrefs = connection.prepareStatement(insertString);
			for(Product p : toInsert){
				insertPrefs.setInt(1,p.id);
				insertPrefs.setString(2,p.name);
				insertPrefs.setDouble(3,p.attributes.get(0));
				insertPrefs.setDouble(4,p.attributes.get(1));
				insertPrefs.setDouble(5,p.attributes.get(2));
				insertPrefs.setDouble(6,p.attributes.get(3));
				insertPrefs.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toInsert.size();
	}

	private static void createBeerVotess(Connection connection, int beers) {
		int votes = beers;

		String insertStatement = "INSERT INTO votes(beer_id, name) VALUES(?,?)";

		PreparedStatement insertPrefs = null;
		try {
			insertPrefs = connection.prepareStatement(insertStatement);
			for(int i = 0; i < 10; i++) {
				int numberOfVotes = randomNumber(1,5);
				HashSet<Integer> voted = new HashSet<Integer>();
				for(int j = 0; j < numberOfVotes; j++) {
					int beerId = randomNumber(1, beers);
					if(!voted.contains(beerId)){
						insertPrefs.setInt(1,beerId);
						insertPrefs.setString(2, "voter" + i);
						insertPrefs.executeUpdate();
						voted.add(beerId);
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}



	private static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
