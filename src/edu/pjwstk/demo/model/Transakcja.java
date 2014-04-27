package edu.pjwstk.demo.model;

/**
 * Created by Małgorzata on 24.04.14.
 */
public class Transakcja {
    public String data;
    public Integer ilosc;
    public Double cenaSprzedazy;

    public Transakcja(String data, Integer ilosc, Double cenaSprzedazy) {
        this.data = data;
        this.ilosc = ilosc;
        this.cenaSprzedazy = cenaSprzedazy;
    }
}
