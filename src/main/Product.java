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
}
