package edu.pjwstk.demo.model;

/**
 * Created by Małgorzata on 24.04.14.
 */
public class Transakcja {
    private String data;
    private Integer ilosc;
    private Double cenaSprzedazy;

    public Transakcja(String data, Integer ilosc, Double cenaSprzedazy) {
        this.data = data;
        this.ilosc = ilosc;
        this.cenaSprzedazy = cenaSprzedazy;
    }
}
