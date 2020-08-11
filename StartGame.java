/**
*
* This class was made by Florian Scheunert
* 28.10.2019
*
**/
package de.FScheunert.HolsDerGeier;

import de.FScheunert.HolsDerGeier.Bots.Bot1;
import de.FScheunert.HolsDerGeier.FlosGeier.FlosFinalForm;

public class StartGame {
	
	public static void main(String[] args) throws Exception {
		
		HolsDerGeierSpieler player1 = new FlosFinalForm();
		HolsDerGeierSpieler player2 = new Bot1();
		
		HolsDerGeier hdg = new HolsDerGeier();
		hdg.neueSpieler(player1, player2);
		
		for(int i = 0; i < 1000; i++)
			hdg.newGame();
		
	}
	
}
