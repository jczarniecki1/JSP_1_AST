package edu.pjwstk.demo.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Małgorzata on 24.04.14.
 */
public class CD {
    public Wytwornia wytwornia;
    public Integer id;
    public String tytul;
    public String artysta;
    public Integer rokWydania;
    public Double cena;
    public Transakcja[] sprzedaz = new Transakcja[0];


    public CD(Wytwornia wytwornia, String tytul, String artysta, Integer rokWydania, Double cena, Integer id) {
        this.wytwornia = wytwornia;
        this.tytul = tytul;
        this.artysta = artysta;
        this.rokWydania = rokWydania;
        this.cena = cena;
        this.id = id;
    }

    public void addTransakcja(Transakcja transakcja) {
        ArrayList<Transakcja> tempList = new ArrayList<>(Arrays.asList(sprzedaz));
        tempList.add(transakcja);
        sprzedaz = tempList.toArray(new Transakcja[tempList.size()]);
    }
}

