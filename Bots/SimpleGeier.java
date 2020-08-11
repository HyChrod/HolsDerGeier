package de.FScheunert.HolsDerGeier.Bots;

import java.util.ArrayList;
import java.util.HashMap;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;


public class SimpleGeier extends HolsDerGeierSpieler {
	private HashMap<Integer, PatternRecognition> pattern = new HashMap<Integer, PatternRecognition>();
	
	private int playerPosition;
	private ArrayList<Integer> myCards = new ArrayList<Integer>();
	private HashMap<Integer, Integer> enemyCards = new HashMap<Integer, Integer>();
	private ArrayList<Integer> prevGeierCard = new ArrayList<Integer>();
	private ArrayList<Integer> cardNotPlayed = new ArrayList<Integer>();
	private int geierCard;
	private int nextCard;
	private int prevCard;
	private boolean firstRoundDone = false;

	public SimpleGeier() {}


	@Override
	public void reset() {
		if(playerPosition == 0) {
			prevCard = getHdg().letzterZug(1);
			if(prevCard > -6) collectData();
		}
		if(enemyCards.size() > 0) updatePattern();
		cardNotPlayed.clear();
		for(int counter = 1; counter < 16; counter++) {
			cardNotPlayed.add(counter);
		}
		
		enemyCards.clear();
		myCards.clear();
	}

	@Override
	public int gibKarte(int naechsteKarte) {
 		playerPosition = getNummer();
 		if(firstRoundDone) prevCard = nextCard;
 		nextCard = naechsteKarte;
		prevGeierCard.add(naechsteKarte);
		

		if(firstRoundDone) {
			collectData();
		}
		firstRoundDone = true;
		
		selectStrategy();
		myCards.add(geierCard);
 		return cardNotPlayed.remove(cardNotPlayed.indexOf(findNextValidCard(geierCard)));
	}
	
	private void collectData() {
		if (playerPosition == 0) {
				if(cardNotPlayed.size() == 1)
					prevCard = cardNotPlayed.get(0);
				enemyCards.put(prevCard, getHdg().letzterZug(1));
		} else {
			enemyCards.put(prevCard, getHdg().letzterZug(0));
		}
	}
	
	private void updatePattern() {
		if (pattern.size() != enemyCards.size()) {
			for (HashMap.Entry<Integer, Integer> entry : enemyCards.entrySet()) {
				pattern.put(entry.getKey(), new PatternRecognition(entry.getValue()));
			}
		} else {
			for (HashMap.Entry<Integer, Integer> entry : enemyCards.entrySet()) {
				PatternRecognition pr = pattern.get(entry.getKey());
				if(pr != null)
				pr.update(entry.getValue());
			}
		}
	}
	
	private void selectStrategy() {
		if(pattern.size() > 0) { dataStrategy();}
		else { simpleStrategy();}
	}
	
	private void dataStrategy() {
		if(pattern.get(nextCard) == null) {
			simpleStrategy();
		}else {
			PatternRecognition pr = pattern.get(nextCard);
			if(pr.getEnemyCard() == 15) {
				geierCard = 1;
			}else {
				geierCard = pr.getEnemyCard() + 1;
			}
		}
		//If 50/50 from enemy Card
		if(myCards.contains(geierCard) && pattern.containsKey(nextCard)) {
			HashMap<Integer, Integer> map = pattern.get(nextCard).getSortedHashMap(pattern.get(nextCard).getData());
			for(int key : map.keySet()) {
				if(!myCards.contains(key)) {
					geierCard = key;
					break;
				}
			}
		}
	}
	
	private void simpleStrategy() {
		if(nextCard < 0) {
			geierCard = nextCard + 6;
		}else {
			geierCard = nextCard + 5;
		}
	}
	
	private int findNextValidCard(int currentCard) {
		// Ist die Karte noch vorhanden, gib diese zurück
		if(cardNotPlayed.contains(currentCard)) return currentCard;
		// Ist die Karte größer, als die höchste gültige, versuche es mit der nächst niedrigeren
		if(currentCard > cardNotPlayed.get(cardNotPlayed.size()-1)) return findNextValidCard(currentCard-1);
		// Sollte die Karte kleiner sein, als die niedrigste gültige, versuche es mit der nächst höheren
		if(currentCard < cardNotPlayed.get(0)) return findNextValidCard(currentCard+1);
		return cardNotPlayed.get(0);
	}


}
