package main;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {

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
