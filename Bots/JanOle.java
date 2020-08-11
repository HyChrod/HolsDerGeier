package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

/**
 * Der Geier legt je nach Karte immer genau einen bestimmten zugewiesenen Wert(Karte)
 *
 * 
 * 
 * 
 * @author (Jan-Ole) 
 * @version (Version 1)
 */
public class JanOle extends HolsDerGeierSpieler {
  
  /** ein Arry für die noch nicht gespielten Karten */
  private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
  
  
  /** löscht die noch nicht gespielten Karten und füllt diese dann wieder auf */
   public void reset () {
	   nochNichtGespielt.clear();
		for (int i=1;i<=15;i++)            
			nochNichtGespielt.add(i);
   }
  
   private int a= 15, b=14, c=13, d=12, e=11, f=10, g=9, h=8, i=7, j=6, k=5, l=4, m=3, n=2, o=1 ;
  
   public int gibKarte(int naechsteKarte) {
	   if(naechsteKarte==10) {
		   return a;
	   }else if(naechsteKarte==9) {
		   return b;
	   }else if(naechsteKarte==8) {
		   return c;
	   }else if(naechsteKarte==7) {
		   return d;
	   }else if(naechsteKarte==6) {
		   return j;
	   }else if(naechsteKarte==5) {
		   return k;
	   }else if(naechsteKarte==4) {
		   return l;
	   }else if(naechsteKarte==3) {
		   return m;
	   }else if(naechsteKarte==2) {
		   return n;
	   }else if(naechsteKarte==1) {
		   return o;
	   }else if(naechsteKarte==-1) {
		   return i;
	   }else if(naechsteKarte==-2) {
		   return h;
	   }else if(naechsteKarte==-3) {
		   return g;
	   }else if(naechsteKarte==-4) {
		   return f;
	   }else if(naechsteKarte==-5) {
		   return e; 
	   }else {
	  return naechsteKarte;
    }
   }

}