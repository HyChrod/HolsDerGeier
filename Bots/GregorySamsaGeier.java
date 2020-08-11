package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * Shoutout geht raus an Florenzo und Fabio die Ehrenmänner für die Hilfe. Dieser Geier hat
 * keinen Konflikt mit seinem Vater und verlernt auch nicht das Menschsein,
 * sondern erlernt es vielmehr (oder er probiert es wenigstens).
 * 
 * @author Georg Busch
 * @version 10.01.2020
 */

public class GregorySamsaGeier extends HolsDerGeierSpieler {
	/** gameNo: Zähler für Spielnummer.*/
	  int gameNo = 0;
	/** Methode, welche die Spielnummer hochzählt.*/
	private void incGameNo() {
		gameNo++;
	}

	/** myNum: Meine Spielernummer
	// opNum: Gegnerische Spielernummer
	// roundNum: Rundennummer innerhalb eines Spiels*/
	private int myNum, opNum, roundNum = 0;
	/** opCardLastRound: Karte, die der Gegner letze Runde gespielt hat
	// geLastRound: Karte, die der Geier letze Runde gelegt hat
	// geNow: Geierkarte dieses Zuges*/
	private   int opCardLastRound, geLastRound, geNow = 0; 

	/** Methode, welche die letze Gegnerkarte speichert.*/
	private void setOpCardLastRound() {
		opCardLastRound = getHdg().letzterZug(opNum);
	}

	/** Methode, welche die letze Geierkarte speichert.*/
	private void setGeierLastRound() {
		geLastRound = geNow;
	}

	/** Methode, welche die Spielernummern festlegt.*/
	private void setPlayerNumbers() {
		myNum = getNummer();
		if (myNum == 1) {
			opNum = 0;
		} else {
			opNum = 1;
		}
	}

	/** Methode, welche die Rundennummer hochzählt.*/
	private void incRoundNumber() {
		roundNum++;
	}

	/** gCard01 - gCard15: Arrays, welche die Haufigkeit einer Gegnerkarte (in Verbindung mit der
	einer bestimmten Geierkarte=> in Kobination mit rememberedCardsXX HashMap (s. unten)) speichern .*/
	private int[] gCard01 = new int[15], gCard02 = new int[15], gCard03 = new int[15], gCard04 = new int[15], gCard05 = new int[15],
			gCard06 = new int[15], gCard07 = new int[15], gCard08 = new int[15], gCard09 = new int[15], gCard10 = new int[15],
			gCard11 = new int[15], gCard12 = new int[15], gCard13 = new int[15], gCard14 = new int[15], gCard15 = new int[15];
	/** hand: Meine Kartenhand.*/
	private ArrayList<Integer> hand = new ArrayList<Integer>();
	/** Methode, welche meine Hand von neuem füllt und die Karten der Größe nach sortiert.*/
	private void fillHand() {
		for (int i = 1; i <= 15; i++)
			hand.add(i);
		Collections.sort(hand);
	}

	/** HashMaps, welche zu jeder Geierkarte die Gegnerkarte und ihre
	Wahrscheinlichkeiten speichern (in Kombination mit gCardXX Array (s. oben)). Statisch, damit sie Spielübergreifend bestehen bleiben und nicht zurückgesetzt werden, wenn ein neues Objekt erstellt wird */
	  private HashMap<Integer, Integer> rememberedCards01 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards02 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards03 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards04 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards05 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards06 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards07 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards08 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards09 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards10 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards11 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards12 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards13 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards14 = new HashMap<Integer, Integer>();
	  private HashMap<Integer, Integer> rememberedCards15 = new HashMap<Integer, Integer>();
	
	/** Methode, welche den Spieler zurücksetzt.*/
	@Override
	public void reset() {
		hand.clear();
		incGameNo();
		setPlayerNumbers();
		fillHand();
		roundNum = 0;
		randomCounter();
	}

	/** Methode, welche entscheidet, welche Karte mein Spieler in Abhängikeit der nächsten Geierkarte spielt. Außerdem: legt letze Geierkarte fest,
	 * legt letze Gegnerkarte fest, erhöht die Rundenzahl, merkt sich die Gegnerkarte, wechselt nach 5 Spielen von einer statisches zu einer dynamischen Strategie.*/
	@Override
	public int gibKarte(int naechsteKarte) {
		setGeierLastRound();
		geNow = naechsteKarte;
		setOpCardLastRound();
		incRoundNumber();
		setMemory(geLastRound);
		int returnCard = decideStrategy(naechsteKarte);
		return returnCard;
	}

