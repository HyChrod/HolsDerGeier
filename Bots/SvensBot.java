package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * Dieser Bot gibt eine Zufällige Zahl aus einer von drei Gruppen wieder.
 * Die zu verwendende Gruppe wird nach der grösse der Geier-/Mäusekarten entschieden.
 * Hinzu kommen sonderfälle für unentschieden.
 *
 * @author (Sven - Chrstian)
 * @version (23.12)
 */
public class SvensBot extends HolsDerGeierSpieler {

	//Meine Karten 1-15
	private ArrayList<Integer> mycards = new ArrayList<>();

	//Gegner Karten Historie
	private ArrayList<Integer> enemyCards = new ArrayList<>();

	//Ausgespielte Geier und Mäusekarten
	private ArrayList<Integer> playedCards = new ArrayList<>();

	//Meine Spielhistorie
	private ArrayList<Integer> myHistory = new ArrayList<>();
	private int playThis;
	private int oppNummer;

    /**methode die meine Karten ArrayList wiederauffüllt, die
     * gegner ArrayList leert und meine Spielernummer und die Gegnernummer herausfindet
     *
     */
	public void reset() {
		mycards.clear();
		for (int i = 1; i < 16; i++) {
			mycards.add(i);
		}
		enemyCards.clear();

		int meineNummer = getNummer();
		if (meineNummer == 1) {
			oppNummer = 0;
		} else {
			oppNummer = 1;
		}
	}

    /**
     * Die gibtKarte methode wird vom HolsDerGeier Spiel aufgerufen, um
     * herauszufinden welche Karte ich legen möchte.
     * Es werden hier die gelegten Geier und Mäusekarten in die playedCards ArrayList gespeichert
     * und der letzte Zug des Gegners in der enemyCards ArrayList gespeichert.
     * Es wird zudem die Strategie methode zur erzeugung meiner Karte aufgerufen
     */
	public int gibKarte(int naechsteKarte) {
		System.out.println(this.getClass());
		playedCards.add(naechsteKarte);
		enemyCards.add(getHdg().letzterZug(oppNummer));

		return Strategy(naechsteKarte);
	}

