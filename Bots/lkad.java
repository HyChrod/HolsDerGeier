package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

    /**
     * lkad erbt von der Klasse HolsDerGeierSpieler
     */
public  class lkad extends HolsDerGeierSpieler {

    /**
     * Hier lege ich die Array Listen an
     */
    private ArrayList<Integer> nochNichtGespielt = new ArrayList<>();
    private ArrayList<Integer> niedrigeKarten = new ArrayList<>();
    private ArrayList<Integer> mittlereKarten = new ArrayList<>();
    private ArrayList<Integer> hoheKarten = new ArrayList<>();
    private ArrayList<Integer> hoechsteKarten = new ArrayList<>();



    /**
     * Das ist der Standartbefehl um eine Karte aufdecken zu lassen
     */

        @Override
public int gibKarte(int naechsteKarte) {

        int i = nochNichtGespielt.get(0);



    /*
     * Ich habe hier eingeteilt, bei welchen aufgedeckten Karten ich meine Karte aus der Arraylist "niedrigeKarten",
     * "mittlereKarten", "hoheKarten" und "hoechsteKarten" lege.
     * Dies Kartenwerte werden zufaellig aus der jeweiligen Arraylist generiert.
     */

        if (-2 <= naechsteKarte && naechsteKarte <= 3){
            i = niedrigeKarten.get((int) (Math.random() * niedrigeKarten.size()));
        }

        else if (4 <= naechsteKarte && naechsteKarte <= 8){
            i = mittlereKarten.get((int) (Math.random() * mittlereKarten.size()));
        }
        else if (-5 <= naechsteKarte && naechsteKarte <= -3){
            i = hoheKarten.get((int) (Math.random() * hoheKarten.size()));
        }
        else if (naechsteKarte >= 9){
            i = hoechsteKarten.get((int) (Math.random() * hoechsteKarten.size()));
        }


    /*
     * Hier werden die gespielten Karten entfernt, damit die Karten nicht doppelt gespielt werden.
     */
        if (niedrigeKarten.contains(i)) {
            niedrigeKarten.remove((Integer) i);
        } else if (mittlereKarten.contains(i)) {
            mittlereKarten.remove((Integer) i);
        } else if (hoheKarten.contains(i)) {
            hoheKarten.remove((Integer) i);
        }else{
            hoechsteKarten.remove((Integer) i);
        }
        if(nochNichtGespielt.contains(i))
        nochNichtGespielt.remove((Integer) i);
        return i;
    }


    /**
     * Zu beginn jeder Runde werden die Array Listen gesäubert und anschließend mit den noch nicht gespielten Karten
     * gefüllt.
     */
    @Override
public void reset() {

        nochNichtGespielt.clear();
        for (int i = 1; i < 16; i++)
            nochNichtGespielt.add(i);
        niedrigeKarten.clear();
        for (int i = 1; i <= 5; i++)
            niedrigeKarten.add(i);
        mittlereKarten.clear();
        for (int i = 6; i <= 10; i++)
            mittlereKarten.add(i);
        hoheKarten.clear();
        for (int i = 11; i <= 13; i++)
            hoheKarten.add(i);
        hoechsteKarten.clear();
        for (int i = 14; i <= 15; i++)
            hoechsteKarten.add(i);
    }

}

