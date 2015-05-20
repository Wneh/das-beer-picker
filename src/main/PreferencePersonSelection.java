package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Erik on 2015-05-19.
 */
public class PreferencePersonSelection {
    private final int IDEAL_AGE = 25;
    private final int IDEAL_WEIGHT = 65;
    private final int IDEAL_LENGTH = 175;

    public ArrayList<Person> selectPersonsByPreference(ArrayList<Person> persons, ArrayList<Prefs> prefs, int k){
        HashSet<Person> result = new HashSet<Person>();

        for (int i = 0; i < prefs.size(); i++) {
            double max = 0;
            int cand = -1;
            ArrayList<PersonWrapper> sums = new ArrayList<PersonWrapper>();
            for (int j = 0; j < persons.size(); j++) {
                double [] weights = calculateWeightedValues(persons.get(j));
                double sum = weights[0]*prefs.get(i).pref1 + weights[1]*prefs.get(i).pref2 + weights[2]*prefs.get(i).pref3;
                //double sum = persons.get(j).age*prefs.get(i).pref1 +persons.get(j).length*prefs.get(i).pref2 + persons.get(j).weight*prefs.get(i).pref3;
                sums.add(new PersonWrapper(persons.get(j),sum));
            }
            Collections.sort(sums);
            Collections.reverse(sums);
            for (int j = 0; j < k; j++) {
                result.add(sums.get(j).person);
            }
        }

        return new ArrayList<Person>(result);
    }

    public double [] calculateWeightedValues(Person person){
        double [] weights = new double[3];

        double a = Math.pow(2,(10 - Math.abs(IDEAL_AGE - person.age)/5));
        double l = Math.pow(2,(10 - Math.abs(IDEAL_LENGTH - person.length)/6));
        double w = Math.pow(2,(10 - Math.abs(IDEAL_WEIGHT - person.weight)/7));

        weights[0] = a;
        weights[1] = l;
        weights[2] = w;
        return weights;
    }

    private class PersonWrapper implements Comparable<PersonWrapper>{
        public Person person;
        public double v;
        public PersonWrapper(Person person, double v){
            this.person = person;
            this.v = v;
        }
        public int compareTo(PersonWrapper other){
            if (this.v < other.v) return -1;
            else if (this.v > other.v) return 1;
            return 0;
        }
    }
}