    /**
     * Die Strategie besteht aus 7 teilen. 4 davon sind Sonderfälle.
     * Alle sind in eine if-Schleife eingegliedert.
     * Der erste Teil ist der Sonderfall, wenn zu wenig Karten sind um die später verwendete Random generation zu verwenden.
     * Drei Fälle sind für unentschieden Situationen, wo die Summe der Ausgespielten Karten genommen wird
     * um zu entscheiden, ob die höchste übrige Karte(Summe>10),eine hohe(Summe>5) oder niedrige(Summe<=5) Karte gelegt werden soll
     * Die restlichen drei sind zur Entscheidung, ob aus der hächsten, mittleren oder neidrigsten Kategorie gelegt werden soll.
     * Die Kategorien teilen die Arraylist nach dem Index in die drei Kategorien auf. Da die Arraylist sortiert aufgefüllt wird,
     * sind in der kategorie der niedrigsten indexe auch die niedrigsten karten usw.
     */
	private int Strategy(int naechsteKarte) {
		//Gibt die letzten beiden Karten wieder, da Zufallsmethode nicht mehr möglich ist
		if ((mycards.size() - 1) < 3) {
			int lastCards = mycards.get(mycards.size() - 1);
			mycards.remove(mycards.size() - 1);
			return lastCards;
        //unentschieden Situation mit Summe>10 ruft methode auf um die höchste Karte zu legen
		} else if (((myHistory.size() - 1) > 0) && (enemyCards.get(enemyCards.size() - 1).equals(myHistory.get(myHistory.size() - 1)))
                && ((naechsteKarte + (playedCards.get(playedCards.size() - 2))) > 10)) {
			try {
				return playThis = playHighestCard();
			} catch (Exception e) {
				e.printStackTrace();
			}
        //unentschieden Situation mit Summe>5 ruft methode auf um eine hohe Karte zu legen
		} else if (((myHistory.size() - 1) > 0) && (enemyCards.get(enemyCards.size() - 1).equals(myHistory.get(myHistory.size() - 1)))
                && ((naechsteKarte + (playedCards.get(playedCards.size() - 2))) > 5)) {
			try {
				return playThis = highCards();
			} catch (Exception e) {
				e.printStackTrace();
			}
            //unentschieden Situation mit Summe<=5 ruft methode auf um eine niedrige Karte zu legen
		} else if (((myHistory.size() - 1) > 0) && (enemyCards.get(enemyCards.size() - 1).equals(myHistory.get(myHistory.size() - 1)))
                && ((naechsteKarte + (playedCards.get(playedCards.size() - 2))) <= 5)) {
			try {
				return playThis = lowCards();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Spielt Karten der höchsten Kategorie wenn -5,-4,7,8,9,10 ausgespielt wird
		} else if ((naechsteKarte < -3 || naechsteKarte > 6)) {
			try {
				playThis = highCards();

			} catch (Exception e) {
				e.printStackTrace();
			}
			//Spielt Karten der mittleren Kategorie wenn -3,-2,5,6 ausgespielt wird
		} else if (naechsteKarte < -1 || naechsteKarte > 4) {
			try {
				playThis = middleCards();


			} catch (Exception e) {
				e.printStackTrace();
			}
			//Spielt Karten der niedrigsten Kategorie wenn -1,1,2,3,4 ausgespielt wird
		} else {
			try {

				playThis = lowCards();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		int lastCard = playThis;
		myHistory.add(lastCard);
		return playThis;
	}
    //Die ArrayListe wird mit Vier Grenzen in drei Kategorien eingeteilt:
    //1.Kategorie(kleinste Karten):kleinster Index bis obere Grenze der kleinsten Kategorie
    //2.Kategorie(mittlere Karten):obere Grenze kleinste Kategorie +1 bis obere Grenze mittlere Kategorie
    //3.Kategorie(höchste Karten): obere Grenze mittlere Kategorie +1 bis höchster Index

    /**
     * Die Methode nimmt die obere Grenze der mittleren Kategorie+1 und die höchste Karte der ArrayList als Kategorie Grenzen
     * und gibt diese an die Random funktion, um einen zufälligen Index zurück zu bekommen.
     * Die dazugehörige Karte wird dann geholt, aus der ArrayListe entfernt und als zu legende Karte zurückgegeben
     */
	private int highCards() {
		int cardToPlay = random((maxAndMinMiddle() + 1), (mycards.size() - 1));
		int playingCards = mycards.get(cardToPlay);
		mycards.remove(cardToPlay);
		return playingCards;
	}
   /** Die Methode nimmt die obere Grenze der kleinsten Kategorie+1 und die obere Grenze der mittleren Kategorie+1 als Grenzen
     * und gibt diese an die Random funktion, um einen zufälligen Index zurück zu bekommen.
     * Die dazugehörige Karte wird dann geholt, aus der ArrayListe entfernt und als zu legende Karte zurückgegeben
    */
	private int middleCards() {
		int cardToPlay = random((maxAndMinLow() + 1), maxAndMinMiddle());
		int playingCards = mycards.get(cardToPlay);
		mycards.remove(cardToPlay);
		return playingCards;
	}
    /** Die Methode nimmt den niedrigsten Index und obere Grenze der niedrigsten Kategorie als Grenzen
     * und gibt diese an die Random funktion, um einen zufälligen Index zurück zu bekommen.
     * Die dazugehörige Karte wird dann geholt, aus der ArrayListe entfernt und als zu legende Karte zurückgegeben
     */
	private int lowCards() {
		int cardToPlay = random(0, maxAndMinLow());
		int playingCards = mycards.get(cardToPlay);
		mycards.remove(cardToPlay);
		return playingCards;

	}

    /**
     * gibt eine zufälligen index aus einer range von indexen wieder
     */
	private int random(int min, int max) {
		return ((int) ((Math.random() * ((max - min) + 1))) + min);
	}

    //Um die einteilung für jede Indexgrösse zu ermöglichen, muss sie immer durch drei teilbar sein.
    //dafür wird die Indexgrösse wenn nötig aufgefüllt, bis sie durch drei teilbar ist und nachher wieder vekleinert

	/**Nimmt die ArrayList-grösse und findet die obere Grenze der untersten Kategorie*/
     private int maxAndMinLow() {
		int indexSize = (mycards.size() - 1);
		int maxOne;
		if ((indexSize) < 3) {
			maxOne = indexSize;
			return maxOne;

		} else if ((indexSize) % 3 == 0) {
			maxOne = (indexSize) / 3;
			return maxOne;

		} else if ((indexSize) % 3 == 2) {
			maxOne = (((indexSize) + 1) / 3) - 1;
			return maxOne;

		} else {//1,4,7,10,13
			maxOne = (((indexSize) + 2) / 3) - 1;
			return maxOne;
		}
	}

    /**Nimmt die ArrayList-grösse und findet die obere Grenze der untersten Kategorie*/
	private int maxAndMinMiddle() {
		int indexSize = (mycards.size() - 1);
		int maxTwo;
		if ((indexSize) < 3) {
			maxTwo = indexSize;
			return maxTwo;

		} else if ((indexSize) % 3 == 0) {
			maxTwo = (((indexSize) / 3) * 2);
			return maxTwo;

		} else if (((indexSize) % 3 == 2)) {
			maxTwo = ((((indexSize) + 1) / 3) * 2) - 1;
			return maxTwo;

		} else {
			maxTwo = ((((indexSize) + 2) / 3) * 2) - 1;
			return maxTwo;
		}

	}

    /**holt den höchsten Index, dann den dazugehörigen Kartenwert, und entfernt diesen aus der ArrayList mit meinen Karten */
	private int playHighestCard() {
		int cardToPlay = mycards.size() - 1;
		int playingCards = mycards.get(cardToPlay);
		mycards.remove(cardToPlay);
		return playingCards;

	}


}