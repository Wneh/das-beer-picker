package main;

public class Person {
	
	public int id;
	public int age;
	public int length;
	public int weight;

	public Person(int id, int age, int length, int weight){
		this.id = id;
		this.age = age;
		this.length = length;
		this.weight = weight;
	}
	
	public String toString(){
		return "id: " + this.id + ", age: " + this.age + ", length: " + this.length + ", weight: " + this.weight;
	}

	public double[] toArray(){
		double [] arr = new double[3];
		arr[0] = age;
		arr[1] = length;
		arr[2] = weight;
		return arr;
	}
}
