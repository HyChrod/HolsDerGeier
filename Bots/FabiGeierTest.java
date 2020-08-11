package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

public class FabiGeierTest extends HolsDerGeierSpieler {
	
	private ArrayList<Integer> cardsNotPlayed = new ArrayList<>();
	
	private ArrayList<Integer> enemyHandCards = new ArrayList<>();
	
	private HashMap<Integer, HashMap<Integer, Integer>> enemyPatternFull = new HashMap<>();
	
	private ArrayList<Integer> pointCards = new ArrayList<>();
	
	private HashMap<Integer, Integer> predictions = new HashMap<>();
	
	private int playerPoints[];
	
	private int enemyPosition;
	
	private int rounds;
	
	private int currentPointCard;
	
	private int games = 0;
	
	private int myCard;
	
	
	@Override
	public void reset() {
		enemyPosition = getNummer() ^ 1;
		resetHandCards();
		rounds = 0;
		games++;
		if(games > 1) findPattern();
		System.out.println(enemyPatternFull.toString());
	}
	
	@Override
	public int gibKarte(int pointCard) {
		playerPoints = getHdg().getPlayerPoints();
		int enemyCard = getEnemyCard();
		rounds++;
		if(enemyCard != -99) {
			enemyHandCards.remove(enemyHandCards.indexOf(enemyCard));
			initPattern(enemyCard);
			if(enemyHandCards.size() == 1) {
				currentPointCard = pointCard;
				initPattern(enemyHandCards.get(0));
			}
		}
		
		
		currentPointCard = pointCard;
		myCard = selectStrategy();
		pointCards.remove(pointCards.indexOf(currentPointCard));
		
		return cardsNotPlayed.remove(cardsNotPlayed.indexOf(findNextValidCard(myCard)));
	}
	
	private void initPattern(int enemyCard) {
		if(games == 1 ) {
			createEnemyPattern(enemyCard);
		}else {
			updateEnemyPattern(enemyCard);
		}
	}
	
	private void resetHandCards() {
		cardsNotPlayed.clear();
		enemyHandCards.clear();
		pointCards.clear();
		for(int counter = 1; counter <= 15; counter++) {
			cardsNotPlayed.add(counter);
			enemyHandCards.add(counter);
		}
		for(int counter = -5; counter <= 10; counter++) {
			if(counter == 0) continue;
			pointCards.add(counter);
		}
	}
	
	private int getEnemyCard() {
		return getHdg().letzterZug(enemyPosition);
	}
	
	private int selectStrategy() {
		int predictionCard;

		if((playerPoints[getNummer()] - negativPointCards() ) >= 21) {
			return randomStrategy();
		}
		if(enemyHandCards.size() == 2) {
			return calculateBestOption();
		}
		if(games == 1 && enemyHandCards.size() != 2) {
			return matchingStrategy(currentPointCard);
		}
		if(games > 1 && enemyHandCards.size() != 2 && !enemyPatternFull.isEmpty()){
			predictionCard = betterDataStrategy();
			if(predictionCard - matchingStrategy(currentPointCard) > 3) {
				return matchingStrategy(currentPointCard);
			}
			return predictionCard;
		}
		return matchingStrategy(currentPointCard);
	}
	
                
	
	
	private int randomStrategy() {
		Random random = new Random();
		int rand = random.nextInt(cardsNotPlayed.size());
		return cardsNotPlayed.get(rand);
	}
	
	private int calculateBestOption() {
		int indexPointCard = pointCards.indexOf(currentPointCard);
		int indexMyCard = 0;
		if(cardsNotPlayed.get(0) < cardsNotPlayed.get(1)) {
			indexMyCard = 1;
		}
		if(pointCards.get(indexPointCard) > pointCards.get(indexPointCard ^ 1)) {
			return cardsNotPlayed.get(indexMyCard);
		}else {
			return cardsNotPlayed.get(indexMyCard ^ 1);
		}
	}
	
