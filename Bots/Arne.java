

package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;


public class Arne extends HolsDerGeierSpieler{
	// Arrays um Karten zu "zählen"
	// Meine Karten
	private ArrayList<Integer> Deck=new ArrayList<Integer>(); 
	//Restliche Gegnerkarten
	private ArrayList<Integer> P2Deck=new ArrayList<Integer>();
	//Kommende Geierkarten
	private ArrayList<Integer> Restkarten=new ArrayList<Integer>();
	//Meine gespielten Karten
	private ArrayList<Integer> Abgelegt=new ArrayList<Integer>();
	//Übrige Geierkarten
	private ArrayList<Integer> Geierkarten=new ArrayList<Integer>();
	//gespielte Karten Gegner
	private ArrayList<Integer> P2Abgelegt=new ArrayList<Integer>();
	
	//Variablen zum Rundenzählen und zur Auswahl der Karten
	private int Runde;
	private int x=0;
	private int y=0;
	
	private int gegnerNummer;

	//Befüllen der Arrays
	@Override
	public void reset()
	{
		gegnerNummer = getNummer() ^ 1;
		x=0;
		y=0;
		Runde=0;
		Restkarten.clear();
		for (int i=-5;i<16;i++)
			if (i!=0)
				Restkarten.add(i);
		P2Deck.clear();        
		for (int i=1;i<16;i++)
			P2Deck.add(i);
		Deck.clear();
		for (int i=1;i<16;i++)            
			Deck.add(i);  
		Abgelegt.clear();
		Geierkarten.clear();
	}
	//Überschreibt die gibKarte Methode
	@Override
	public int gibKarte (int naechsteKarte)
	{
		Geierkarten.add(naechsteKarte);

		if (Runde>0)
		{
			if(P2Deck.contains(letzterZug()))
			{
				P2Deck.remove(P2Deck.indexOf(getHdg().letzterZug(gegnerNummer)));
				P2Abgelegt.add(letzterZug());
			}
		}
		if (Runde>0)
			Punkte();

		int ret=Deck.remove(Deck.indexOf(Kartenwahl(naechsteKarte)));
		Abgelegt.add(ret);
		Runde++;
		return ret;  
	}

	private void Punkte()
	{
		if (Abgelegt.get(Abgelegt.size()-1)==P2Abgelegt.get(P2Abgelegt.size()-1)) 
		{
			x+=Geierkarten.get(Geierkarten.size()-2);
			y=x+Geierkarten.get(Geierkarten.size()-1);
		}
		else 
		{
			x=0;
			y=0;
		}
	}
	
	//Schleifen um zu determinieren welche Karte gelegt werden soll
	private int Kartenwahl(int naechsteKarte)
	{
		if (x!=0)
		{
			return Gewichtung(y);
		}
		return Gewichtung(naechsteKarte);
	}

	private int Punktewert(int Stichkarte)
	{
		return Math.abs(Stichkarte);
	}


	private int Gewichtung(int Stichkarte)
	{
		int Spielkarte=0;
		if (Karte(Stichkarte)<4)
		{
			Spielkarte=Karte(Stichkarte)+6+Random();
		}	
		else if(Stichkarte<10)
		{
			Spielkarte=Stichkarte+7+Random();
		}
		else 
		{
			Spielkarte=15;
		}
		return Karte(Spielkarte);
	}

	//Randomzahl für Abwechslung
	private int Random()
	{
		if(0!=(int)(Math.random()))
		{
			return 0;
		}
		else
		{
			return 1;
		}
		}

	//Abfrage ob Karte gelegt werden kann/darf
	private int Karte(int Auswahl)
	{
		boolean fertig=false;
		int i=0;
		int Ergebnis=0;
		while(!fertig && ((Auswahl-i)>0 || (Auswahl+i)<16))
		{
			if (Deck.contains(Auswahl+i))
			{
				fertig=true;
				Ergebnis=Auswahl+i;
				break;
			}
			if (Deck.contains(Auswahl-i))
			{
				fertig=true;
				Ergebnis=Auswahl-i;
				break;
			}
			i++;
		}
		return Ergebnis;
	}
}

	