	/**Methode, welche rememberedCardsXX (HashMap) und gCardXX (Array) miteinander kombiniert, um sich die Gegnerkarte und ihre Häufigkeit in Abhängigkeit der Geierkarte zu merken.*/
	private void setMemory(int geLastR) {
		int geLast;
		int comUp=0;
		if (geLastR < 0) {
			geLast = geLastR + 6;
		} else {
			geLast = geLastR + 5;
		}
		if (roundNum > 1 && opCardLastRound != 0) {
			switch (geLast) {
			case 0:
				break;
			case 1:
				comUp = gCard01[opCardLastRound - 1] + 2;
				gCard01[opCardLastRound - 1] = comUp;
				rememberedCards01.put(opCardLastRound, comUp);
				break;
			case 2:
				comUp = gCard02[opCardLastRound - 1] + 2;
				gCard02[opCardLastRound - 1] = comUp;
				rememberedCards02.put(opCardLastRound, comUp);
				break;
			case 3:
				comUp = gCard03[opCardLastRound - 1] + 2;
				gCard03[opCardLastRound - 1] = comUp;
				rememberedCards03.put(opCardLastRound, comUp);
				break;
			case 4:
				comUp = gCard04[opCardLastRound - 1] + 2;
				gCard04[opCardLastRound - 1] = comUp;
				rememberedCards04.put(opCardLastRound, comUp);
				break;
			case 5:
				comUp = gCard05[opCardLastRound - 1] + 2;
				gCard05[opCardLastRound - 1] = comUp;
				rememberedCards05.put(opCardLastRound, comUp);
				break;
			case 6:
				comUp = gCard06[opCardLastRound - 1] + 2;
				gCard06[opCardLastRound - 1] = comUp;
				rememberedCards06.put(opCardLastRound, comUp);
				break;
			case 7:
				comUp = gCard07[opCardLastRound - 1] + 2;
				gCard07[opCardLastRound - 1] = comUp;
				rememberedCards07.put(opCardLastRound, comUp);
				break;
			case 8:
				comUp = gCard08[opCardLastRound - 1] + 2;
				gCard08[opCardLastRound - 1] = comUp;
				rememberedCards08.put(opCardLastRound, comUp);
				break;
			case 9:
				comUp = gCard09[opCardLastRound - 1] + 2;
				gCard09[opCardLastRound - 1] = comUp;
				rememberedCards09.put(opCardLastRound, comUp);
				break;
			case 10:
				comUp = gCard10[opCardLastRound - 1] + 2;
				gCard10[opCardLastRound - 1] = comUp;
				rememberedCards10.put(opCardLastRound, comUp);
				break;
			case 11:
				comUp = gCard11[opCardLastRound - 1] + 2;
				gCard11[opCardLastRound - 1] = comUp;
				rememberedCards11.put(opCardLastRound, comUp);
				break;
			case 12:
				comUp = gCard12[opCardLastRound - 1] + 2;
				gCard12[opCardLastRound - 1] = comUp;
				rememberedCards12.put(opCardLastRound, comUp);
				break;
			case 13:
				comUp = gCard13[opCardLastRound - 1] + 2;
				gCard13[opCardLastRound - 1] = comUp;
				rememberedCards13.put(opCardLastRound, comUp);
				break;
			case 14:
				comUp = gCard14[opCardLastRound - 1] + 2;
				gCard14[opCardLastRound - 1] = comUp;
				rememberedCards14.put(opCardLastRound, comUp);
				break;
			case 15:
				comUp = gCard15[opCardLastRound - 1] + 2;
				gCard15[opCardLastRound - 1] = comUp;
				rememberedCards15.put(opCardLastRound, comUp);
				break;
			}
		}
	}

	/** Methode, welche nach 5 Spielen von einer statischen zu einer dynamischen Strategie wechselt.*/
	private int decideStrategy(int gCard) {
		if (gameNo <= 5) {
			int returnCard = playBaitStrat(gCard);
			return returnCard;
		} else {
			int returnCard = youActivatedMyTrapCard(gCard);
			return returnCard;
		}
	}

	/** Statische Strategie*/
	private int playBaitStrat(int gCard) {
		if (gCard < 0) {
			int myCard = (hand.remove(hand.indexOf(findNextValidCard(gCard + 6))));
			return myCard;
		} else {
			int myCard = (hand.remove(hand.indexOf(findNextValidCard(gCard + 5))));
			return myCard;
		}
	}

