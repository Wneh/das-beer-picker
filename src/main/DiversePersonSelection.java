package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by Erik on 2015-05-19.
 */
public class DiversePersonSelection {

    public ArrayList<Person> selectMostDiversePersons(ArrayList<Person> persons, int r){
        if(r > persons.size()) return null;
        ArrayList<Person> result = new ArrayList<Person>();
        // calc distance between all persons
        int p1 = 0, p2 = 0;
        double [][] distances = new double[persons.size()][persons.size()];
        for (int i = 0; i < persons.size(); i++) {
            double maxDist = Double.MAX_VALUE;
            for (int j = i + 1; j < persons.size(); j++){
                    double tmpDist = new CosineSimilarity().cosineSimilarity(persons.get(i).toArray(), persons.get(j).toArray());
                    if (tmpDist < maxDist) {
                        maxDist = tmpDist;
                        p1 = i;
                        p2 = j;
                    }
                    distances[i][j] = tmpDist;

            }
        }
        // add persons with greatest distance
        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);
        result.add(persons.get(p1));
        result.add(persons.get(p2));
        HashSet<Integer> taken = new HashSet<Integer>();
        taken.add(p1);taken.add(p2);
        while (result.size() < r) {
            double min = Double.MAX_VALUE;
            int cand = -1;

            for (int i = 0; i < persons.size(); i++) {
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
            result.add(persons.get(cand));

        }
        return result;
    }



}
