package main;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {
	private final int IDEAL_AGE = 25;
	private final int IDEAL_WEIGHT = 65;
	private final int IDEAL_LENGTH = 175;


	public Main(){
		Connection connection = new utils.DatabaseConnection().getConnection();
		
	}
	
	public ArrayList<Person> loadPersons(Connection connection){
//		ArrayList<>
		return null;
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
