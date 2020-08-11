package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;
import java.util.HashMap;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * The Class FabisGeier, is part of a game called <a href="https://de.wikipedia.org/wiki/Hol%E2%80%99s_der_Geier"> "Hol's der Geier" </a> <p>
 * It's part of a project for programming 1 where every
 * student needs to create his own player class. They then
 * will be tested in a tournament setting.
 * 
 * @author Fabian Zahn
 * @version 1.0
 * @since 2020-01-01
 */
public class FabisGeier extends HolsDerGeierSpieler{

	/** The point cards that have been played during each round. */
	private ArrayList<Integer> pointCards = new ArrayList<>();
	
	/** The enemy cards that have been played during each round. */
	private ArrayList<Integer> enemyCards = new ArrayList<>(); 
	
	/** The cards that have not been played by me, preventing me from playing the same card twice. */
	private ArrayList<Integer> cardsNotPlayed = new ArrayList<>();
	
	/** The enemy pattern, created by linking the point cards and the cards played by the enemy.  */
	private HashMap<Integer, Integer> enemyPattern = new HashMap<>();
	
	/** The player position in the game. */
	private int playerPosition;
	
	/** The enemy position. */
	private int enemyPosition;
	
	/** The card chosen by {@link #matchingStrategy(int)}. */
	private int cardSimple;
	
	/**  The card chosen by {@link #dataStrategy(int)}. */
	private int cardData;
	
	/** The card chosen by {@link #random()}. */
	private int cardRandom;
	
	/** The card I'll be playing. */
	private int myCard;
	
	/** The number of games. */
	private int numberOfGames = 0;
	
	/** The number of rounds. */
	private int numberOfRounds = 0;
	
	/** The secret strategy played in the last round. */
	private SecretStrategy secretStrategy;
	
	/**
	 * Resets the lists that have been used during each round.
	 * Clears all arrays that have been filled during the round and fills up the array
	 * with the cards that can be played next round. Calls Method to add the last card
	 * played by the enemy and creates the enemy pattern (linking point with enemy cards).
	 * 
	 */
	@Override
	public void reset() {
		playerPosition = getNummer();
		enemyPosition = playerPosition ^ 1;
		if(!enemyCards.isEmpty()) {
			checkLastEnemyCard();
			createEnemyPattern();
		}
		numberOfGames++;
		
		pointCards.clear();
		enemyCards.clear();
		cardsNotPlayed.clear();
		for(int counter = 1; counter < 16; counter++) {
			cardsNotPlayed.add(counter);
		}
		if(secretStrategy == null)
			secretStrategy = new SecretStrategy( playerPosition);
	}

	/**
	 * Gib karte (give card) is used to select and return the card we want to play.
	 * Adds the current point card from the the card to an ArrayList.
	 * Then calls the {@link #addEnemyCard()}addEnemyCard Method which gets the right card the enemy
	 * played (playerPosition either 1 or 0 for the enemy). After that the  {@link #selectCard(int)}selectStrategy
	 * Method which decides what strategy I'll be playing. After everything is done, the
	 * Method returns the card I want to play and checks if the card i want to play hasn't
	 * been played before. The card is removed from the cardsNotPlayed ArrayList in the return function.
	 *
	 * @param currentPointCard the point card from the game
	 * @return the card I want to play in this round
	 */
	@Override
	public int gibKarte(int currentPointCard) {
		int[] gamePoints = getHdg().getGamePoints();
		/*
		if(gamePoints[playerPosition] < gamePoints[enemyPosition] && numberOfGames == 100) {
			numberOfRounds++;
			if(numberOfRounds == 14) {
				secretStrategy.start();
			}
		}*/
		pointCards.add(currentPointCard);
		addEnemyCard();
		selectStrategy(currentPointCard);
		
		return cardsNotPlayed.remove(cardsNotPlayed.indexOf(findNextValidCard(myCard)));
	}
	
	/**
	 * Select the Strategy that is played for each round in the game.
	 * If the points - the negative point card in the game is greater or
	 * equal to the min required to win the {@link #cardRandom} is select.
	 * If {@link #testIfStatic()} return true the {@link #cardData} is select.
	 * Otherwise we play the {@link #cardSimple}.
	 *
	 * @param currentPointCard the current point card for the round
	 */
	private void selectStrategy(int currentPointCard) {
		selectCard(currentPointCard);
		int[] playerPoints = getHdg().getPlayerPoints();
		if((playerPoints[playerPosition] - negativPointCards() ) >= 21) {
			myCard = cardRandom;
		}
		else if(testIfStatic()) {
			myCard = cardData + 1;
		}else if(cardData != 0) {
			myCard = cardData + 1;
		}else {
			myCard = cardSimple;
		}
	}
	
	/**
	 * Check if in the last rounds the same cards were played as in this rounds.
	 * If the cards have stayed the same then the enemy might play static.
	 * If the card have changed the enemy might not play static and the default
	 * strategy {@link #matchingStrategy(int)} is played.
	 *
	 * @return true, if same card was played before
	 * false, if the not the same card was played
	 */
	private boolean testIfStatic() {
		boolean isStatic = false;
		if(enemyPattern.isEmpty() || enemyCards.isEmpty())
			return isStatic;
		
		for(int counter = 0; counter < enemyCards.size(); counter++) {
			int pointCard = pointCards.get(counter);
			if(enemyCards.get(pointCards.indexOf(pointCard)).equals(enemyPattern.get(pointCard))) {
				isStatic = true;
			}else {
				isStatic = false;
				break;
			}
		}
		return isStatic;
	}
	
