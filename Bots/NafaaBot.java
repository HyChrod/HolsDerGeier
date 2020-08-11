package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

//* @author (Nafaa - Alasfar)
//* @version (12.01)

public class NafaaBot extends HolsDerGeierSpieler {

	private ArrayList<Integer> meineKarten = new ArrayList<Integer>(); // Array für meine Karten
	private ArrayList<Integer> geierKarten = new ArrayList<Integer>(); // Array für Geier Karten
	private int letzteGKarte; // Die zuletzt gespielte Geierkarte
	private int lastplayedcard; // Die von mir zuletzt gespielte Karte
	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode reset() und da werden die Arrays entleert und dann
	// befüllt
	//
	// =================================================================================================================================
	// =================================================================================================================================

	@Override
	public void reset() {

		geierKarten.clear();
		for (int i = 10; i > 0; i--)
			geierKarten.add(i);
		for (int i = -5; i < 0; i++)
			geierKarten.add(i);
		meineKarten.clear();
		for (int i = 15; i > 0; i--)
			meineKarten.add(i);
	}
	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode gibkarte() und da werden 2 unterschiedliche Taktiken
	// geführt.
	//
	// =================================================================================================================================
	// =================================================================================================================================

	@Override
	public int gibKarte(int naechsteKarte) {
		// unentschieden? dann wird index vom letztenzug mit dem neuen addiert und
		// ersetzt.
		if (this.lastplayedcard == letzterZug()) {
			this.geierKarten.set(this.geierKarten.indexOf(Integer.valueOf(naechsteKarte)),
					this.letzteGKarte + naechsteKarte);
		}
		float geierKartenM = this.mittelwert(this.geierKarten); // hier wird der Mittelwert von den Geier Karten
																// gespeichert
		float meineKartenM = this.mittelwert(this.meineKarten); // hier wird der Mittelwert von den meine Karten
																// gespeichert
		// Array für die karten die gespielt werden können
		ArrayList<Integer> cardTempArray = new ArrayList<Integer>();
		int card;
		if (naechsteKarte < geierKartenM) {
			for (int i : this.meineKarten) {
				if (i <= meineKartenM) {
					cardTempArray.add(i);
				}
			}
		} else {
			for (int i : this.meineKarten) {
				if (i >= meineKartenM) {
					cardTempArray.add(i);
				}
			}
		}
		// hier wird eine Karte zufällig ausgesucht und zurückgegeben und von den Arrays
		// gelöscht
		card = cardTempArray.remove((int) (Math.random() * (cardTempArray.size() - 1)));
		this.meineKarten.remove(Integer.valueOf(card));
		this.geierKarten.remove(Integer.valueOf(naechsteKarte));
		this.lastplayedcard = card;
		return card;
	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode mittelwert() und da wird den Mittelwert berechnet,
	// den ich für meine Strategie brauche.
	//
	// =================================================================================================================================
	// =================================================================================================================================

	private float mittelwert(ArrayList<Integer> list) {

		int sum = 0;
		for (int item : list) {
			sum += item;
		}
		return sum / list.size();
	}

}
