package de.FScheunert.HolsDerGeier.Bots;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PatternRecognition {
	
	private HashMap<Integer, Integer> data;
	
	public PatternRecognition(int enemyCard) {
		this.data = new HashMap<Integer, Integer>();
		update(enemyCard);
	}
	
	public void update(int enemyCard) {
		if(data.containsKey(enemyCard)) {
			int numberOfRepeats = data.get(enemyCard);
			
			data.put(enemyCard, numberOfRepeats + 1);
		}else {
			data.put(enemyCard, 0);
		}
	}
	
	public int getEnemyCard() {
		return Collections.max(data.entrySet(), Map.Entry.comparingByValue()).getKey();
	}

	public HashMap<Integer, Integer> getData() {
		return data;
	}
	
	public HashMap<Integer, Integer>getSortedHashMap(HashMap<Integer, Integer> dataNotSorted) {
		LinkedHashMap<Integer, Integer> sorted = new LinkedHashMap<>();
		dataNotSorted.entrySet()
			    .stream()
			    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
			    .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
	
		return sorted;	
	}
	
	
	
	
}