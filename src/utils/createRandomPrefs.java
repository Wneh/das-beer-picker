package utils;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class createRandomPrefs {

	public static void main(String args[]){
		
		DatabaseConnection c = new DatabaseConnection();
		
		Connection connection = c.getConnection();
		
		createPrefs(3,3);
		
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
			System.out.println("max: " + max);
			System.out.println("min: " + min);
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
	
	private static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
