package edu.pjwstk.demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Małgorzata on 24.04.14.
 */
public class CD {
    private Wytwornia wytwornia;
    private Integer id;
    private String tytul;
    private String artysta;
    private Integer rokWydania;
    private Double cena;
    private List<Transakcja> sprzedaz = new ArrayList<Transakcja>();


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

    public Collections getSprzedaz() {
        return (Collections) sprzedaz;
    }
}

