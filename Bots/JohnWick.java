package de.FScheunert.HolsDerGeier.Bots;

import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * 	___________________________________________________________________________________________________________________________________________________

	Die Spielidee
	=============
	
	Vorueberlegung: Ob ich X Punkte gutgeschrieben oder mein Gegner X Punkte abgezogen bekommt, macht keinen Unterschied. 
	Denn: Beides hat den gleichen Effekt.
	--> Folglich ist es obsolet, nach dem Vorzeichen der Karte zu fragen.
	
	Aus diesem Grund wird von einer vorzeichenbasierten Entscheidung abgesehen. 
	Vielmehr werden im Folgenden die Betraege der Zahlen bzw. Karten untersucht und als Entscheidungsgrundlage herangezogen.
	
	Es gibt verschiedene Faktoren, die im Entscheidungsprozess eine Rolle spielen. Dazu gehoeren u.A. der Wert der aufgedeckte Karte,
	die noch gewinnbaren Karten und die verfuegbaren Karten der Spieler. Auch hier wurden diese Faktoren (z.B. durch ArrayLists) beruecksichtigt.
	
	
	Die Strategie
	=============
	
	1. Wird eine Karte aufgedeckt, so schaut sich der Spieler zunaechst den (Gesamt-)Betrag an, um den es in dieser Runde geht.
	2. Ist dieser sehr klein, so wird eine niedrige Karte gelegt. Ist er groeﬂer, so wird erneut zwischen hohen und ganz hohen Karten
		unterschieden.
	3. Ist der Betrag ganz hoch, so wird ohne weitere Ueberlegung eine ganz hohe bzw. die hoechstmoegliche Karte gespielt. Ansonsten wird vorher
		versucht, eine niedrigere Karte zu finden, mit der der Gegner ueberbietet werden kann.
	4. Wenn der Betrag nicht klein aber auch nicht groﬂ zu sein scheint, so wird zunaechst geschaut, ob es noch hoehere Karten zu gewinnen gibt.
		Gibt es hoehere Karten, so wird eine niedrigere Karte gespielt. Andernfalls wird eine hoehere Karte gesucht, mit der der Gegner ueberbietet
		werden kann.
    	
	___________________________________________________________________________________________________________________________________________________
    	
	@author (Yasin Yildirim) 
	@version (Version 1.0)
*/

public class JohnWick extends HolsDerGeierSpieler {

    private ArrayList<Integer> nochZuGewinnen=new ArrayList<Integer>();
    private ArrayList<Integer> vomGegnerNochNichtGelegt=new ArrayList<Integer>();
    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
    
    private int nummerJohn;
    private int nummerGegner;
    private int tmpBetrag;
    private int meineKarte;
    private int letzteKarteGegner;
    private int letzteKarteJohn;
    private int savedPoints;
    
    
    public JohnWick() {
    	// Definiere Spielernummern, um spaeter letzte Zuege zuzuweisen
    	nummerJohn=this.getNummer();
    	if (nummerJohn==0) {
    		nummerGegner=1;
    	} else {
    		nummerGegner=0;
    	}
    }

