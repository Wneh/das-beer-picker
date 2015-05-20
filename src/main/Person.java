package main;

/**
 * Person class that represent the data stored in the
 * persons table in the database.
 * 
 * Also implements comparable based on the cosine similarity
 * The cosine score is calculated with the CosineSimilarity class
 */
public class Person implements Comparable<Person> {

	public int id;
	public int age;
	public double length;
	public double weight;
	public double cosineScore;

	public Person(int id, int age, int length, int weight){
		this.id = id;
		this.age = age;
		this.length = length;
		this.weight = weight;
		this.cosineScore = -1;
	}
	
	public String toString(){
		return "id: " + this.id + ", age: " + this.age + ", length: " + this.length + ", weight: " + this.weight +", cosineScore: "+ this.cosineScore + "\n";
	}

	/**
	 * Creates and double[] array with the following scheme:
	 * 		Index 0: age
	 * 		Index 1: length
	 * 		Index 2: weight
	 * 
	 * @return double[] array with age, length and weight
	 */
	public double[] toArray(){
		double [] arr = new double[3];
		arr[0] = age;
		arr[1] = length;
		arr[2] = weight;
		return arr;
	}

	/**
	 * Compares two Person object based on the cosine score.
	 */
	public int compareTo(Person other){
		if (this.cosineScore < other.cosineScore) return -1;
		else if (this.cosineScore > other.cosineScore) return 1;
		return 0;
	}
}