	/**
	 * Calculate the negative point cards that haven't been played.
	 * Used to determine if {@link #random()} strategy is played.
	 *
	 * @return the sum of the negative point card in the game
	 */
	private int negativPointCards() {
		int numberOfNegativCards = 0;
		for(int pointCard : pointCards) {
			if(pointCard < 0)
				numberOfNegativCards += pointCard;
		}
		return 15 + numberOfNegativCards;
	}

	/**
	 * Select a card from every strategy.
	 * The {@link #dataStrategy(int)} will select a card after an enemy
	 * pattern was created. {@link #matchingStrategy(int)} and {@link #random()}
	 * always select a card.
	 *
	 * @param currentPointCard the current point card
	 */
	private void selectCard(int currentPointCard) {		
		if(!enemyPattern.isEmpty()) {
			cardData = dataStrategy(currentPointCard);
		}
		
		cardSimple = matchingStrategy(currentPointCard);
		
		cardRandom = random();
	}
	
	/**
	 * Select a random card from the cards that haven't been played.
	 *
	 * @return a random card.
	 */
	private int random() {
		int random;
		random = (int) (Math.random()* cardsNotPlayed.size()) + 1;
		random = findNextValidCard(random);
		return random;
	}
	
	/**
	 * Data strategy is used after the second round based on the enemies moves.
	 * In this function we look at which card has been played by the enemy
	 * during the last round and then select the card he's played for
	 * the point card but with a + 1 (next higher card). 
	 *
	 * @param currentPointCard the current point card
	 * @return the enemy card for the current point card + 1
	 */
	private int dataStrategy(int currentPointCard) {
		int enemyCard = enemyPattern.get(currentPointCard);
		return enemyCard + 1;
	}
	
	/**
	 * Finds the next valid card to play.
	 * This method is recursive. If the card we want to play
	 * has not been played we just return it (first if statement).
	 * If the card all ready has been played look for the next valid 
	 * card starting at the highest and going down (second if statement).
	 * If the card has been played and the current card is lower than 
	 * the lowest card, play the next higher card.
	 * If we don't find a card that fits the rules play the a card we
	 * havent't played (last return statement).
	 * 
	 *
	 * @param currentPointCard the current point card
	 * @return the card we want to play
	 */
	private int findNextValidCard(int currentPointCard) {
		if(cardsNotPlayed.contains(currentPointCard)) return currentPointCard;
		if(currentPointCard > cardsNotPlayed.get(cardsNotPlayed.size()-1)) return findNextValidCard(currentPointCard-1);
		if(currentPointCard < cardsNotPlayed.get(0)) return findNextValidCard(currentPointCard+1);
		return cardsNotPlayed.get(0);
	}
	
	/**
	 * Creates a pattern of the enemies moves.
	 * Combines the point cards with the card the enemy has played for them. 
	 * During testing it turned out that only saving the enemy moves 
	 * from the round before yielded the best results. That's why counting
	 * how often the enemy has played a card has been removed.
	 */
	private void createEnemyPattern() {
		for(int counter = 0; counter < pointCards.size(); counter++) {
				enemyPattern.put(pointCards.get(counter), enemyCards.get(counter));
		}
	}
	
	/**
	 * Check the last card played by the enemy.
	 * Since the last card is played before the {@link #letzterZug()}
	 * function is called we have to look for the card that the
	 * enemy hasn't played. The only card he could have played is
	 * the one missing from 1 to 15 available cards. By counting
	 * up with a for loop we can check which card is missing. 
	 */
	private void checkLastEnemyCard() {
		for(int counter = 1; counter <= 15; counter++) {
			if(!enemyCards.contains(counter)) {
				enemyCards.add(counter);
				break;
			}
		}
	}
	
	/**
	 * Adds the enemy card to an ArrayList.
	 * Depending if we are player one (0) or player two (1).
	 * We have to call the {@link #letzterZug()}letzterZug function with different
	 * parameters. Since  {@link #letzterZug()}letzterZug returns -99 if called before
	 * the enemy has played a card (start of every round), the card
	 * will only be added to the ArrayList if it's a valid card. 
	 */
	private void addEnemyCard() {
		if (getHdg().letzterZug(enemyPosition) != -99) 
			enemyCards.add(getHdg().letzterZug(enemyPosition));
	}
	
	/**
	 * Simple strategy is used in the first round.
	 * Add + 6 to the point card if it'S less than 0
	 * otherwise add + 5 and set this as the card to
	 * play.
	 *
	 * @param currentPointCard the point card
	 * @return the current point card + 5 or + 6
	 */
	private int matchingStrategy(int currentPointCard) {
		if(currentPointCard < 0) {
			return currentPointCard + 6;
		}
		return  currentPointCard + 5;
	}


}
