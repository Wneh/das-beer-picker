package main;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-21.
 */
public class CustomerGroup {
    public ArrayList<Customer> customers;
    public CustomerGroupInformation customerGroupInformation;
    private final int WEIGHT_SCALE = 100;

    public CustomerGroup(ArrayList<Customer> customers){
        this.customers = customers;
        this.customerGroupInformation = new CustomerGroupInformation(customers);
    }

    public double getMaxAttributeValue(int i){
        return customerGroupInformation.getMaxAttributeValue(i);
    }
    public double getMinAttributeValue(int i){
        return customerGroupInformation.getMinAttributeValue(i);
    }
    public double getAvgAttributeValue(int i){
        return customerGroupInformation.getAvgAttributeValue(i);
    }

    private class CustomerGroupInformation{
        int nrOfCustomerPreferences;
        double [] maxValues;
        double [] minValues;
        double [] avgValues;
        CustomerGroupInformation(ArrayList<Customer> customers){
            if(!customers.isEmpty()){
                nrOfCustomerPreferences = customers.get(0).preferences.size();
                maxValues = new double[nrOfCustomerPreferences];
                minValues = new double[nrOfCustomerPreferences];
                avgValues = new double[nrOfCustomerPreferences];

                for (Customer c : customers) {
                    for (int i = 0; i < c.preferences.size(); i++) {
                        double attr = c.preferences.get(i);
                        if(maxValues[i] < c.preferences.get(i)){
                            maxValues[i] = attr;
                        }
                        else if(minValues[i] > attr)minValues[i] = attr;
                        avgValues[i] += attr;
                    }
                }
                for (int i = 0; i < avgValues.length; i++) {
                    avgValues[i] = avgValues[i]/customers.size();
                }
            }
        }
        private double getAvgAttributeValue(int i){
            return avgValues[i];
        }
        private double getMaxAttributeValue(int i){
            if(maxValues.length > i) return maxValues[i];
            return 0;
        }
        private double getMinAttributeValue(int i){
            if(minValues.length > i) return minValues[i];
            return 0;
        }
    }
}
