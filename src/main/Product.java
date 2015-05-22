package main;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-21.
 */
public class Product {
    public int id;
    public String name;
    public ArrayList<Double> attributes;
    public ArrayList<Double> weightedAttributes;


    public Product(int id, String name, ArrayList<Double> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.weightedAttributes = getWeightedAttributesList(this);
    }

    public double[] toArray(){
        double[] arr = new double[attributes.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = attributes.get(i);
        }
        return arr;
    }

    private ArrayList<Double> getWeightedAttributesList(Product product){
        ArrayList<Double> doubles = new ArrayList<Double>();
        double sum = 0;
        for (int i = 0; i < product.attributes.size(); i++) {
            sum += product.attributes.get(i);
        }
        for (int i = 0; i < product.attributes.size(); i++) {
            doubles.add(product.attributes.get(i)/sum);
        }
        return doubles;
    }


    public Product(){
        this.id = -1;
        this.name = "";
        ArrayList<Double> doubles = new ArrayList<Double>();
        doubles.add(0.0);
        doubles.add(0.0);
        doubles.add(0.0);
    }

    public String printForGNUPlot(){
        StringBuilder sb = new StringBuilder();
        for(double d: attributes){
            sb.append(d+" ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(double d: attributes)sb.append(d+", ");
        return "id: " +id+", name: "+name+", Att: "+sb.toString()+ "\n";
    }
}
