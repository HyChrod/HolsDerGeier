package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;
/**
 * Beschreiben Sie hier die Klasse Random.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Random extends HolsDerGeierSpieler {
    /**
    * Hier definieren Sie die Attribute Ihrer Klasse
    * Beispiel:   private <Typ> Name_des_Attributs
    */
    /* Hier stehen die Karten, die noch nicht gespielt wurden */
    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();

    /**
     * Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls Sie einen eigenen brauchen) Random
    */        
    public Random() {
        //  Meine Karten            
       
    }
    
    public void reset() {
        nochNichtGespielt.clear();
        for (int i=1;i<=15;i++)            
            nochNichtGespielt.add(i);                     
    }

    /**   
     *  Spiele zufaellig eine Karte
    */
    private int spieleZufallskarte() {
        int nochVorhanden=nochNichtGespielt.size();            
        int index=(int) (Math.random()*nochVorhanden);
        int ret=nochNichtGespielt.remove(index);
        return ret;
    }
    
    public int gibKarte(int naechsteKarte) {
        
        return spieleZufallskarte();
        
    }
        
    
    
    
}
