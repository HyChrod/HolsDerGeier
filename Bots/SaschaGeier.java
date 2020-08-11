package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * Dieser Bot gibt eine Zufällige Zahl aus einer von 4 Gruppen wieder.
 * Die zu verwendende Gruppe wird nach der grösse der Geier-/Mäusekarten entschieden.
 *
 * @author (Sascha Hessmann)
 * @version (12.01.2020)
 */


public  class SaschaGeier extends HolsDerGeierSpieler {

    // Alle meine Handkarten
    private ArrayList<Integer> nochNichtGespielt = new ArrayList<>();

    // Meine Karten 1-5
    private ArrayList<Integer> niedrigeKarten = new ArrayList<>();

    // Meine Karten 6-10
    private ArrayList<Integer> mittlereKarten = new ArrayList<>();

    // Meine Karten 11-13
    private ArrayList<Integer> hoheKarten = new ArrayList<>();

    // Meine Karten 14 & 15
    private ArrayList<Integer> hoechsteKarten = new ArrayList<>();

    // noch nicht gespielte Geier-/Mäusekarten
    private ArrayList<Integer> geierKarten=new ArrayList<>();


    // gespielte Gegner Karten
    private ArrayList<Integer> gegnerKarten= new ArrayList<>();

    // meine bereits gespielten Karten
    private ArrayList<Integer> myHistory = new ArrayList<>();

    // gegnerische Nummer 0 oder 1
    private int gegnerNummer;


    /**
     * Die gibtKarte methode wird aufgerufen, um abhängig von der gelegten Geier-oder Möusekarte
     * herauszufinden welche Karte gelegt wird.
     * Außerdem werden hier alle gelgten Karten(meine,Gegner und GeierKarten) in verschidenen Arraylisten gespeichert
     * und meine gelegte Karte aus der jeweiligen ArrayListe gestrichen.
     * Desweiteren wird geprüft ob Punkte aus der vorigen Runde mitgenommen werden
     */
    @Override
    public int gibKarte(int naechsteKarte) {


        // gespielte GeierKarte wird zu den gespielten GeierKarten hinzugefügt
        geierKarten.add(naechsteKarte);

        // gespielte GegnerKarte wird zu den gespielten GegnerKarten hinzugefügt
        gegnerKarten.add(getHdg().letzterZug(gegnerNummer));

        int i = 0;



        /**
         * Unentschiedenprüfung (verringert die Win-Chance)
         * hier wird geprüft ob die letzte runde unentschiden war,falls ja werden die Geierkarten zusammen gerechnet
         *if ((myHistory.size() - 1) == (gegnerKarten.get(gegnerKarten.size() - 1)) && geierKarten.get(geierKarten.size() - 2) >0)
         * naechsteKarte=+ (geierKarten.get(geierKarten.size() - 2));
        **/

        // falls die Geierkarten zwischen -2 und 3 liegt wird hier eine zufäliige Karte aus der ArrayList niedrigeKarte generiert
        if (-2 <= naechsteKarte && naechsteKarte <= 3) {
            i = niedrigeKarten.get((int) (Math.random() * niedrigeKarten.size()));

            // falls die Arraylist niedrigeKarten leer ist wird eine zufällige Karte aus der ArrayListe hoechsteKarten generiert
            if (niedrigeKarten.size() == 0) {
                i = hoechsteKarten.get((int) (Math.random() * hoechsteKarten.size()));
            }

            // falls die Geierkarten zwischen 4 und 8 liegt wird hier eine zufäliige Karte aus der ArrayList mittlereKarten generiert
        } else if (4 <= naechsteKarte && naechsteKarte <= 8) {
            i = mittlereKarten.get((int) (Math.random() * mittlereKarten.size()));

            //falls die Arraylist mittlere Karten leer ist wird eine zufällige Karte aus der ArrayListe niedrigenKarten generiert
            if (mittlereKarten.size() == 0) {
                i = niedrigeKarten.get((int) (Math.random() * niedrigeKarten.size()));
            }

            // falls die Geierkarten zwischen -5 und -3 liegt  wird hier eine zufäliige Karte aus der ArrayList hoheKarten generiert
        } else if (-5 <= naechsteKarte && naechsteKarte <=-3) {
            i = hoheKarten.get((int) (Math.random() * hoheKarten.size()));

            //falls die Arraylist hoheKarten leer ist wird eine zufällige Karte aus der ArrayListe mittlereKarten generiert
            if (hoheKarten.size() == 0) {
                i = mittlereKarten.get((int) (Math.random() * mittlereKarten.size()));

            }

            // falls die Geierkarten höher als 8 ist wird hier eine zufäliige Karte aus der ArrayList hoechsteKarten generiert
        } else if (naechsteKarte >= 9){
            i = hoechsteKarten.get((int) (Math.random() * hoechsteKarten.size()));

            //falls die Arraylist hoechsteKarten leer ist wird eine zufällige Karte aus der ArrayListe hoheKarten generiert
            if (hoechsteKarten.size() == 0) {
                i = hoheKarten.get((int) (Math.random() * hoheKarten.size()));
            }
        }

        // hier werden die jeweiligen Karten aus ihrer ArrayListe gelöscht
        if (niedrigeKarten.contains(i)) {
            niedrigeKarten.remove((Integer) i);
        } else if (mittlereKarten.contains(i)) {
            mittlereKarten.remove((Integer) i);
        } else if (hoheKarten.contains(i)) {
            hoheKarten.remove((Integer) i);
        } else {
            hoechsteKarten.remove((Integer) i);
        }
        if (nochNichtGespielt.contains(i))
            nochNichtGespielt.remove((Integer) i);
        // hier wird die gespielte Karten, der bereits gespielten hinzugefügt
        myHistory.add(i);

        // hier wird die Karte ausgespielt
        return i;

    }

    /**reset ist die Methode, die Arraylisten bei Spielbeginn leert und wenn nötig wieder auffüllt
     * und die Spielernummern ermittelt
     */
    @Override
    public void reset() {
        // füllt die Arraylist nochNichtGespielt mit Karten 1-15
        nochNichtGespielt.clear();
        for (int i = 1; i < 16; i++)
            nochNichtGespielt.add(i);

        // füllt die Arraylist niedrigeKarten mit karten 1-5
        niedrigeKarten.clear();
        for (int i = 1; i <= 5; i++)
            niedrigeKarten.add(i);

        // füllt die Arraylist mittlerekarten mit Karten 6-10
        mittlereKarten.clear();
        for (int i = 6; i <= 10; i++)
            mittlereKarten.add(i);

        // füllt die Arraylist hoheKarten mit Karten 11-13
        hoheKarten.clear();
        for (int i = 11; i <= 13; i++)
            hoheKarten.add(i);

        // füllt die Arraylist hoechsteKarten mit den Karten 14&15
        hoechsteKarten.clear();
        for (int i = 14; i <= 15; i++)
            hoechsteKarten.add(i);

        // leert die Arraylist gegnerKarten
        gegnerKarten.clear();


        // leert die Arraylist geierKarten
        geierKarten.clear();

        //leert die ArrayListe myHistory
        myHistory.clear();


        // ermittelt meine Spielernummer und die Gegnernummer
        int meineNummer = getNummer();
        if (meineNummer == 1) {
            gegnerNummer = 0;
        } else {
            gegnerNummer= 1;
        }



    }


}


