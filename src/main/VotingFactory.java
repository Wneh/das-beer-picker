package main;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by jomo on 2015-05-22.
 */
public class VotingFactory {

    public int id;
    public double beska;
    public double sotma;
    public double fyllighet;

    public VotingFactory(double beska, double sotma, double fyllighet){
        this.beska = beska;
        this.sotma = sotma;
        this.fyllighet = fyllighet;
        weigthValues();
    }

    public String toString(){
        return "id: "+id + ", beska: " + beska + ", sötma: " + sotma + ", fyllighet: " + fyllighet+"\n";
    }

    public void weigthValues() {
        double sum = beska + sotma + fyllighet;
        beska = beska/sum;
        sotma = sotma/sum;
        fyllighet = fyllighet/sum;
    }



}
