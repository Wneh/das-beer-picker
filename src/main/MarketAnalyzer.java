package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Erik on 2015-05-21.
 */
public class MarketAnalyzer {
    public ProductGroup productGroup;
    public CustomerGroup customerGroup;

    public MarketAnalyzer(ProductGroup productGroup, CustomerGroup customerGroup){
        this.customerGroup = customerGroup;
        this.productGroup = productGroup;
    }

    public ArrayList<Product> getKMostDiverseProducts(ArrayList<Product> products,int k){

        if(k > products.size()) return null;
        ArrayList<Product> result = new ArrayList<Product>();
        // calc distance between all products
        int p1 = 0, p2 = 0;
        double [][] distances = new double[products.size()][products.size()];
        for (int i = 0; i < products.size(); i++) {
            double maxDist = Double.MAX_VALUE;
            for (int j = i + 1; j < products.size(); j++){
                double tmpDist = new CosineSimilarity().cosineSimilarity(products.get(i).toArray(), products.get(j).toArray());
                if (tmpDist < maxDist) {
                    maxDist = tmpDist;
                    p1 = i;
                    p2 = j;
                }
                distances[i][j] = tmpDist;

            }
        }
        // add products with greatest distance
        result.add(products.get(p1));
        result.add(products.get(p2));
        HashSet<Integer> taken = new HashSet<Integer>();
        taken.add(p1);taken.add(p2);
        while (result.size() < k) {
            double min = Double.MAX_VALUE;
            int cand = -1;
            for (int i = 0; i < products.size(); i++) {
                if (!taken.contains(i)) {
                    double sum = 0;
                    for (int j : taken) {
                        sum += distances[i][j];
                    }
                    if (sum < min) {
                        min = sum;
                        cand = i;
                    }
                }
            }
            taken.add(cand);
            result.add(products.get(cand));
        }
        return result;
    }

    public ArrayList<Product> getKMostDiverseProducts(int k) {
        return getKMostDiverseProducts(productGroup.products,k);
    }

    public ArrayList<Product> getTopKProducts(ArrayList<Product> products,ArrayList<Customer> customers, int k){
        HashSet<Product> result = new HashSet<Product>();
        for (int i = 0; i < customers.size(); i++) {
            ArrayList<ProductWrapper> sums = new ArrayList<ProductWrapper>();
            for (int j = 0; j < products.size(); j++) {
                double [] weights = getWeightedAttributes(products.get(j));
                double sumWeights = 0;
                for (int r = 0; r < products.get(j).attributes.size(); r++) {
                    sumWeights += weights[r]*customers.get(i).preferences.get(r);
                }
                sums.add(new ProductWrapper(products.get(j), sumWeights));
            }
            Collections.sort(sums);
            Collections.reverse(sums);
            for (int j = 0; j < k; j++) {
                result.add(sums.get(j).product);
            }
        }
        return new ArrayList<Product>(result);
    }

    public ArrayList<Customer> getTopKCustomerCentroidCandidates(Product product, int k){
        Customer idealCustomer = generateIdealCustomer(product);
        System.out.println("IDEAL CUSTOEMR: "+idealCustomer);
        HashMap<Double,Customer> customerMap = new HashMap<Double, Customer>();
        ArrayList<Double> cosines = new ArrayList<Double>();
        for (Customer c: customerGroup.customers){
            double cosine = cosineSimilarity(idealCustomer, c);
            cosines.add(cosine);
            customerMap.put(cosine,c);
        }
        Collections.sort(cosines);
        Collections.reverse(cosines);
        ArrayList<Customer> candidates = new ArrayList<Customer>();
        for (int i = 0; i < k; i++) {
            candidates.add(customerMap.get(cosines.get(i)));
        }
        return candidates;
    }

    /**
     * TODO:
     * THIS IS WRONG
     * @param product
     * @return
     */
    private Customer generateIdealCustomer(Product product) {
        double[] weDoubles = getWeightedAttributes(product);
        double sum = 0;
        for (double d: weDoubles)sum += d;
        ArrayList<Double> preferences = new ArrayList<Double>();
        for (double d: weDoubles)preferences.add(d/sum);
        return new Customer(preferences,9876, "ideal");
    }

    private double[] getWeightedAttributes(Product product){
        double [] weights = new double[product.attributes.size()];
        for (int i = 0; i < weights.length; i++) {
            double distanceFromAvg = Math.abs(productGroup.getAvgAttributeValue(i) - product.attributes.get(i));
            weights[i] = 10 * (distanceFromAvg/productGroup.getMaxAttributeValue(i));
        }
        return weights;
    }

    public ArrayList<Product> getTopKProducts(int k) {
        return getTopKProducts(productGroup.products,customerGroup.customers, k);
    }

    private class ProductWrapper implements Comparable<ProductWrapper>{
        public Product product;
        public double v;
        public ProductWrapper(Product product, double v){
            this.product = product;
            this.v = v;
        }
        public int compareTo(ProductWrapper other){
            if (this.v < other.v) return -1;
            else if (this.v > other.v) return 1;
            return 0;
        }
    }

    private Double cosineSimilarity(Customer a, Customer b){
        return cosineSimilarity(a.toArray(),b.toArray());
    }
    private Double cosineSimilarity(double[] a, double[] b){
        return new CosineSimilarity().cosineSimilarity(a, b);
    }

}

