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

	public ArrayList<Integer> calculateCentroid(ArrayList<Person> persons){
		ArrayList<Integer> centroid = new ArrayList<Integer>();
		int a = 0, l = 0, w = 0;
		ArrayList<Integer> tmpWeights;
		for (Person person : persons){
			tmpWeights = calculateWeightedValues(person);
			a += tmpWeights.get(0);
			l += tmpWeights.get(1);
			w += tmpWeights.get(2);
		}
		centroid.add(a/persons.size());
		centroid.add(l/persons.size());
		centroid.add(w/persons.size());
		return centroid;
	}

	public ArrayList<Integer> calculateWeightedValues(Person person){
		ArrayList<Integer> weights = new ArrayList<Integer>();

		int a = 10 - (Math.abs(IDEAL_AGE - person.age) / 5);
		int l = 10 - (Math.abs(IDEAL_LENGTH - person.length) / 6);
		int w = 10 - (Math.abs(IDEAL_WEIGHT - person.weight) / 7);

		weights.add(a);
		weights.add(l);
		weights.add(w);
		return weights;
	}


	public static void main(String[] args) {
		new Main();
	}
}
