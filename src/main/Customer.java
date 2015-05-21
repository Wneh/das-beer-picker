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

    public double[] toArray(){
        double[] arr = new double[preferences.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = preferences.get(i);
        }
        return arr;
    }
}
