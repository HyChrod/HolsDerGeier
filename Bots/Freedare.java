package de.FScheunert.HolsDerGeier.Bots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

public class Freedare extends HolsDerGeierSpieler {
	
	
	//Arraylists für die eigene Hand, die des Gegners und den Spielstapel
	private ArrayList<Integer> stack = new ArrayList<Integer>();
	private ArrayList<Integer> handIch = new ArrayList<Integer>();
	private ArrayList<Integer> handGegner = new ArrayList<Integer>();
	//WeitereAttribute
	private int countIch;
	private int countGegner;
	private int countDraws;
	private int unsereLetzeKarte;
	private int letzteZuGewinnendeKarte;
	private int tFaktor=0;
	boolean firstRound = true;
	//Für Leune sorgen
	private TacticsLib myVisualizer = new TacticsLib();
	private boolean visualize=false;
	
	
	//Konstruktor
	public Freedare (){
		reset();
	
	}
		
	//Reset Methode - stellt Spielbereitschaft her
	@Override
	public void reset() {
		
		
		//Letzte Runde auswerten und Taktik anpassen
        if (!firstRound) {
        	kalkuliereTaktik();
		} else {
			if (visualize) {
				myVisualizer.visualize("first-round",5000);
			}
		}
		
		
        //Arraylists leeren
		stack.clear();
        handGegner.clear();
        handIch.clear();
        
        countIch=0;
        countGegner=0;
        countDraws=0;

        //ArrayLists füllen
        for (int i=1;i<=15;i++){
            handGegner.add(i);
            handIch.add(i);
            if (i<6) {
				stack.add(i-6);	
			}else{
				stack.add(i-5);
			}
        } 
        
        
    }
	
	private void kalkuliereTaktik(){
		
		
		
		/**
		 * Errechne in Gib Kate den jeweils aktuellen stand des spiels 
		 * (immer eine runde rückwirkend, bzw mit der letzten restkarte in den jeweiligen arraylisten)
		 * Und vlt die Anzahl der Draws
		 * Und vlt den Durchscnitts Abstand zuz Standard Liste der vom Gegner gelegten Karten
		 * 
		 * Hier dann die Auswertung und Anpassung der Taktik über den T-Faktor
		 * 
		 */
		
		////System.out.println("Freedare: Gegnerische Punkte: "+countGegner);
		////System.out.println("Freedare: Unsere Punkte: "+countIch);
		////System.out.println("Freedare: Draws: "+countDraws);
		
		//Im Falle eines Gleichstandes wird der T-Faktor um 3 erhöht
		if (countIch==countGegner) {
			////System.out.println("Freedare: Previous T: "+tFaktor);
			////System.out.println("Freedare: Shifting T +3");
			tFaktor=tFaktor+3;
			if (visualize) {
				myVisualizer.visualize("changing-tactics",3000);
			}
			return;
		}
	
		//Bei mehr als 7 Draws im vergangenen Spiel wird der T-Faktor um 1 erhöht
		if (countDraws>7) {
			////System.out.println("Freedare: Previous T: "+tFaktor);
			////System.out.println("Freedare: Shifting T +1");
			tFaktor++;
			if (visualize) {
				myVisualizer.visualize("changing-tactics",3000);
			}
			return;
		}
		
		//Falls das letzte Spiel verloren wurde wird der T-Faktor um 4 erhöht
		if (countIch<countGegner) {
			////System.out.println("Freedare: Previous T: "+tFaktor);
			////System.out.println("Freedare: Shifting T +4");
			tFaktor=tFaktor+4;
			if (visualize) {
				myVisualizer.visualize("changing-tactics",3000);
			}
			return;
		}
		
		//Für den Fall das der Gegner knapp besiegt wurde (taktisch knapp, viele Punkte) wird der T-Faktor um 1 erhöht
		if (countIch-countGegner>=50) {
			////System.out.println("Freedare: Previous T: "+tFaktor);
			////System.out.println("Freedare: Shifting T +1");
			tFaktor++;
			if (visualize) {
				myVisualizer.visualize("changing-tactics",3000);
			}
			return;
		}
		
	}
	
