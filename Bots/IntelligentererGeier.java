package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;
/**
 * Beschreiben Sie hier die Klasse IntelligentererGeier.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class IntelligentererGeier extends HolsDerGeierSpieler {

    private ArrayList<Integer> nochZuGewinnen=new ArrayList<Integer>();
    private ArrayList<Integer> vomGegnerNochNichtGelegt=new ArrayList<Integer>();
    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
    
    public IntelligentererGeier() {}
    
    public void reset() {
        nochZuGewinnen.clear();
        for (int i=10;i>-6;i--)
            if(i != 0) nochZuGewinnen.add(i);
        vomGegnerNochNichtGelegt.clear();        
        for (int i=15;i>0;i--)
            vomGegnerNochNichtGelegt.add(i);
        for (int i=15;i>0;i--)            
            nochNichtGespielt.add(i);                                 
    }
    
    public int gibKarte(int naechsteKarte) {
       int ret=-99;
       int letzteKarteGegner= getHdg().letzterZug( getNummer() == 0 ? 1 : 0 );
       if (letzteKarteGegner!=-99)
          vomGegnerNochNichtGelegt.remove(vomGegnerNochNichtGelegt.indexOf(letzteKarteGegner));
       // Lösche dieser Karte
       nochZuGewinnen.remove(nochZuGewinnen.indexOf(naechsteKarte));
       // Wenn die karte negativ ist  => spiele die letzte Karte
       if (naechsteKarte>0) {           
           // Kommt die hoechste Karte und der gener hat die gleiche höchste => spiele mittlere Karte
           if ((nochZuGewinnen.indexOf(naechsteKarte)==0)&&(vomGegnerNochNichtGelegt.get(0)==nochNichtGespielt.get(0))) {
                   ret=nochNichtGespielt.get(nochZuGewinnen.size()/2);
                   //System.out.println(">>> A");
           // Steht die zu gewinnende Karte in der vorderen Haelfte der Reihenfolge und hat der gegner => Spiele die höchste Karte
        } else if (nochZuGewinnen.indexOf(naechsteKarte)>nochZuGewinnen.size()/2) {
                   ret=nochNichtGespielt.get(0);  
                   //System.out.println(">>> B");                   
                } else { // sonst greife in die Mitte
                   ret=nochNichtGespielt.get(nochZuGewinnen.size()/2);
                   //System.out.println(">>> C");                   
                }
       } else {
           ret=nochNichtGespielt.get(nochNichtGespielt.size()-1);
           //System.out.println(">>> D");                              
       }
       nochNichtGespielt.remove(nochNichtGespielt.indexOf(ret));
       return ret;
    }
    
}
