package main;

import java.util.ArrayList;

public class Prefs {
	
	public int id;
	public double pref1;
	public double pref2;
	public double pref3;
	public double pref4;
	
	public Prefs(int id, double pref1, double pref2, double pref3, double pref4){
		this.id = id;
		this.pref1 = pref1;
		this.pref2 = pref2;
		this.pref3 = pref3;
		this.pref4 = pref4;
	}
	
	public String toString(){
		return "id: "+id + ", pref1: " + pref1 + ", pref2: " + pref2 + ", pref3: " + pref3+"pref4: "+pref4+"\n";
	}

	public ArrayList<Double> toArrayList(){
		ArrayList<Double> arr = new ArrayList<Double>();
		arr.add(pref1);
		arr.add(pref2);
		arr.add(pref3);
		arr.add(pref4);
		return arr;
	}
}