	//Methode zum ausgeben einer Karte mit Entscheidungslogik
	@Override
	public int gibKarte(int naechsteKarte) {
		
		firstRound=false;
		
			//Variablen werden initialisiert
		   int ret;
	       int letzteKarteGegner = getHdg().letzterZug(getNummer() == 0 ? 1 : 0);
	       int prio;
	       
	       
	       //Gegnerische Hand aktualisieren und letzte Runde auswerfen
	       if (letzteKarteGegner!=-99 && letzteKarteGegner!=-1001 && letzteKarteGegner!=-1002){
	       handGegner.remove(handGegner.indexOf(letzteKarteGegner));
	       
	       //Counter für draws und Punkte aktualisieren
	       if (letzteZuGewinnendeKarte<0 && unsereLetzeKarte<letzteKarteGegner || letzteZuGewinnendeKarte>0 && unsereLetzeKarte>letzteKarteGegner) {
	    	   countIch=countIch+letzteZuGewinnendeKarte;
	       }else{
	    	   if (unsereLetzeKarte==letzteKarteGegner) {
				countDraws++;
			} else {
				countGegner=countGegner+letzteZuGewinnendeKarte;
			}
	       }
	       }
	       
	       //Letzte runde auswerten (bevor sie gespielt wird) da eine spätere Auswertung nicht möglich ist
	       if (stack.size()==1) {
			if (stack.get(0)<0 && handIch.get(0)<handGegner.get(0) || stack.get(0)>0 && handIch.get(0)>handGegner.get(0)) {
				countIch=countIch+stack.get(0);
			}else{
				if (handIch.get(0)==handGegner.get(0)) {
					countDraws++;
				} else {
					countGegner=countGegner+stack.get(0);
				}
			}
			
			if (visualize) {
				if (countIch>countGegner) {
					myVisualizer.visualize("good-round", 5000);
				}else{
					myVisualizer.visualize("bad-end", 5000);
				}
			}
			
		   }

	       
	       //Priorität festlegen:
	       if (naechsteKarte<0) {
			prio = naechsteKarte*-1;
	       } else {
	    	   prio = naechsteKarte;
	       }
	       


	      //Ret bestimmen:
	       /*
	        * Die auszuspielende Karte wird bestimmt:
	        * Entsprechend der Priorität werden mögliche Karten errechnet
	        * (im Prioritätsbereich von 1-5  werden durch das doppelte Vorkommen dieser Prioritäten 2 Karten in Erwägung gezogen)
	        * 
	        * Der T-Faktor verschiebt die Skala um ähnlich Spielenden Gegnern überlegen zu sein.
	        * (Nach dem Verschieben verzichtet diese Logik auf das obere Ende der Skala um im Mittelfeld bessere Chancen zu haben)
	        */
			if (tFaktor>15) {
				tFaktor=tFaktor%15;
			}
	       if (prio>=6) {
	    	  ret=prio+5+tFaktor;
		   } else {
			   ret=prio*2+tFaktor;
		   }
	       
	       
	       
	     //Fehler durch T-Faktor abfangen:
	       /*
	        * Komplettiert das verschieben der Skala indem es aus dem verfügbaren Wertebereich verschobene Fälle umbricht
	        */
			 if (ret>15) {
				ret=ret-15;
			}
			 
	       
			//Korrigiere doppeltes legen im Bereich der Karten mit gleicher Priorität 
			if (!handIch.contains(ret)) { 
		   	   if (ret-1<1) {
				ret=15;
		   	   }else {
		   		   ret=ret-1;
		   	   }
		    }
		       
		


		
	    //Eigenes Array aktualisieren:
	      handIch.remove(handIch.indexOf(ret));
	      stack.remove(stack.indexOf(naechsteKarte));
	      unsereLetzeKarte=ret;
	      letzteZuGewinnendeKarte=naechsteKarte;
	       
		return ret;
	}
	

	
	class TacticsLib extends JFrame{ 
		
		MathTools panel1 = new MathTools();
		int x;
		int y;
		
		public TacticsLib() { 
			super();
			
			Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize ();
			x = (int) screenSize.getWidth();
			y = (int) screenSize.getHeight();
			setLayout(null);
			panel1.setLayout(null);
			add(panel1);
			setUndecorated(true);
			panel1.setSize(x,y);
			setSize(x,y);
			
		}
		
		
		
		public void visualize(String command, int time){
			
			panel1.setPicture(command);
			
			setVisible(true);
			requestFocus();
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				////System.out.println("Fehler während der Sleep-Time");
			}
			setVisible(false);
			
		}

		
	}
	
	class MathTools extends JPanel{
		
		URL url=null;
		
		public MathTools() {
			super();			
		}
		
		public void setPicture(String command){
			
			try {
				url = new URL("http://freedpf.fr.funpic.de/holsdergeier/"+command+".jpg");
			} catch (MalformedURLException e) {
				////System.out.println("Der Visualizer konnte eine URL nicht abrufen!");
			}
			
		}
		
		@Override
		public void paint (Graphics g){
			
			Graphics2D g2d = (Graphics2D)g;

			Image img1 = Toolkit.getDefaultToolkit().getImage(url);
			
			Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize ();
			int x = (int) screenSize.getWidth();
			int y = (int) screenSize.getHeight();
			
			g2d.setColor(new Color(0,0,0,255));
			
			g2d.fillRect(0, 0, x, y);
		
			g2d.drawImage(img1,x/2-img1.getWidth(this)/2,y/2-img1.getHeight(this)/2, this);
			
		}
		
	}

}