	/** Dynamische Strategie, welche mit den Informationen aus dem Gedächtnis (s. unten returnCardFromMemory()) entscheidet, welche Karte der Spieler legen soll. (Gegnerkarte + 2)*/
	private int youActivatedMyTrapCard(int gCard) {
		int enemyCard = returnEnemyCardfromMemory(gCard);
		int myCard = (hand.remove(hand.indexOf(findNextValidCard(enemyCard + 2))));
		return myCard;
	}

	/** Holt die Gegnerkarte, die bei der gelegten Geierkarte die größte Häufigkeit besitzt.*/
	private int returnEnemyCardfromMemory(int geCard) {
		int maxVal = 0;
		int geLast;
		if (geCard < 0) {
			geLast = geCard + 6;
		} else {
			geLast = geCard + 5;
		}
		switch (geLast) {
		case 1:
			int max_key01 = Collections.max(rememberedCards01.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key01;
			break;
		case 2:
			int max_key02 = Collections.max(rememberedCards02.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key02;
			break;
		case 3:
			int max_key03 = Collections.max(rememberedCards03.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key03;
			break;
		case 4:
			int max_key04 = Collections.max(rememberedCards04.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key04;
			break;
		case 5:
			int max_key05 = Collections.max(rememberedCards05.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key05;
			break;
		case 6:
			int max_key06 = Collections.max(rememberedCards06.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key06;
			break;
		case 7:
			int max_key07 = Collections.max(rememberedCards07.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key07;
			break;
		case 8:
			int max_key08 = Collections.max(rememberedCards08.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key08;
			break;
		case 9:
			int max_key09 = Collections.max(rememberedCards09.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key09;
			break;
		case 10:
			int max_key10 = Collections.max(rememberedCards10.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key10;
			break;
		case 11:
			int max_key11 = Collections.max(rememberedCards11.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key11;
			break;
		case 12:
			int max_key12 = Collections.max(rememberedCards12.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key12;
			break;
		case 13:
			int max_key13 = Collections.max(rememberedCards13.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key13;
			break;
		case 14:
			int max_key14 = Collections.max(rememberedCards14.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key14;
			break;
		case 15:
			int max_key15 = Collections.max(rememberedCards15.entrySet(), Map.Entry.comparingByValue()).getKey();
			maxVal = max_key15;
			break;
		}
		return maxVal;
	}

	/** Methode, welche überprüft, ob die gewünschte Karte noch verfügbar ist. Wenn nicht sucht sie eine Alternative.*/
	private int findNextValidCard(int currentCard) {
		// Ist die Karte noch vorhanden, gib diese zurück
		if (hand.contains(currentCard))
			return currentCard;
		// Ist die Karte größer, als die höchste gültige, versuche es mit der nächst
		// niedrigeren
		if (currentCard > hand.get(hand.size() - 1))
			return findNextValidCard(currentCard - 1);
		// Sollte die Karte kleiner sein, als die niedrigste gültige, versuche es mit
		// der nächst höheren
		if (currentCard < hand.get(0))
			return findNextValidCard(currentCard + 1);
		return hand.get(0);
	}

	/**Methode, welche im Fall einer "Gedächtnislücke" am Ende eines Spieles, einen Standartwert einsetzt, um keine Probleme zu bereiten.*/
	private void randomCounter() {
		if (rememberedCards01.isEmpty())
			rememberedCards01.put(1, 1);
		if (rememberedCards02.isEmpty())
			rememberedCards02.put(1, 1);
		if (rememberedCards03.isEmpty())
			rememberedCards03.put(1, 1);
		if (rememberedCards04.isEmpty())
			rememberedCards04.put(1, 1);
		if (rememberedCards05.isEmpty())
			rememberedCards05.put(1, 1);
		if (rememberedCards06.isEmpty())
			rememberedCards06.put(1, 1);
		if (rememberedCards07.isEmpty())
			rememberedCards07.put(1, 1);
		if (rememberedCards08.isEmpty())
			rememberedCards08.put(1, 1);
		if (rememberedCards09.isEmpty())
			rememberedCards09.put(1, 1);
		if (rememberedCards10.isEmpty())
			rememberedCards10.put(1, 1);
		if (rememberedCards11.isEmpty())
			rememberedCards11.put(1, 1);
		if (rememberedCards12.isEmpty())
			rememberedCards12.put(1, 1);
		if (rememberedCards13.isEmpty())
			rememberedCards13.put(1, 1);
		if (rememberedCards14.isEmpty())
			rememberedCards14.put(1, 1);
		if (rememberedCards15.isEmpty())
			rememberedCards15.put(1, 1);

	}
}
