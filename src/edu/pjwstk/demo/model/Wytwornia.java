package edu.pjwstk.demo.model;

/**
 * Created by Małgorzata on 24.04.14.
 */
public class Wytwornia {
    private String nazwaWytworni;
    private String kraj;


    public Wytwornia(String nazwaWytworni, String kraj) {
        this.nazwaWytworni = nazwaWytworni;
        this.kraj = kraj;
    }


    public String getNazwaWytworni() {
        return nazwaWytworni;
    }

    public void setNazwaWytworni(String nazwaWytworni) {
        this.nazwaWytworni = nazwaWytworni;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    @Override
    public String toString() {
        return "Wytwórnia [nazwa = " + nazwaWytworni + ", kraj = " + kraj;
    }
}
