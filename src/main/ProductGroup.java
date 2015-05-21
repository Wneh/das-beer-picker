package main;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Erik on 2015-05-21.
 */
public class ProductGroup {
    public ArrayList<Product> products;
    public ProductGroupInformation productGroupInformation;
    private final int WEIGHT_SCALE = 100;

    public ProductGroup(ArrayList<Product> products){
        this.products = products;
        this.productGroupInformation = new ProductGroupInformation(products);
    }


    public double getMaxAttributeValue(int i){
        return productGroupInformation.getMaxAttributeValue(i);
    }
    public double getMinAttributeValue(int i){
        return productGroupInformation.getMinAttributeValue(i);
    }
    public double getAvgAttributeValue(int i){
        return productGroupInformation.getAvgAttributeValue(i);
    }

    private class ProductGroupInformation{
        int nrOfProductAttributes;
        double [] maxValues;
        double [] minValues;
        double [] avgValues;
        ProductGroupInformation(ArrayList<Product> products){
            if(!products.isEmpty()){
                nrOfProductAttributes = products.get(0).attributes.size();
                maxValues = new double[nrOfProductAttributes];
                minValues = new double[nrOfProductAttributes];
                avgValues = new double[nrOfProductAttributes];

                for (Product p : products) {
                    for (int i = 0; i < p.attributes.size(); i++) {
                        double attr = p.attributes.get(i);
                        if(maxValues[i] < p.attributes.get(i))maxValues[i] = attr;
                        else if(minValues[i] > attr)minValues[i] = attr;
                        avgValues[i] += attr/nrOfProductAttributes;
                    }
                }
            }
        }
        private double getAvgAttributeValue(int i){
            if(maxValues.length-1 > i) return maxValues[i];
            return 0;
        }
        private double getMaxAttributeValue(int i){
            if(maxValues.length - 1 > i) return maxValues[i];
            return 0;
        }
        private double getMinAttributeValue(int i){
            if(minValues.length - 1 > i) return minValues[i];
            return 0;
        }
    }
}
