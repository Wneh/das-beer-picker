package main;

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

	public double[] toArray(){
		double [] arr = new double[3];
		arr[0] = age;
		arr[1] = length;
		arr[2] = weight;
		return arr;
	}

	public int compareTo(Person other){
		if (this.cosineScore < other.cosineScore) return -1;
		else if (this.cosineScore > other.cosineScore) return 1;
		return 0;
	}

	public String toPlotString(){
		return age + " " + length + " " + weight+ "";
	}
}
