package pokemongame;

import java.util.Random;

/**
 * Der Schiedsrichter des Spiels. Regelt Ablaeufe und Statuseffekte.
 */
public class KampfSystem {

    /**
     * Führt eine komplette Kampfrunde aus inklusive Initiative-Prüfung und Giftschaden.
     */
    public static void fuehreRundeAus(Pokemon spieler, Pokemon gegner, int atkIndex, int runde, Random random) {
        
        // 1. Initiative prüfen
        boolean spielerZuerst = spieler.getEffectiveInit() > gegner.getEffectiveInit() ||
                        (spieler.getEffectiveInit() == gegner.getEffectiveInit() && runde % 2 != 0);

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
    /**
     * Berechnet den Typ-Multiplikator einer Attacke.
     * 
     * Beispiel: Glut (Feuer) gegen Bisasam (Pflanze)
     *   -> Feuer ist stark gegen Pflanze -> mult = 1.3
     *   -> Glumanda (Feuer) setzt Glut (Feuer) ein -> STAB -> mult = 1.5
     * 
     * Beispiel: Kratzer (Normal) gegen Schiggy (Wasser)
     *   -> Normal hat keinen Vor/Nachteil -> mult = 1.0
     *   -> Attackentyp != Angreifertyp -> kein STAB -> mult bleibt 1.0
     * 
     * Beispiel: Rankenhieb (Pflanze) gegen Glumanda (Feuer)
     *   -> Pflanze ist schwach gegen Feuer -> mult = 0.8
     */
    public static double berechneTypMultiplikator(String attackTyp, String defTyp, String angrTyp) {
        double mult;
        if (   (attackTyp.equals("Feuer")   && defTyp.equals("Pflanze"))
            || (attackTyp.equals("Pflanze") && defTyp.equals("Wasser"))
            || (attackTyp.equals("Wasser")  && defTyp.equals("Feuer"))) {
            mult = 1.3;
        } else if (
            (attackTyp.equals("Feuer")   && defTyp.equals("Wasser"))
            || (attackTyp.equals("Pflanze") && defTyp.equals("Feuer"))
            || (attackTyp.equals("Wasser")  && defTyp.equals("Pflanze"))) {
            mult = 0.8;
        } else {
            mult = 1.0;
        }
        // STAB: Attackentyp == Typ des Angreifers UND bereits Typvorteil -> 1.5
        if (mult == 1.3 && attackTyp.equals(angrTyp)) {
            mult = 1.5;
        }
        return mult;
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