	public void reset() {
        nochZuGewinnen.clear();
        for (int i=10;i>-6;i--)
            nochZuGewinnen.add(i);
        vomGegnerNochNichtGelegt.clear();        
        for (int i=15;i>0;i--)
            vomGegnerNochNichtGelegt.add(i);
        for (int i=15;i>0;i--)            
            nochNichtGespielt.add(i);
        savedPoints=0;
        letzteKarteGegner=0;
        letzteKarteJohn=0;
    }
	
    
    public int gibKarte(int naechsteKarte) {    	
    	
    	// ========================
    	// ===== VORBEREITUNG =====
    	// ========================
    	
    	
      	// Aktualisiere die Karten des Gegners und den Stapel
    	// ==================================================
    	
    	// Fuer die naechsten Runden --> Entferne die aufgedeckte Karte vom Stapel.
    	nochZuGewinnen.remove(nochZuGewinnen.indexOf(naechsteKarte));
    	
    	// Aktualisiere die (noch nicht gespielten) Karten des Gegners.
    	letzteKarteGegner=getHdg().letzterZug(nummerGegner);
    	if (letzteKarteGegner!=-99)
    		vomGegnerNochNichtGelegt.remove(vomGegnerNochNichtGelegt.indexOf(letzteKarteGegner));
    	
    	//Anmerkung: Die eigenen Karten werden schon zuvor aktualisiert, wenn die letzte Karte "gelegt" wird.
    	
    	
    	// Worum geht es in dieser Runde?
    	// ==============================
    	
    	// Definiere den Betrag, um den es in dieser Runde geht.
    	tmpBetrag=Math.abs(naechsteKarte);
    	
    	// Gab es in der letzten Runde einen Gleichstand, so berueksichtige die Karte aus der vorherigen Runde und aktualisiere den Gesamtbetrag.
    	letzteKarteJohn=getHdg().letzterZug(nummerJohn);
    	if (letzteKarteJohn == letzteKarteGegner)
    		tmpBetrag=tmpBetrag+savedPoints;
    	
    	
    	
    	// ================================
    	// ===== ENTSCHEIDUNGSPROZESS =====
    	// ================================
    	
    	
    	// Ist der Betrag "klein", werden bewusst niedrige Karten gespielt, um die Hoeheren fuer wichtigere Karten zu nutzen.
    	// ==================================================================================================================
    	
    	if (tmpBetrag<3) {
    
    		meineKarte=nochNichtGespielt.get(nochNichtGespielt.size()-1);
		
    	// Wenn der Betrag verhaeltnismassig groeﬂer ist...
    	// ================================================
    	} else {
    		
    		// Erneute Differenzierung --> Untersuche, ob es sich um eine entscheidende bzw. ganz hohe Karte handelt...
    		// Wenn die Karte ganz wichtig bzw. der (Gesamt-)Betrag hoch ist, schaue dir die Karten des Gegners an.
    		if (tmpBetrag>5) {
    			
    			// Um zu gewinnen --> Spiele niedrigste Karte, wenn die zuletzt (!) aufgedeckte Karte negativ ist.
    			if (naechsteKarte<0) {
    				
    				meineKarte=nochNichtGespielt.get(nochNichtGespielt.size()-1);
    				
    			} else {
	    			
    				// Idee: Wenn ich bspw. 15, 14 und 10 und mein Gegner 14, 13 und 12 spielen kann, macht es keinen Sinn direkt die 15 zu spielen, da ich dann zwar die erste Karte gewinne, mich in den naechsten Runden aber in einer suboptimalen Situation befinde und die naechsten 2 hoeheren Karten mit groﬂer Wahrscheinlichkeit verliere (Voraussetzung: Der Gegner spielt logisch).
	    			
	    			
					// Bei ganz hohem Betrag
    				if (tmpBetrag>8) {
    					
    					// Spiele ohne Ueberlegung die hoechste Karte!
    					meineKarte=nochNichtGespielt.get(0);
    					
    				// Ansonsten...
    				} else {
    					
    					// Der Betrag ist nicht ganz hoch --> Waehle groeﬂte Karte<14 und reserviere 14 & 15 f¸r die ganz hohen Karten (Karten>8)
    					for( int ij: nochNichtGespielt )
    	    			{
    	    				if (ij<14) {
    	    					meineKarte=ij;
    	    					break;
            				}
    	    			}
    					
    					// Suche nun die hoechste Karte des Gegners
        				int tmpHoechsteKarteGegner=vomGegnerNochNichtGelegt.get(0);
    	    			
    	    			// Siehe nach, ob du ggf. eine niedrigere Karte spielen kannst, mit der du den Gegner ueberbietest...
    	    			for( int ij: nochNichtGespielt )
    	    			{
    	    				if (ij>tmpHoechsteKarteGegner) {
    	    					meineKarte=ij;
    	    				} else {
            					break;
            				}
    	    			}	
    				}    			
    			}
    			
    			
    		// Wenn die Karte unwichtig bzw. der (Gesamt-)Betrag niedrig zu sein scheint, schaue dir die noch moeglichen Gewinne und die uebrig gebliebenen Karten deines Gegners an.
    		} else {
    			
    			// Waehle zunaechst die niedrigste Karte (und spiele sie, falls du spaeter keine Geeignetere findest).
    	    	meineKarte=nochNichtGespielt.get(nochNichtGespielt.size()-1);
    	    	
    			// Ermittle hoehere Karten, die noch gewinnbar sind.
    			int anzahlHoehereBetraege=0;
    	    	for( int ij: nochZuGewinnen )
    			{
    				if (ij>Math.abs(naechsteKarte)) {
    					anzahlHoehereBetraege++;		
    				} else {
    					break;
    				}
    			}
    			// Gibt es noch viel hoehere Karten bzw. Betraege zu gewinnen, so spiele die Niedrigste>=3. --> Karten<3 werden fuer ganz niedrige Betraege (oder fuer hohe Gesamtbetraege ueber 2 Runden, wenn letzte aufgedeckte Karte negativ ist) aufgehoben.
    			// Anmerkung: Die Auswertung dieser Variable auf diese Art hat sich in der Praxis als erfolgsversprechend herausgestellt...
    	    	if ((float) anzahlHoehereBetraege/nochNichtGespielt.size()>0.3) {
    			    				
    				for( int ij: nochNichtGespielt )
        			{
        				if (ij>=3) {
        					meineKarte=ij;		
        				} else {
        					break;
        				}
        			}	

				// Gibt es nicht mehr so viel zu gewinnen,...
    			} else {
    				
    				// ... so suche zunaechst die hoechste Karte des Gegners...
        			int tmpHoechsteKarteGegner=vomGegnerNochNichtGelegt.get(0);
    				
        			/// ... und spiele mindestens die hoechste Karte<10, die dein Gegner hat.
    				for( int ij: nochNichtGespielt )
        			{
        				if (ij>=tmpHoechsteKarteGegner && ij<10) {
        					meineKarte=ij;
        				} else {
        					break;
        				}
        			}
    			}
    		} 
    	}
    	
    	// Speichere den Betrag ab, sofern er in der naechsten Runde beruecksichtigt werden soll.
    	savedPoints=naechsteKarte;
    	
    	// Spiele die ausgewaehlte Karte und lege sie "von der Hand auf den Tisch".
    	nochNichtGespielt.remove(nochNichtGespielt.indexOf(meineKarte));
    	return meineKarte;
    	
    } 
    
}
