package edu.pjwstk.demo.model;

import java.util.ArrayList;
import java.util.List;

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
    public List<Transakcja> sprzedaz = new ArrayList<>();


    public CD(Wytwornia wytwornia, String tytul, String artysta, Integer rokWydania, Double cena, Integer id) {
        this.wytwornia = wytwornia;
        this.tytul = tytul;
        this.artysta = artysta;
        this.rokWydania = rokWydania;
        this.cena = cena;
        this.id = id;
    }

    public void addTransakcja(Transakcja transakcja) {
        sprzedaz.add(transakcja);
    }
}

