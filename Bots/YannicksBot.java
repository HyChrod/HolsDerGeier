package de.FScheunert.HolsDerGeier.Bots;
/**
 * 
 * Dieser Bot lernt durch eine satische Strategie
 * und verwendet dann eine angepasste dynamische Strategie
 * @author Yannick Stumpf 
 * 
 **/
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

public class YannicksBot extends HolsDerGeierSpieler {
	// Enthält alle Informationen über Spiele und Runden
	private HashMap<Integer, HashMap<Integer, Integer>> enemyPattern = new HashMap<Integer, HashMap<Integer, Integer>>();

	private ArrayList<Integer> pCards = new ArrayList<Integer>();// Initzalisierung der Variabeln
	private int games = 0;// Initzalisierung der Variabeln
	private int lastGCard = -99;// Initzalisierung der Variabeln
	private int pointToWin = 0;// Initzalisierung der Variabeln

	public void reset() { // Diese Methode setzt das Spiel auf den Anfangswert zurück
		if (!pCards.isEmpty())
			pCards.clear();
		for (int i = 1; i <= 15; i++)
			pCards.add(i);
		games++;
		pointToWin = 0;
		lastGCard = -99;
	}

//	Die Methode findNextValidCard sucht eine Alternative Karte solle meine Karte nicht vorhanden sein
	private int findNextValidCard(int currentCard) {
		if (pCards.contains(currentCard)) {
			return currentCard;
		}
		if (currentCard > pCards.get(pCards.size() - 1)) {
			return findNextValidCard(currentCard - 1);
		}
		if (currentCard < pCards.get(0)) {
			return findNextValidCard(currentCard + 1);
		}
		return pCards.get(0);
	}

	public int gibKarte(int aCard) {
		// Überprüft den aktuellen Rundenwert
		if (games > 1) {
			learnEnemysStrategy(lastGCard);//
		}
		if (letzterZug() == getHdg().letzterZug(getNummer() == 0 ? 1 : 0))// Vergleicht meinen letzten Zug mit der
																			// Gespielten Karten des Gengers
		{
			// Eine Bedingte Abfrage falls die aktuelle Ausgespielte Karte sich subtrahiert
			// mit der vorherigen Karte
			if (pointToWin == 0 && pointToWin != lastGCard) {
				pointToWin = lastGCard;
			} else {
				pointToWin += aCard;
			}
		} else {
			pointToWin = aCard;
		}
		lastGCard = aCard;
		int card = games > 10 ? playDynamicStrategy(aCard) : playStaticStrategy(aCard);
		return card;
	}

	private int playStaticStrategy(int aCard) {
		return aCard < 0 ? aCard + 6 : aCard + 5;
	}

//	Lernt die Strategie des Gegners aus den 10 Runden davor
	private void learnEnemysStrategy(int lastGCard) {

		if (lastGCard < -6) {
			return;
		}
		HashMap<Integer, Integer> playedCard = new HashMap<Integer, Integer>();// lokale Hashmap für die Methode
		if (enemyPattern.containsKey(lastGCard)) {// überprüft ob die Karte des Gegners in der Hashmap regestriert wurde
			playedCard = enemyPattern.get(lastGCard);
		}

		int enemyCard;
		if (getHdg().letzterZug(getNummer()) == 0)// Versucht vorher zu berechnen welche Karte der Gegner legen wird
		{
			enemyCard = 1;
		} else {
			enemyCard = 0;
		}

		int cardCounter = 0;
		if (playedCard.containsKey(enemyCard)) {
			cardCounter = playedCard.get(enemyCard);
		}

		playedCard.put(enemyCard, cardCounter + 1);
		enemyPattern.put(lastGCard, playedCard);
	}

	private int playDynamicStrategy(int aCard) {
		if (pointToWin > 0 && aCard < 0) {
			return pCards.remove(0);
		}
		HashMap<Integer, Integer> cardSet = enemyPattern.get(aCard);

		int enemysCard = Collections.max(cardSet.entrySet(), Map.Entry.comparingByValue()).getKey();
		int cardToPlay = enemysCard + 1;
		int alterCard = playStaticStrategy(aCard);

		if (alterCard > (cardToPlay + 5)) {
			cardToPlay = alterCard;
		}

		if (!pCards.contains(cardToPlay)) {// durch wahrscheinlichkeit ermittelt gibt er hier meine karte +1
			cardToPlay = findNextValidCard(cardToPlay);
		}
		return pCards.remove(pCards.indexOf(cardToPlay));// Gibt den Wert der Karte aus der Liste wieder
	}

}