	private void findPattern() {
		ArrayList<Integer> mostPlayedCard = new ArrayList<>();
		ArrayList<Integer> averagePerCard = new ArrayList<>();
		ArrayList<Integer> staticCards = new ArrayList<>();
		for(int counter = -5; counter <= 10; counter++) {
			if(counter == 0) continue;
			HashMap<Integer, Integer> data = enemyPatternFull.get(counter);
			mostPlayedCard.add(Collections.max(data.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey());
			averagePerCard.add(calculateAverage(counter));
			staticCards.add(matchingStrategy(counter));
		}

		ArrayList<Integer> higherThanAverage = new ArrayList<>();
		ArrayList<Integer> sameAsAverage = new ArrayList<>();
		ArrayList<Integer> lowerThanAverage = new ArrayList<>();
		int differents = 5;
		
		for(int counter = -5; counter <= 10; counter++) {
			if(counter == 0) {
				differents = 4;
				continue;
			}
			HashMap<Integer, Integer> enemyMoves = enemyPatternFull.get(counter);
			higherThanAverage.add(0);
			sameAsAverage.add(0);
			lowerThanAverage.add(0);
			for(Entry<Integer, Integer> entry : enemyMoves.entrySet()) {
				if(entry.getKey() < averagePerCard.get(counter + differents)) {
					lowerThanAverage.set(counter + differents, lowerThanAverage.get(counter + differents) + entry.getValue());
				}
				if(entry.getKey() > averagePerCard.get(counter + differents)) {
					higherThanAverage.set(counter + differents, higherThanAverage.get(counter + differents) + entry.getValue());
				}else {
					sameAsAverage.set(counter + differents, sameAsAverage.get(counter + differents) + entry.getValue());
				}
			}
		}
		
		differents = 5;
		
		for(int counter = 0; counter < mostPlayedCard.size(); counter++) {
			if(counter == 5) differents = 4;
			System.out.println("Pointcard " + (counter - differents) 
							+ "\n the average was " + averagePerCard.get(counter)
							+ "\n the most played Card was " + mostPlayedCard.get(counter) 
							+ "\n " + higherThanAverage.get(counter) +" higher than the average was played \n " 
							+ sameAsAverage.get(counter) + " times the average \n "
							+ lowerThanAverage.get(counter) + " times lower than the average was played \n " 
							+ staticCards.get(counter) + " was the static value \n");
		}
		differents = 5;
		
		for(int counter = 0; counter < mostPlayedCard.size(); counter++) {
			if(counter == 5) differents = 4;
			if(lowerThanAverage.get(counter) > sameAsAverage.get(counter)) {
				predictions.put(counter - differents, averagePerCard.get(counter));
			}
			if(higherThanAverage.get(counter) > sameAsAverage.get(counter)) {
				predictions.put(counter - differents, mostPlayedCard.get(counter));
			}else {
				predictions.put(counter - differents, averagePerCard.get(counter));
			}
		}
		

		System.out.println("######### Prediction " + predictions.toString());
		
		
	}
	
	 float correlationCoefficient(int X[], int Y[], int n) {

		int sum_X = 0, sum_Y = 0, sum_XY = 0;
		int squareSum_X = 0, squareSum_Y = 0;

		for (int i = 0; i < n; i++) {
			// sum of elements of array X. 
			sum_X = sum_X + X[i];

			// sum of elements of array Y. 
			sum_Y = sum_Y + Y[i];

			// sum of X[i] * Y[i]. 
			sum_XY = sum_XY + X[i] * Y[i];
			
			// sum of square of array elements. 
			squareSum_X = squareSum_X + X[i] * X[i];
			squareSum_Y = squareSum_Y + Y[i] * Y[i];
		}

		// use formula for calculating correlation  
		// coefficient. 
		float corr = (float) (n * sum_XY - sum_X * sum_Y)
				/ (float) (Math.sqrt((n * squareSum_X - sum_X * sum_X) * (n * squareSum_Y - sum_Y * sum_Y)));

		return corr;
	}


	private int betterDataStrategy() {
		return predictions.get(currentPointCard);
	}
	
	private int calculateAverage(int pointCard) {
		HashMap<Integer, Integer> enemyMoves = enemyPatternFull.get(pointCard);
		double sum = 0;
		int numberOfEntries = 0;
		for(Entry<Integer, Integer> entry : enemyMoves.entrySet()) {
			numberOfEntries += entry.getValue();
			sum += entry.getKey() * entry.getValue();
		}
		sum /= numberOfEntries;
		return (int) Math.round(sum);
	}
	
	private void createEnemyPattern(int enemyCard) {
		HashMap<Integer, Integer> enemyMove = new HashMap<>();
		enemyMove.put(enemyCard, 1);
		enemyPatternFull.put(currentPointCard, enemyMove);

	}
	
	private void updateEnemyPattern(int enemyCard) {
		HashMap<Integer, Integer> enemyMove = enemyPatternFull.get(currentPointCard);
		if(enemyMove.containsKey(enemyCard)) {
			enemyMove.put(enemyCard, enemyMove.get(enemyCard) + 1);
			enemyPatternFull.put(currentPointCard, enemyMove);
		}else {
			enemyMove.put(enemyCard, 1);
			enemyPatternFull.put(currentPointCard, enemyMove);
		}
	}
	
	
	private int matchingStrategy(int pointCard) {
		if(pointCard < 0) return pointCard * - 1;
		return pointCard + 5;
	}

	
	private int findNextValidCard(int currentCard) {
		if(cardsNotPlayed.contains(currentCard)) return currentCard;
		if(currentCard > cardsNotPlayed.get(cardsNotPlayed.size()-1)) return findNextValidCard(currentCard-1);
		if(currentCard < cardsNotPlayed.get(0)) return findNextValidCard(currentCard+1);
		return cardsNotPlayed.get(0);
	}
	
	private int negativPointCards() {
		int numberOfNegativCards = 0;
		for(int pointCard : pointCards) {
			if(pointCard < 0)
				numberOfNegativCards += pointCard;
		}
		return 15 + numberOfNegativCards;
	}
	

}
