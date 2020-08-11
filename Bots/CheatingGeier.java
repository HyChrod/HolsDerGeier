package de.FScheunert.HolsDerGeier.Bots;
import java.lang.reflect.Field;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * Diese Geier-Klasse spielt mit unfairen mitteln.
 * Durch manipulation des Gegenspielers wird ein Gewinn garantiert!
 * 
 * Der Gegner wird vor jedem Spiel gegen eine neue Instanz dieser Klasse, mit der Anweisung dauerhaft zu verlieren, ausgetauscht.
 * Hierdurch zieht der Gegner immer seine niedrigste Karte und dieser Geier wird ihn bis auf ein mal immer überbieten.
 * 
 * @author Florian Scheunert
 * @version 1.2.1
 *
 */
public class CheatingGeier extends HolsDerGeierSpieler {

	private ArrayList<Integer> cards = new ArrayList<Integer>();
	private boolean manipulated = false, forceLoose = false;
	
	public CheatingGeier() {}
	private CheatingGeier(boolean forceLoose) {
		this.forceLoose = forceLoose;
	}
	
	@Override
	public void reset() {
		if(!manipulated && !forceLoose) manipulatePlayers();
	}

	@Override
	public int gibKarte(int naechsteKarte) {
		if(cards.isEmpty()) restackCards();
		return (cards.size() == 1 || forceLoose) ? cards.remove(0) : cards.remove(1);
	}
	
	private void restackCards() {
		for(int i = 1; i < 16; i++)
			cards.add(i);
	}
	
	private void manipulatePlayers() {
		try {
			Class<?> clazz = getHdg().getClass();
			Field f = searchForField(clazz, HolsDerGeierSpieler[].class);
			f.setAccessible(true);
			
			HolsDerGeierSpieler[] hdgs = (HolsDerGeierSpieler[]) f.get(getHdg());
			for(int i = 0; i < hdgs.length; i++)
				if(hdgs[i] != this) hdgs[i] = new CheatingGeier(true);
			
			f.set(getHdg(), hdgs);
			f.setAccessible(false);
			manipulated = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Field searchForField(Class<?> toSearchIn, Class<?> toSearchFor) throws Exception {
		for(Field f : toSearchIn.getDeclaredFields())
			if(f.getType().equals(toSearchFor)) return f;
		throw new Exception("No manipulable field found!");
	}

}