package de.FScheunert.HolsDerGeier.Bots;
import java.util.ArrayList;
import java.util.Random;

import de.FScheunert.HolsDerGeier.HolsDerGeierSpieler;

public class DarioClassGeierNeu extends HolsDerGeierSpieler{

    int r;
    boolean geht;
    int ausgewaehlt;
    int indexPointer;
    ArrayList<Integer> meineKarten = new ArrayList<Integer>();

    //Alles auf 0 setzen bevor das Spiel losgeht
    public void reset() {
        r=0;
        geht=true;
        ausgewaehlt=0;
        indexPointer=0;
        meineKarten.clear();
        for (int i=0;i<15;i++){
            meineKarten.add(i+1);
        }
    }

    //hier wird ausgewählt welche Karte ausgespielt wird
    public int gibKarte(int naechsteKarte) {
    	int cc = 0;
        do {
            r=new Random().nextInt(10)+1;

            switch (naechsteKarte){
                case 10:
                    switch (r){
                        case 1: case 2: case 3:
                            ausgewaehlt=15;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=14;
                            break;
                        case 7: case 8:
                            ausgewaehlt=13;
                            break;
                        case 9:
                            ausgewaehlt=12;
                            break;
                        case 10:
                            ausgewaehlt=11;
                            break;
                    }
                    break;

                case 9:
                    switch(r){
                        case 1: case 2:
                            ausgewaehlt=15;
                            break;
                        case 3: case 4: case 5:
                            ausgewaehlt=14;
                            break;
                        case 6: case 7:
                            ausgewaehlt=13;
                            break;
                        case 8: case 9:
                            ausgewaehlt=12;
                            break;
                        case 10:
                            ausgewaehlt=11;
                            break;
                    }
                    break;

                case 8:
                    switch (r){
                        case 1:
                            ausgewaehlt=15;
                            break;
                        case 2: case 3:
                            ausgewaehlt=14;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=13;
                            break;
                        case 7: case 8:
                            ausgewaehlt=12;
                            break;
                        case 9: case 10:
                            ausgewaehlt=11;
                            break;
                    }
                    break;

                case 7:
                    switch (r){
                        case 1:
                            ausgewaehlt=15;
                            break;
                        case 2: case 3:
                            ausgewaehlt=14;
                            break;
                        case 4: case 5:
                            ausgewaehlt=13;
                            break;
                        case 6: case 7: case 8:
                            ausgewaehlt=12;
                            break;
                        case 9: case 10:
                            ausgewaehlt=11;
                            break;
                    }
                    break;

                case 6:
                    switch (r){
                        case 1:
                            ausgewaehlt=15;
                            break;
                        case 2:
                            ausgewaehlt=14;
                             break;
                        case 3: case 4:
                            ausgewaehlt=13;
                            break;
                        case 5: case 6: case 7:
                            ausgewaehlt=12;
                            break;
                        case 8: case 9: case 10:
                            ausgewaehlt=11;
                            break;
                    }
                    break;

                case 5:
                    switch (r){
                        case 1: case 2: case 3:
                            ausgewaehlt=10;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=9;
                            break;
                        case 7: case 8:
                            ausgewaehlt=8;
                            break;
                        case 9:
                            ausgewaehlt=7;
                            break;
                        case 10:
                            ausgewaehlt=6;
                            break;
                    }
                    break;

                case -5:
                    switch(r){
                        case 1: case 2:
                            ausgewaehlt=10;
                            break;
                        case 3: case 4: case 5:
                            ausgewaehlt=9;
                            break;
                        case 6: case 7:
                            ausgewaehlt=8;
                            break;
                        case 8: case 9:
                            ausgewaehlt=7;
                            break;
                        case 10:
                            ausgewaehlt=6;
                            break;
                    }
                    break;

                case 4:
                    switch (r){
                        case 1:
                            ausgewaehlt=10;
                            break;
                        case 2: case 3:
                            ausgewaehlt=9;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=8;
                            break;
                        case 7: case 8:
                            ausgewaehlt=7;
                            break;
                        case 9: case 10:
                            ausgewaehlt=6;
                            break;
                    }
                    break;

                case -4:
                    switch (r){
                        case 1:
                            ausgewaehlt=10;
                            break;
                        case 2: case 3:
                            ausgewaehlt=9;
                            break;
                        case 4: case 5:
                            ausgewaehlt=8;
                            break;
                        case 6: case 7: case 8:
                            ausgewaehlt=7;
                            break;
                        case 9: case 10:
                            ausgewaehlt=6;
                            break;
                    }
                    break;

                case 3:
                    switch (r){
                        case 1:
                            ausgewaehlt=10;
                            break;
                        case 2:
                            ausgewaehlt=9;
                            break;
                        case 3: case 4:
                            ausgewaehlt=8;
                            break;
                        case 5: case 6: case 7:
                            ausgewaehlt=7;
                            break;
                        case 8: case 9: case 10:
                            ausgewaehlt=6;
                            break;
                    }
                    break;

                case -3:
                    switch (r){
                        case 1: case 2: case 3:
                            ausgewaehlt=5;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=14;
                            break;
                        case 7: case 8:
                            ausgewaehlt=3;
                            break;
                        case 9:
                            ausgewaehlt=2;
                            break;
                        case 10:
                            ausgewaehlt=1;
                            break;
                    }
                    break;

                case 2:
                    switch(r){
                        case 1: case 2:
                            ausgewaehlt=5;
                            break;
                        case 3: case 4: case 5:
                            ausgewaehlt=4;
                            break;
                        case 6: case 7:
                            ausgewaehlt=3;
                            break;
                        case 8: case 9:
                            ausgewaehlt=2;
                            break;
                        case 10:
                            ausgewaehlt=1;
                            break;
                    }
                    break;

                case -2:
                    switch (r){
                        case 1:
                            ausgewaehlt=5;
                            break;
                        case 2: case 3:
                            ausgewaehlt=4;
                            break;
                        case 4: case 5: case 6:
                            ausgewaehlt=3;
                            break;
                        case 7: case 8:
                            ausgewaehlt=2;
                            break;
                        case 9: case 10:
                            ausgewaehlt=1;
                            break;
                    }
                    break;

                case 1:
                    switch (r){
                        case 1:
                            ausgewaehlt=5;
                            break;
                        case 2: case 3:
                            ausgewaehlt=4;
                            break;
                        case 4: case 5:
                            ausgewaehlt=3;
                            break;
                        case 6: case 7: case 8:
                            ausgewaehlt=2;
                            break;
                        case 9: case 10:
                            ausgewaehlt=1;
                            break;
                    }
                    break;

                case -1:
                    switch (r){
                        case 1:
                            ausgewaehlt=5;
                            break;
                        case 2:
                            ausgewaehlt=4;
                            break;
                        case 3: case 4:
                            ausgewaehlt=3;
                            break;
                        case 5: case 6: case 7:
                            ausgewaehlt=2;
                            break;
                        case 8: case 9: case 10:
                            ausgewaehlt=1;
                            break;
                    }
                break;
                default: meineKarten.get(0);
                  
            }
            cc++;
            //hier wird geguckt ob die ausgewaehlte Karte sich tatsächlich in meiner Hand befindet
            geht = meineKarten.contains(ausgewaehlt);
            if(cc > 10) {
            	ausgewaehlt = meineKarten.get(0);
            	geht = true;
            }
        } while (!geht);

        //hier wird der Index von der ausgewaehlten Karte ermittelt damit man sie danach aus meiner Hand entfernen kann
        for (int i=0 ; i<meineKarten.size() ; i++){
            if (meineKarten.get(i)==ausgewaehlt){
                indexPointer = i;
            }
        }

        //hier wird die Karte aus meiner Hand entfernt
        meineKarten.remove(indexPointer);

        //hier wird die Karte ausgegeben
        return ausgewaehlt;
    }
}