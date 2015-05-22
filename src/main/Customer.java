package main;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-21.
 */
public class Customer {
    ArrayList<Double> preferences;
    int id;
    String name;

    public Customer(ArrayList<Double> preferences, int id, String name) {
        this.preferences = preferences;
        this.id = id;
        this.name = name;
    }

    public Customer(){
        this.id = -1;
        this.name = "name";
        this.preferences = new ArrayList<Double>();
    }

    public double[] toArray(){
        double[] arr = new double[preferences.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = preferences.get(i);
        }
        return arr;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(double d: preferences)sb.append(d+", ");
        return "id: " +id+", name: "+name+", Att: "+sb.toString()+ "\n";
    }

}
