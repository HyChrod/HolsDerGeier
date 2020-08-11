/**
*
* This class was made by Florian Scheunert
* 28.10.2019
*
**/
package de.FScheunert.HolsDerGeier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class HolsDerGeier {
	
	// Hier befinden sich die noch nicht gespielten Geier- und Mäusekarten
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	// Hier werden die von den Spielern bereits gelegten Karten gepeichert
	private HashMap<Integer, LinkedList<Integer>> playerCards = new HashMap<Integer, LinkedList<Integer>>();
	
	// Ein Array, welches die beiden Spieler beinhaltet
	private HolsDerGeierSpieler[] players = new HolsDerGeierSpieler[2];
	// Der aktuelle Punktestand beider Spieler
	public int[] playerPoints = new int[] {0, 0};
	// Hier wird dem Gewinner, nach beenden eines Spiel, ein Punkt hinzugefügt
	// Dies sorgt dafür, dass man mehere Runden hintereinander Spielen kann
	public int[] gamePoints = new int[] {0,0};
	
	public HolsDerGeier() {
		//Setzt den SecurityManager um Manipulation und cheaten zu verhindern
		System.setSecurityManager(new SecurityManager());
	}
	
	// Das spiel wird auf Starteinstellungen zurückgesetzt
	public void resetGame() {
		cards.clear();
		playerCards.clear();
		playerPoints[0] = 0;
		playerPoints[1] = 0;
		for(int i = -5; i < 11; i++) {
			if(i == 0) continue;
			cards.add(i);
		}
		players[0].reset();
		players[1].reset();
	}
	
	public void neueSpieler(HolsDerGeierSpieler player1, HolsDerGeierSpieler player2) {
		players[0] = player1;
		player1.register(this, 0);
		players[1] = player2;
		player2.register(this, 1);
	}
	
	// Hier kann nach dem letzten Zug eines Spielers gefragt werden
	public int letzterZug(int player) {
		return playerCards.containsKey(player) ? playerCards.get(player).getLast() : -99;
	}
	
	// Zieht die nächste Geierkarte
	private int getGeierCard() {
		return cards.remove(new Random().nextInt(cards.size()));
	}
	
	// Beginnt ein neues Spiel
	public void newGame() throws Exception {
		if(cards.isEmpty()) resetGame();
		if(players[0] == null || players[1] == null) throw new Exception("Players are missing!");
		System.out.println("----------------------------------");
		System.out.println("Neues Spiel gestartet: [" + gamePoints[0] + ":" + gamePoints[1] + "]");
		System.out.println("----------------------------------");
		runGame();
	}
	
	public int[] getGamePoints() {
		return new int[] {gamePoints[0], gamePoints[1]};
	}
	
	public int[] getPlayerPoints() {
		return new int[] {playerPoints[0], playerPoints[1]};
	}
	
	// Beinhaltet die Spielmachanik:
	private void runGame() throws Exception {
		// Punkte, welche aus einer vorherigen, unentschiedenen Runde gespeichert wurden
		int savedPoints = 0;
		// Solange wie es noch Karten zu spielen gibt, läuft diese Schleife durch
		while(!cards.isEmpty()) {
			// Anfragen einer neuen Geierkarte
			int geierCard = getGeierCard();
			int[] pCards = new int[2];
			
			HashMap<Integer, LinkedList<Integer>> hashedCards = new HashMap<Integer, LinkedList<Integer>>();
			for(int i = 0; i < 2; i++) {
				
				// Abfragen der Karten, welche der Spieler bereits gelegt hat
				LinkedList<Integer> playedCards = playerCards.containsKey(i) ? new LinkedList<Integer>(playerCards.get(i)) 
						: new LinkedList<Integer>();
				// Abfragen der nächsten Karte, die der Spieler legen möchte
				int toPlay = players[i].gibKarte(geierCard);
				
				/*
				 * Überprüfen ob;
				 * 1. Die gleiche Karte bereits gelegt wurde
				 * 2. Die Zahl zwischen 1 und 15 liegt
				 */
				if((playerCards.containsKey(i) && playerCards.get(i).contains(toPlay)) 
						|| toPlay < 1 || toPlay > 15) throw new Exception("Invalid card: " + toPlay + "\nThe card has already been played or is outside the valid range [1-15]");
				
				playedCards.add(toPlay);
				hashedCards.put(i, playedCards);
				pCards[i] = toPlay;
				
			}
			playerCards.put(0, hashedCards.get(0));
			playerCards.put(1, hashedCards.get(1));
			
			System.out.println("Ausgespielte Karte: " + geierCard);
			System.out.println("Zug erster Spieler: " + pCards[0]);
			System.out.println("Zug zweiter Spieler: " + pCards[1]);
			
			// Überprüfen des Ergebnisses und anpassen des Punktestandes
			boolean negative = geierCard < 0;
			if(pCards[0] > pCards[1]) playerPoints[negative ? 1 : 0] += (geierCard+savedPoints);
			else if(pCards[0] < pCards[1]) playerPoints[negative ? 0 : 1] += (geierCard+savedPoints);
			else {
				// Gegebenenfalls zwischenspeichern der Punkte, falls unentschieden
				savedPoints = geierCard;
				System.out.println("Unentschieden - Punkte wandern in die naechste Runde");
			}
			
			System.out.println(">> Spielstand: " + playerPoints[0] + " : " + playerPoints[1]);
			if(savedPoints != 0 && savedPoints != geierCard) savedPoints = 0;
		}
		int winner = playerPoints[0] > playerPoints[1] ? 1 : playerPoints[1] > playerPoints[0] ? 2 : 0;
		System.out.println("== Spieler " + winner + " hat gewonnen!");
		if(winner > 0) gamePoints[winner-1]++;
	}

}