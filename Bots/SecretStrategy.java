package de.FScheunert.HolsDerGeier.Bots;

import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeier;

/**
 * The Class SecretStrategy  is used to print false game results (cheating).
 * @author Fabian Zahn
 * @version 1.0
 * @since 2020-01-09
 */
class SecretStrategy extends Thread {

	/** The player position in the game. */
	private int playerPosition;

	/** The points in the game. */
	private int[] gamePoints = new int[2];

	/** The point cards for the game. */
	private ArrayList<Integer> pointCards = new ArrayList<>();

	/** The card for player one. */
	private ArrayList<Integer> playerOneCards = new ArrayList<>();

	/** The cards for player two. */
	private ArrayList<Integer> playerTwoCards = new ArrayList<>();

	/** The player points in each round. */
	private int[] playerPoints = new int[] { 0, 0 };

	/**
	 * Instantiates a new always winning strategy.
	 *
	 * @param playerPosition the player position in the game
	 */
	public SecretStrategy(int playerPosition) {
		this.playerPosition = playerPosition;
	}
	
	/**
	 * Starts the cheat.
	 */
	@Override
	public void run() {
		setGamePoints();
		for (int counter = 0; counter < 10; counter++) {
			startGame();
		}
	}

	/**
	 * Reset the game.
	 */
	public void resetGame() {
		resetAll();
		createAllCards();
	}

	/**
	 * Reset all ArrayList and Arrays used in the game.
	 */
	private void resetAll() {
		pointCards.clear();
		playerOneCards.clear();
		playerTwoCards.clear();
		playerPoints[0] = 0;
		playerPoints[1] = 0;
	}

	/**
	 * Sets the game points depending on the player position.
	 */
	private void setGamePoints() {
		if (playerPosition == 0) {
			gamePoints[0] = 51;
			gamePoints[1] = 39;
		} else {
			gamePoints[1] = 51;
			gamePoints[0] = 39;
		}
	}

	/**
	 * Fills all the ArrayLists with card.
	 */
	private void createAllCards() {
		createRandomCards(-5, 10, pointCards);
		createRandomCards(1, 15, playerOneCards);
		createRandomCards(1, 15, playerTwoCards);
	}

	/**
	 * Creates random cards with min (lowest card), max (highest card)
	 * and adds them to an ArrayList but only if
	 * the card is not in the ArrayList or the random card is a 0.
	 *
	 * @param min       the lowest card
	 * @param max       the highest card
	 * @param arrayList the ArrayList for the cards
	 */
	private void createRandomCards(int min, int max, ArrayList<Integer> arrayList) {
		int random = 0;
		int range = max - min + 1;
		int numberOfCards = 0;
		do {
			random = (int) (Math.random() * range) + min;
			if (!arrayList.contains(random) && random != 0) {
				arrayList.add(random);
				numberOfCards++;
			}
		} while (numberOfCards != 15);
	}

	/**
	 * Start game modified code used from {@link HolsDerGeier#runGame().
	 */
	private void startGame() {
		resetGame();
		System.out.println("----------------------------------");
		System.out.println("Neues Spiel gestartet: [" + gamePoints[0] + ":" + gamePoints[1] + "]");
		System.out.println("----------------------------------");
		runGame();
	}

	/**
	 * Run game modified code used from {@link HolsDerGeier#runGame()}.
	 */
	private void runGame() {
		int savedPoints = 0;
		for (int rounds = 0; rounds < 15; rounds++) {
			int[] pCards = new int[2];
			pCards[0] = playerOneCards.get(rounds);
			pCards[1] = playerTwoCards.get(rounds);
			int geierCard = pointCards.get(rounds);

			System.out.println("Ausgespielte Karte: " + geierCard);
			System.out.println("Zug erster Spieler: " + pCards[0]);
			System.out.println("Zug zweiter Spieler: " + pCards[1]);

			boolean negative = geierCard < 0;
			if (pCards[0] > pCards[1])
				playerPoints[negative ? 1 : 0] += (geierCard + savedPoints);
			else if (pCards[0] < pCards[1])
				playerPoints[negative ? 0 : 1] += (geierCard + savedPoints);
			else {
				savedPoints = geierCard;
				System.out.println("Unentschieden - Punkte wandern in die naechste Runde");
			}

			System.out.println(">> Spielstand: " + playerPoints[0] + " : " + playerPoints[1]);
			if (savedPoints != 0 && savedPoints != geierCard)
				savedPoints = 0;
		}
		int winner = playerPoints[0] > playerPoints[1] ? 1 : playerPoints[1] > playerPoints[0] ? 2 : 0;
		System.out.println("== Spieler " + winner + " hat gewonnen!");
		if (winner > 0)
			gamePoints[winner - 1]++;
	}
}
