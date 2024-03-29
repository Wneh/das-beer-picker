package main;

import java.util.*;

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
        double maxDist = Double.MAX_VALUE;
        double [][] distances = new double[products.size()][products.size()];
        for (int i = 0; i < products.size(); i++) {
            for (int j = 0; j < products.size(); j++){
                if(i == j)continue;
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
            int cand = -1;
            double min = Double.MAX_VALUE;
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

    public ArrayList<Customer> getKMostDiverseCustomers(ArrayList<Customer> customers,int k){
        if(k > customers.size()) return null;
        ArrayList<Customer> result = new ArrayList<Customer>();
        // calc distance between all customers
        int p1 = 0, p2 = 0;
        double maxDist = Double.MAX_VALUE;
        double [][] distances = new double[customers.size()][customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            for (int j = 0; j < customers.size(); j++){
                if(i == j)continue;
                double tmpDist = new CosineSimilarity().cosineSimilarity(customers.get(i).toArray(), customers.get(j).toArray());
                if (tmpDist < maxDist) {
                    maxDist = tmpDist;
                    p1 = i;
                    p2 = j;
                }
                distances[i][j] = tmpDist;
            }
        }
        // add products with greatest distance
        result.add(customers.get(p1));
        result.add(customers.get(p2));
        HashSet<Integer> taken = new HashSet<Integer>();
        taken.add(p1);taken.add(p2);
        double min = Double.MAX_VALUE;
        while (result.size() < k) {
            int cand = -1;
            for (int i = 0; i < customers.size(); i++) {
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
            result.add(customers.get(cand));
        }
        return result;
    }

    public Product topProduct(Customer c){
        double min = Double.MAX_VALUE;
        Product top = new Product();
        for(Product p: productGroup.products){
            double sum = 0;
            for (int i = 0; i < p.attributes.size(); i++) {
                sum += Math.abs(p.weightedAttributes.get(i) - c.preferences.get(i));
            }
            if(sum < min){
                min = sum;
                top = p;
            }
        }
        return top;
    }

    public ArrayList<Product> getTopKProducts(ArrayList<Product> products,ArrayList<Customer> customers, int k){
        HashSet<Product> result = new HashSet<Product>();
        double min = Double.MAX_VALUE;
        Product top = new Product();
        ArrayList<ProductWrapper> sums = new ArrayList<ProductWrapper>();
        for (Product p : productGroup.products) {
            double sum = 0;
            for (int i = 0; i < p.attributes.size(); i++) {
                sum += Math.abs(p.weightedAttributes.get(i) - customerGroup.getAvgAttributeValue(i));
            }
            sums.add(new ProductWrapper(p, sum));
        }

        Collections.sort(sums);
        for (int j = 0; j < k; j++) {
            result.add(sums.get(j).product);
        }
        return new ArrayList<Product>(result);
    }

    public ArrayList<Customer> getTopKCustomerCentroidCandidates(Product product, int k){
        HashSet<Double> hashcosines = new HashSet<Double>();
        Customer idealCustomer = generateIdealCustomer(product);
        HashMap<Double,Customer> customerMap = new HashMap<Double, Customer>();
        for (Customer c: customerGroup.customers){
            double cosine = cosineSimilarity(idealCustomer, c);
            hashcosines.add(cosine);
            customerMap.put(cosine,c);
        }
        ArrayList<Double> cosines = new ArrayList<Double>(hashcosines);
        Collections.sort(cosines);
        //Collections.reverse(cosines);
        System.out.println("Cosines: "+cosines);

        ArrayList<Customer> candidates = new ArrayList<Customer>();
        for (int i = 0; i < k; i++) {
            candidates.add(customerMap.get(cosines.get(i)));
        }

        return candidates;
    }

    public ArrayList<Product> getTopKProductCentroidCandidates(Customer customer, int k){
        Product idealProduct = generateIdealProduct(customer);
        HashMap<Double,Product> productMap = new HashMap<Double, Product>();
        ArrayList<Double> cosines = new ArrayList<Double>();
        for (Product p: productGroup.products){
            double cosine = cosineSimilarity(idealProduct.toArray(), p.toArray());
            cosines.add(cosine);
            productMap.put(cosine,p);
        }
        Collections.sort(cosines);
        Collections.reverse(cosines);
        ArrayList<Product> candidates = new ArrayList<Product>();
        for (int i = 0; i < k; i++) {
            candidates.add(productMap.get(cosines.get(i)));
        }
        return candidates;
    }

    private Customer generateIdealCustomer(Product product) {
        Customer top = new Customer(product.weightedAttributes, 1337, "Leetman Leetson");
        return top;
    }

    private Product generateIdealProduct(Customer customer) {
        Product top = new Product(1337, "Leet Beer", customer.preferences);
        return top;
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

