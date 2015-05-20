package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Erik on 2015-05-19.
 */
public class CentroidCandidateFinder {
    private final int IDEAL_AGE = 25;
    private final int IDEAL_WEIGHT = 65;
    private final int IDEAL_LENGTH = 175;

    public ArrayList<Person> findCentroidCandidates(ArrayList<Person> persons,int limit){
        ArrayList<Integer> absCentroid = calculateAbsoluteCentroid(persons);
        Person centroidPerson = new Person(0,absCentroid.get(0),absCentroid.get(1),absCentroid.get(2));
        for (Person p : persons)p.cosineScore = cosineSimilarity(centroidPerson,p);
        Collections.sort(persons);
        if(limit <= 0) return persons;
        return new ArrayList<Person>(persons.subList(0,limit));
    }

    private ArrayList<Integer> calculateAbsoluteCentroid(ArrayList<Person> persons){
        ArrayList<Integer> centroid = new ArrayList<Integer>();
        int a = 0, l = 0, w = 0;
        for (Person person : persons){
            a += person.age;
            l += person.length;
            w += person.weight;
        }
        centroid.add(a/persons.size());
        centroid.add(l/persons.size());
        centroid.add(w/persons.size());
        return centroid;
    }

    private Double cosineSimilarity(Person person1, Person person2){
        double [] p1 = person1.toArray();
        double [] p2 = person2.toArray();
        return new CosineSimilarity().cosineSimilarity(p1, p2);
    }

    public double [] calculateWeightedValues(Person person){
        double [] weights = new double[3];

        double a = 100 - (Math.abs(IDEAL_AGE - person.age) * (100/47));
        double l = 100 - (Math.abs(IDEAL_LENGTH - person.length) * (100/60));
        double w = 100 - (Math.abs(IDEAL_WEIGHT - person.weight) * (100/70));

        weights[0] = a;
        weights[1] = l;
        weights[2] = w;
        return weights;
    }
}
