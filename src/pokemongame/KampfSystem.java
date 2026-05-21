package pokemongame;

import java.util.Random;

/**
 * Der Schiedsrichter des Spiels. Regelt Abläufe und Statuseffekte.
 */
public class KampfSystem {

    /**
     * Führt eine komplette Kampfrunde aus inklusive Initiative-Prüfung und Giftschaden.
     */
    public static void fuehreRundeAus(Pokemon spieler, Pokemon gegner, int atkIndex, int runde, Random random) {
        
        // 1. Initiative prüfen
        boolean spielerZuerst = spieler.init > gegner.init || 
                                (spieler.init == gegner.init && runde % 2 != 0);

        if (spielerZuerst) {
            if (spieler.kannAgieren()) {
                spieler.fuehreAktionAus(gegner, atkIndex);
            }
            if (gegner.hp > 0) {
                Rivale.fuehreZufallsAktionAus(gegner, spieler, random);
            }
        } else {
            Rivale.fuehreZufallsAktionAus(gegner, spieler, random);
            if (spieler.hp > 0) {
                if (spieler.kannAgieren()) {
                    spieler.fuehreAktionAus(gegner, atkIndex);
                }
            }
        }

        // 2. Rundenende abwickeln (Giftschaden)
        verarbeiteGiftschaden(spieler);
        verarbeiteGiftschaden(gegner);
    }

    private static void verarbeiteGiftschaden(Pokemon p) {
        if (p.hp > 0 && p.istVergiftet) {
            double giftSchaden = Math.round(p.maxHp * 0.10);
            p.hp -= giftSchaden;
            if (p.hp < 0) p.hp = 0;
            
            System.out.printf("[GIFT] %s verliert %.0f HP durch Gift! -> %.0f/%d HP %n", 
                              p.name, giftSchaden, p.hp, p.maxHp);
        }
    }
}