package main;

public class Prefs {
	
	public int id;
	public double pref1;
	public double pref2;
	public double pref3;
	
	public Prefs(int id, double pref1, double pref2, double pref3){
		this.id = id;
		this.pref1 = pref1;
		this.pref2 = pref2;
		this.pref3 = pref3;
	}
	
	public String toString(){
		return "id: "+id + ", pref1: " + pref1 + ", pref2: " + pref2 + ", pref3: " + pref3+"\n";
	}
}
