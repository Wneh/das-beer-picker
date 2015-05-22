package main;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-21.
 */
public class Product {
    public int id;
    public String name;
    public ArrayList<Double> attributes;

    public Product(int id, String name, ArrayList<Double> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }

    public double[] toArray(){
        double[] arr = new double[attributes.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = attributes.get(i);
        }
        return arr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(double d: attributes)sb.append(d+", ");
        return "id: " +id+", name: "+name+", Att: "+sb.toString()+ "\n";
    }
}
