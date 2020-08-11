package de.FScheunert.HolsDerGeier.Bots;

import java.util.ArrayList;
import java.util.HashMap;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;


public class ApacheKillerKampfGeier extends HolsDerGeierSpieler {
    private HashMap<Integer, Integer> enemyStrategyHashMap;
    private HashMap<Integer, Integer> enemyStrategyHashMapCounter;
    private ArrayList<Integer> notPlayedEnemy = new ArrayList<>();
    private ArrayList<Integer> notPlayed = new ArrayList<>();
    private ArrayList<Integer> stillWinnableCards = new ArrayList<>();
    private int[] gamePoints;
    private int playerId;
    private int strategy;
    private int loosesInRow;
    private int lastCard;
    private int learningRounds;

    public ApacheKillerKampfGeier() {
        enemyStrategyHashMap = new HashMap<>();
        enemyStrategyHashMapCounter = new HashMap<>();
        gamePoints = new int[]{};
        loosesInRow = 0;
        learningRounds = 0;
        lastCard = 0;
        strategy = 0;
    }

    public void reset() {
        notPlayedEnemy.clear();
        for (int i = 15; i > 0; i--)
            notPlayedEnemy.add(i);
        stillWinnableCards.clear();
        for (int i = 10; i > -6; i--)
            stillWinnableCards.add(i);
        notPlayed.clear();
        for (int i = 15; i > 0; i--)
            notPlayed.add(i);
        lastCard = 0;
    }

    public void strategyChanger() {
        // check if last game was win or lose
        if (gamePoints.length == 2) {
            if (getHdg().getGamePoints()[playerId] - gamePoints[playerId] == 1) { // if own bot wins
                loosesInRow = 0;
            } else if (getHdg().getGamePoints()[playerId ^ 1] - gamePoints[playerId ^ 1] == 1) { // if enemy wins
                loosesInRow += 1;
            }
        }
        gamePoints = getHdg().getGamePoints();

        // changing strategy after the first 5 rounds
        if (strategy == 0 && learningRounds > (15 * 3)) {
            strategy = 1;
            loosesInRow = 0;
        } // change strategy when learning fails (enemy not using static bot)
        else if (strategy == 1 && loosesInRow > 2) {
            strategy = 2;
            loosesInRow = 0;
        }  // change strategy when percent strategy fails
        else if (strategy == 2 && loosesInRow > 2) {
            strategy = 3;
            loosesInRow = 0;
        } // if nothing works reset system strategy
        else if (strategy == 3 && loosesInRow > 2) {
            strategy = 0;
            loosesInRow = 0;
            learningRounds = 0;
            enemyStrategyHashMap.clear();
            enemyStrategyHashMapCounter.clear();
        }
    }

    // Learn the enemy strategy
    public void learn(int nextCard) {
        learningRounds += 1;
        if (lastCard != 0) { // skip first round
            Integer addCard = getHdg().letzterZug(playerId ^ 1); // get card from last round
            if (enemyStrategyHashMap.containsKey(lastCard)) { // check if the card exists already in hashMap
                addCard += enemyStrategyHashMap.get(lastCard); // if it exists add the last and new value together
            }
            enemyStrategyHashMap.put(lastCard, addCard); // update hashMap
            if (enemyStrategyHashMapCounter.containsKey(lastCard)) { // check if card exists in the counter hashMap
                enemyStrategyHashMapCounter.put(lastCard, enemyStrategyHashMapCounter.get(lastCard) + 1); // add +1 if it exists
            } else {
                enemyStrategyHashMapCounter.put(lastCard, 1); // set to 1 if it doesn't exist
            }
        }
        lastCard = nextCard;
    }


    public int smartLearnStrategy(int nextCard) {
        int playCard = (int) Math.ceil((float) enemyStrategyHashMap.get(nextCard) / (float) enemyStrategyHashMapCounter.get(nextCard));
        if (playCard < 15) { // add 1 to the card played from the enemy based on the hashMap
            playCard += 1;
        }
        if (!notPlayed.contains(playCard)) { // when the card is already played iterate though the not played cards and choose the lowest
            int lowestNumber = 99;
            for (int i : notPlayed) {
                if (i < lowestNumber) {
                    lowestNumber = i;
                }
            }
            playCard = lowestNumber;
        }
        return playCard;
    }

    // Play Cards based on win chances
    public int percentCheckStrategy(int nextCard) {
        HashMap<Integer, Integer> possibleBestCard = new HashMap<>();
        for (int ownPossibleCard : notPlayed) { // iterate though all ArrayList to calculate win chances of a card
            for (int enemyPossibleCard : notPlayedEnemy) {
                for (int stillWinnableCard : stillWinnableCards)
                    if (ownPossibleCard > enemyPossibleCard && nextCard > stillWinnableCard / 2) {
                        if (!possibleBestCard.containsKey(ownPossibleCard)) {
                            possibleBestCard.put(ownPossibleCard, 1); // set to 1 when value doesn't exist in HashMap
                        } else {
                            possibleBestCard.put(ownPossibleCard, possibleBestCard.get(ownPossibleCard) + 1); // add +1 when value is already in HashMap
                        }
                    }
            }
        }

        int playCard = notPlayed.get((int) (Math.random() * notPlayed.size())); // play random Card when HashMap is empty
        int ratedValue = 0;
        for (int key : possibleBestCard.keySet()) { // iterate though hashMap to find highest value from hash map
            if (possibleBestCard.get(key) > ratedValue) {
                ratedValue = possibleBestCard.get(key);
                playCard = key;
            }
        }
        return playCard;
    }

    // Basic static strategy
    public int basicStaticStrategy(int Card) {
        if (Card < 0)
            return Card + 6;
        return Card + 5;
    }

    // Confuse dynamic bots with random cards
    public int trollEnemyStrategy() {
        int index = (int) (Math.random() * notPlayed.size());
        return notPlayed.get(index);
    }

    public int gibKarte(int nextCard) {
        playerId = getNummer(); // set current player

        strategyChanger();

        int cardToPlay;
        if (strategy == 0) {
            cardToPlay = trollEnemyStrategy(); // play random without sense to confuse other dynamic bots
        } else if (strategy == 1) {
            cardToPlay = smartLearnStrategy(nextCard); // learning strategy (good against static bots)
        } else if (strategy == 2) {
            cardToPlay = percentCheckStrategy(nextCard); // play based on percent win chances (okay against everything)
        } else {
            cardToPlay = basicStaticStrategy(nextCard); // fallback to static +5 strategy if nothing works
        }
        notPlayed.remove(Integer.valueOf(cardToPlay)); // remove card after playing

        learn(nextCard); // learn every round the enemy strategy

        return cardToPlay;
    }
}
