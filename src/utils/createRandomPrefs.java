package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class createRandomPrefs {
	
	public static final int COUNTER = 1000;
	public static final int PERSON_COUNTER = 1000;

	public static void main(String args[]){
		
		DatabaseConnection c = new DatabaseConnection();
		
		Connection connection = c.getConnection();
		
		ArrayList<ArrayList<Double>> data = createPrefs(COUNTER,3);
		
		createTable(connection);
		
		insertPrefs(connection,data);
		
		createPersons(connection);
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
			System.out.println(row);
			rows.add(row);
		}
		return rows;
	}
	
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
//				connection.commit();
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
			ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS prefs(prefs_if SERIAL NOT NULL PRIMARY KEY,pref1 double precision, pref2 double precision, pref3 double precision)");
			ps.executeUpdate();
			ps.close();
			System.out.println("Created prefs table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createPersons(Connection connection){
		createTablePersons(connection);
		String insertString = "INSERT INTO persons VALUES(?,?,?,?)";

		PreparedStatement insertPrefs = null;
		try {
			insertPrefs = connection.prepareStatement(insertString);
			for(int i = 0; i < PERSON_COUNTER; i++){
				insertPrefs.setInt(1,i);
				insertPrefs.setInt(2,randomNumber(18,65));
				insertPrefs.setInt(3,randomNumber(140,200));
				insertPrefs.setInt(4,randomNumber(50,120));
				insertPrefs.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	private static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
